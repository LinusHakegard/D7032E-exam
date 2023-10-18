package boomerang.server;
import java.io.ObjectInputStream;
import java.io.IOException;


//GÖRA STATISK SEN
public class MessageFromClientReceiver {
    private final ObjectInputStream inFromClient;

    public MessageFromClientReceiver(ObjectInputStream inFromClient) {
        this.inFromClient = inFromClient;
    }

    public String waitForMessage() {
        try {
            return (String) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}




