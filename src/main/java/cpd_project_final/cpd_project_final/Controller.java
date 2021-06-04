package cpd_project_final.cpd_project_final;

import cpd_project_final.cpd_project_final.Socket.Client_socket;
import cpd_project_final.cpd_project_final.Socket.Server_socket;
import cpd_project_final.cpd_project_final.Socket.SocketConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Controller {

    public static AtomicInteger leaderId = new AtomicInteger(0);
    public static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    private Client_socket client_socket;
    private Token token;
    private SocketConfig socketConfig;

    public Controller(int port,SocketConfig socketConfig,Token token){
        client_socket = new Client_socket();
        this.socketConfig=socketConfig;
        this.token = token;
        token.start();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            public void run() {
                new Server_socket().start(port,client_socket,socketConfig,token);

            }
        });
    }

    public int test_connection(int port){


        int flag =0;

        long startCount =System.currentTimeMillis();
        long endCount;
        while(true){
            try{

                client_socket.startConnection("127.0.0.1", port);
                LOGGER.info("client connected to:"+port);
                String resp1 = client_socket.sendMessage("connection initialization");

                System.out.println("Response from "+port +" :" + resp1);
                flag =1;
                break;
            }catch(Exception e){
                LOGGER.info("client trying again to connect to due to exception:"+port);
            }
            endCount =System.currentTimeMillis();
            if(endCount - startCount >15000){
                System.out.println("Connection closed, time exceded.");
                flag=-1;
                break;
            }
        }



        return flag;
    }

    public void choose_leader(int myId){
        client_socket.sendMessage("Election myId ="+myId);

    }

    public void start_token_transmision()
    {
        LOGGER.info("This app id:"+socketConfig.getMyId() +" leader id: "+leaderId);
        if(socketConfig.getMyId() == leaderId.intValue())
        {
            token.setTimeWhenReceived(new AtomicLong(System.currentTimeMillis()));
            token.setTokenIsHERE(new AtomicInteger(1));
        }

    }






}
