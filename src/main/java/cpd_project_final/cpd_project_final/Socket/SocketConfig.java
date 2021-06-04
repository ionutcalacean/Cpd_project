package cpd_project_final.cpd_project_final.Socket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SocketConfig {

    @Value("${input_port}")
    private  int input_port;
    @Value("${output_port}")
    private  int output_port;
    @Value("${myID}")
    private int myId;

    public int getInput_port() {
        return input_port;
    }

    public void setInput_port(int input_port) {
        this.input_port = input_port;
    }

    public int getOutput_port() {
        return output_port;
    }

    public void setOutput_port(int output_port) {
        this.output_port = output_port;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }
}
