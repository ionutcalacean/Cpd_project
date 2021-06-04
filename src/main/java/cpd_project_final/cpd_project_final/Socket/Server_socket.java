package cpd_project_final.cpd_project_final.Socket;

import cpd_project_final.cpd_project_final.Controller;
import cpd_project_final.cpd_project_final.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class Server_socket {

    public static final Logger LOGGER = LoggerFactory.getLogger(Server_socket.class);
    private ServerSocket serverSocket;


    public void start(int port, Client_socket client_socket, SocketConfig socketConfig, Token token){
        try {
            serverSocket = new ServerSocket(port);

            while(true){
                Socket client = serverSocket.accept();
                new ClientHandler(client,client_socket,socketConfig,token).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            stop();
        }


    }

    private static class ClientHandler extends Thread{
        private Socket clientSocket;
        private Client_socket client_for_next_server;
        private SocketConfig socketConfig;
        private Token token;

        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket,Client_socket client_socket,SocketConfig socketConfig,Token token){
            this.clientSocket=socket;
            this.client_for_next_server =  client_socket;
            this.socketConfig = socketConfig;
            this.token = token;

        }

        public void run(){

            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String input;
                while ((input =in.readLine() )!= null) {
                    if(input.contains("Election")){
                        String split[] =input.split("=");
                        int bestIdSoFar = Integer.parseInt(split[1]);
                        if(bestIdSoFar == socketConfig.getMyId()){
                            LOGGER.info("Sent leader id = "+bestIdSoFar);
                            out.println("Response confirmed");
                            client_for_next_server.sendMessage("Leader is ="+bestIdSoFar);


                        }
                        else if(socketConfig.getMyId() < bestIdSoFar){
                            Controller.leaderId = new AtomicInteger(bestIdSoFar);
                            LOGGER.info("Received message election id ="+bestIdSoFar +" send id = "+bestIdSoFar);
                            out.println("Response confirmed");
                            String resp =client_for_next_server.sendMessage("Election bestId ="+bestIdSoFar);



                        }else{
                            Controller.leaderId = new AtomicInteger(socketConfig.getMyId());
                            LOGGER.info("Received message election id ="+bestIdSoFar +" send id = "+socketConfig.getMyId());
                            out.println("Response confirmed");
                            String resp =client_for_next_server.sendMessage("Election bestId="+socketConfig.getMyId());


                        }


                    }
                    else if(input.contains("Leader")){
                        String split[] =input.split("=");
                        int bestId = Integer.parseInt(split[1]);
                        Controller.leaderId = new AtomicInteger(bestId);
                        LOGGER.info("leader set");
                    }
                    else if(input.contains("Token send")){
                        LOGGER.info("Tokken arrived");
                        token.setTimeWhenReceived(new AtomicLong(System.currentTimeMillis()));
                        token.setTokenIsHERE(new AtomicInteger(1));


                    }
                    else if(input.contains("close connection")){
                        break;
                    }

                out.println("Response confirmed");




                }
                LOGGER.info("EXITED!!");
                in.close();
                out.close();
                clientSocket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void stop(){
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
