
package netassign;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
    private Socket socket;
    
    private DataInputStream inStream;
    private OutputStream outStream;
    
    public Connection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        
        inStream = new DataInputStream(socket.getInputStream());
        outStream = socket.getOutputStream();
    }
    
    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        
        inStream = new DataInputStream(socket.getInputStream());
        outStream = socket.getOutputStream();
    }
    
    public void write(byte[] bytes, int offset, int length) throws IOException {
        outStream.write(bytes, offset, length);
    }
    
    public void write(byte[] bytes) throws IOException {
        outStream.write(bytes);
    }
    
    public void readAll(byte[] buffer) throws IOException {
        inStream.readFully(buffer);
    }
    
    public int read(byte[] buffer) throws IOException {
        return inStream.read(buffer);
    }
  
    public Socket getSocket() {
        return socket;
    }
    
    public void close() throws IOException {
        socket.close();
    }
}
