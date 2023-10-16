package boomerang;

import boomerang.server.*;
import boomerang.client.*;
import java.io.*;

public class Boomerang {
    public static void main(String[] args) throws Exception{
        //arg 0 = number of players, arg 1 = number of bots, arg 2 = game edition

        if(args.length == 3){
            try{
                GameServer server = new GameServer(Integer.parseInt(args[0]));
                server.waitForPlayers();
            }
            catch(IOException e){
                System.out.println("oh");
            }
        }
        //arg 0 = server ip
        else if(args.length == 1){
            try{
                GameClient client = new GameClient(args[0]);
                client.run();
            }
            catch(IOException e){
                System.out.println("oh");
            }
        }
    }
}
