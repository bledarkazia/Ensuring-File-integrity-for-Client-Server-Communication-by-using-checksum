package netassign;
import java.util.Scanner;
import java.util.Arrays;
import java.util.concurrent.*;
import java.net.*; 
import java.io.*; 
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private  int currentConnections;
    
    private static Integer conc = 1;
    private static String pDir = "C:\\Users\\user\\Documents\\NetBeansProjects\\NetAssign\\src\\netassign\\Client\\10MB";

    
    public synchronized void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Desired Concurrency: ");
        int conc = 1;

        try{
            conc = scanner.nextInt();
        } catch (Exception exception) {
            conc = 1;
        }
        
        Queue<File> files = getFiles();
        
        long start = System.currentTimeMillis();
        
        while (!files.isEmpty()) {
            while (currentConnections < conc && !files.isEmpty()) {
                File file = files.remove();
                ClientToServer transfer = new ClientToServer(file, this);
                Thread thread = new Thread(transfer);
                
                System.out.println("Transfering file");
                thread.start();
                
                currentConnections++;
            }
            
            while (currentConnections == conc) {
                try { 
                    wait();
                } catch (InterruptedException e)  {
                    Thread.currentThread().interrupt(); 
                }
            }
        }
        
        
        long end = System.currentTimeMillis();
        long transferTime = (end-start) / 1000;
        System.out.println("Time needed for concurrency" + conc + " is:: " + Long.toString(transferTime));
    }
    
    public synchronized void onDone() {
        System.out.println("File transfered successfully");
        currentConnections--;
        notifyAll();
    }
    
     public Queue<File> getFiles(){
        File folder = new File(pDir);
        File[] listOfFiles = folder.listFiles();
        LinkedList<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(listOfFiles));
        return files;
    }
     
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.run();
    }
}