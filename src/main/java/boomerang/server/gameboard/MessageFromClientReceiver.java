package boomerang.server.gameboard;
import java.io.ObjectInputStream;
import java.io.IOException;



public class MessageFromClientReceiver {

    public static String waitForMessage(ObjectInputStream inFromClient) {
        try {
            return (String) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}




