
package netassign;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientToServer implements Runnable{
    private File file;
    private Connection connection;
    private Client client;
    private FileMeta fileMeta;
    
    public ClientToServer(File file, Client client) {
        this.file = file;
        this.client = client;        
    }
    
    @Override
    public void run() {
        
        try {
            this.connection = new Connection("localhost", 9999);
        } catch (IOException ex) {
            System.out.println("Error creating connection");
            client.onDone();
            return;
        }
        
        byte[] fileMetBytes = null;
        try {
            fileMeta = new FileMeta();
            fileMeta.fileName = file.getName();
            fileMeta.checksum = FileUtility.getChecksum(file.getPath());

            
            fileMetBytes = getFileMetaBytes();
        } catch (IOException ex) {
            System.out.println("Error serializing file metadata");
            client.onDone();
            return;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientToServer.class.getName()).log(Level.SEVERE, null, ex);
            client.onDone();
            return;
        }
         
        try {
            byte[] metLengthBytes = ByteBuffer.allocate(4).putInt(fileMetBytes.length).array();
            connection.write(metLengthBytes);
            connection.write(fileMetBytes);
            
            FileInputStream fis = new FileInputStream(file);
            
            byte[] buffer = new byte[16 * 1024];
            
            int length;
            while ((length = fis.read(buffer)) > 0) {
                connection.write(buffer, 0, length);
            }
            
            fis.close();
            
            connection.close();
        
            client.onDone();
        } catch (IOException ex) {
            System.out.println("Sending file failed");
            client.onDone();
        }
    }
    
    private byte[] getFileMetaBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(fileMeta);
        oos.flush();
        
        return bos.toByteArray();
    }
}
