
package netassign;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStore implements Runnable {
    public String dir = "C:\\Users\\user\\Documents\\NetBeansProjects\\NetAssign\\src\\netassign\\Server";
    private Connection connection;
    
    public ServerStore(Socket socket) throws IOException {
        connection = new Connection(socket);
    }
    
    @Override
    public void run() {
        try {
            
            byte[] buffer = new byte[4];
            connection.readAll(buffer);
            int metaLength = ByteBuffer.wrap(buffer).getInt();
            
            buffer = new byte[metaLength];
            connection.readAll(buffer);
            
            FileMeta fileMeta = null;
            try {
                fileMeta = getFileMeta(buffer);
            } catch (ClassNotFoundException ex) {
                System.out.println("Failed to deserialize file meta data");
                return;
            }
            
            String path = dir + "\\" + fileMeta.fileName;
            FileOutputStream os = new FileOutputStream(path);
            
            buffer = new byte[16 * 1024];
            int length;
            while ((length = connection.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            
            os.flush();
            os.close();
            
            String checkSum = FileUtility.getChecksum(path);
            if (checkSum.equals(fileMeta.checksum)) {
                System.out.println("File checksum match " + fileMeta.checksum);
            }
            else {
                
                System.out.println("File checksum did not match");
            }
            connection.close();
        } catch (IOException ex) {
            System.out.println("Failed to receive file");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ClientToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private FileMeta getFileMeta(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        
        ObjectInputStream ois = new ObjectInputStream(bis);
        
        return (FileMeta)ois.readObject();
    }
}
