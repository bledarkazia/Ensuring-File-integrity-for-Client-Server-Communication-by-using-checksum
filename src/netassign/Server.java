package netassign;

import java.net.*; 
import java.io.*; 


public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = null;
        try {
                ss = new ServerSocket(9999);
                
                while(true){
                    try {
                            Socket s = ss.accept();
                            System.out.println("Connected: Getting Files");
                            Runnable runnable = new ServerStore(s);
                            Thread thread = new Thread(runnable);
                            thread.start();
                           
                    } catch (IOException ex) {
                        System.out.println("Connection can not be established");
                    }
                }
        } 
        catch (IOException ex) {
            System.out.println("This port is not idle");
        }
    }
}