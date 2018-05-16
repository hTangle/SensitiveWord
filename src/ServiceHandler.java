import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;



public class ServiceHandler implements Runnable {
    private Socket socket;
    SensitivewordFilter filter;
    public ServiceHandler(Socket socket,SensitivewordFilter filter){
        this.socket = socket;
        this.filter=filter;
    }

    @Override
    public void run() {
        DataOutputStream output = null;
        DataInputStream input = null;
        try{
            output = SocketIO.getOutput(socket);
            input = SocketIO.getInput(socket);

            int len = input.readInt();
            byte[] bytes = new byte[len];
            input.read(bytes);
            String request = new String(bytes, "utf-8");
            System.out.println(request);
            //request为json格式，需要解析得到最后的字符串
            //request=MyFormat.getTheJson(request);
            //if(request.equals("")){
            String answer=filter.replaceSensitiveWord(request, 1,"*");
            System.out.println(answer);
            bytes = answer.getBytes("utf-8");
            len = bytes.length;
            output.writeInt(len);
            output.write(bytes);
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
