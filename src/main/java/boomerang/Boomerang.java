package boomerang;

import boomerang.server.*;
import boomerang.client.*;
import java.io.*;

public class Boomerang {
    public static void main(String[] args) throws Exception{
        //arg 0 = number of players, arg 1 = game edition

        if(args.length == 2){
            try{
                GameServer server = new GameServer(Integer.parseInt(args[0]),args[1]);
                server.waitForPlayers();
            }
            catch(IOException e){
                System.out.println("error");
            }
        }
        //arg 0 = server ip
        else if(args.length == 1){
            try{
                GameClient client = new GameClient(args[0]);
                client.run();
            }
            catch(IOException e){
                System.out.println("error");
            }
        }
    }
}
