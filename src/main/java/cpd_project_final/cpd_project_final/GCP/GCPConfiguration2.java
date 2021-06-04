package cpd_project_final.cpd_project_final.GCP;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GCPConfiguration2 {
    public static final Logger LOGGER = LoggerFactory.getLogger(GCPConfiguration2.class);
    private static String cacheMessage;
    private static PubSubTemplate pubSubTemplate;

    public GCPConfiguration2(PubSubTemplate pubSubTemplate)
    {
        this.pubSubTemplate=pubSubTemplate;

    }

    public static void configureSubscriptions(String[] subscr){
        for(String sub: subscr){
            pubSubTemplate.subscribe(sub,(message)->{
                if(!message.getPubsubMessage().getData().toStringUtf8().equals(cacheMessage)){
                    String split[] = sub.split("-");
                    String topic = split[0];
                    LOGGER.info(topic +":"+ message.getPubsubMessage().getData().toStringUtf8());
                }

                message.ack();
            });
        }
    }


   public static void testSend(String topic,String message){
       pubSubTemplate.publish(topic,message);
       cacheMessage = message;
   }


}
