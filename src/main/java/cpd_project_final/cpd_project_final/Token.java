package cpd_project_final.cpd_project_final;




import cpd_project_final.cpd_project_final.Socket.Client_socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Token extends Thread {

    public static final Logger LOGGER = LoggerFactory.getLogger(Token.class);
    private AtomicLong timeWhenReceived;
    private AtomicInteger tokenIsHERE;
    private int port;


    public Token(int port){
        timeWhenReceived = new AtomicLong(0);
        tokenIsHERE =  new AtomicInteger(0);
        this.port = port;
    }

    @Override
    public void run() {
        long presentTime;
        while(true){
            if(tokenIsHERE.intValue()==1){
                presentTime = System.currentTimeMillis();
                if(presentTime - timeWhenReceived.longValue()>=20000){
                    try{
                        setTokenIsHERE(new AtomicInteger(0));
                        Client_socket client_socket = new Client_socket();
                        client_socket.startConnection("127.0.0.1", port);
                        client_socket.sendMessage("Token send");
                        LOGGER.info("Token given");

                    }catch(Exception e){

                    }
                }
            }
        }
    }

    public AtomicLong getTimeWhenReceived() {
        return timeWhenReceived;
    }

    public void setTimeWhenReceived(AtomicLong timeWhenReceived) {
        this.timeWhenReceived = timeWhenReceived;
    }

    public AtomicInteger getTokenIsHERE() {
        return tokenIsHERE;
    }

    public void setTokenIsHERE(AtomicInteger tokenIsHERE) {
        this.tokenIsHERE = tokenIsHERE;
    }
}
