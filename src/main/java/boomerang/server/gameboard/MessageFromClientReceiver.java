package boomerang.server.gameboard;
import java.io.ObjectInputStream;
import java.io.IOException;

//waits and returns for message from client
public class MessageFromClientReceiver {

    public static String waitForMessage(ObjectInputStream inFromClient) {
        try {
            return (String) inFromClient.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}




