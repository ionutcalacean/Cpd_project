package cpd_project_final.cpd_project_final.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Client_socket {

    public static final Logger LOGGER = LoggerFactory.getLogger(Client_socket.class);
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port){
        try {
            clientSocket = new Socket(ip,port);
            out = new PrintWriter(clientSocket.getOutputStream(),true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Client initialization failed!");;
        }
    }

    public String sendMessage(String msg){
        LOGGER.info("sending message "+msg);
        out.println(msg);
        String resp = null;
        try {
            resp = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("confirmation message "+resp);
        return resp;
    }

    public void stopConnection(){
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
