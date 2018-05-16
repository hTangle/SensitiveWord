import java.io.DataInputStream;  
import java.io.DataOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.Socket;  
  
public class SocketIO{  
    public static DataInputStream getInput(Socket socket) throws IOException{  
        //���ջ�������С��socket��ȡ������֮ǰ����  
        socket.setReceiveBufferSize(10);  
        InputStream input = socket.getInputStream();  
        return new DataInputStream(input);  
    }  
      
    public static DataOutputStream getOutput(Socket socket) throws IOException{  
        //���ͻ�������С��socket��ȡ�����֮ǰ����  
        socket.setSendBufferSize(10);  
        OutputStream output = socket.getOutputStream();  
        return new DataOutputStream(output);  
    }  
}
