package cpd_project_final.cpd_project_final;

import cpd_project_final.cpd_project_final.GCP.GCPConfiguration2;
import cpd_project_final.cpd_project_final.GCP.GcpConfig;
import cpd_project_final.cpd_project_final.Socket.SocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootApplication
@EnableScheduling
public class CpdProjectFinalApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(CpdProjectFinalApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CpdProjectFinalApplication.class, args);



    }

    @Bean
    public CommandLineRunner runner(SocketConfig socketConfig, GcpConfig gcpConfig){
        CommandLineRunner runner = new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                LOGGER.info("Server port is:" + socketConfig.getInput_port());

                Token token = new Token(socketConfig.getOutput_port());
                Controller controller = new Controller(socketConfig.getInput_port(),socketConfig,token);
                int flag = controller.test_connection(socketConfig.getOutput_port());

                String subs = gcpConfig.getSubscribe();
                String subscr[]= subs.split(",");
                GCPConfiguration2.configureSubscriptions(subscr);


                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.submit(new Runnable() {
                    public void run() {

                        Scanner scanner = new Scanner(System.in);
                        String line;
                        while(true){
                            line = scanner.nextLine();
                            if(line.contains("choose leader"))
                            {
                                controller.choose_leader(socketConfig.getMyId());
                            }
                            else if(line.contains("leader start"))
                            {
                                controller.start_token_transmision();
                            }


                            if(token.getTokenIsHERE().intValue()==1)
                            {
                                if(line.contains("Exit"))
                                {
                                    break;
                                }
                                else
                                {
                                    String topics = gcpConfig.getTopics();
                                    String topic[] =topics.split(",");

                                    String topic_message[] = line.split(":");
                                    for(String top: topic){
                                        if(top.equals(topic_message[0])){
                                            GCPConfiguration2.testSend(topic_message[0],topic_message[1]);
                                        }
                                    }

                                }
                            }


                        }
                    }
                });



            }
        };
        return runner;
    }

}
