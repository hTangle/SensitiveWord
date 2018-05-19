import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ThreadTask implements Callable<Void> {
    private Socket socket;
    private SensitivewordFilter filter;

    public ThreadTask(Socket socket, SensitivewordFilter filter) {
        this.socket = socket;
        this.filter = filter;
    }

    @Override
    public Void call() throws Exception {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        OutputStreamWriter outputStreamWriter = null;
        try {
            //获取输入流，并读取客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(isr);
            String info = null;
            String requests="";
            while(true){
                info=br.readLine();
                if(info==null){
                    break;
                }
                requests+=info.trim();
            }
            //info=br.readLine();
            socket.shutdownInput();//关闭输入流
            System.out.println(requests);
            info=JsonFilter.FromStringToJson(filter.replaceSensitiveWord(JsonFilter.FromJsonToString(requests),1,"*"));

            //获取输出流，响应客户端的请求
            outputStreamWriter = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            BufferedWriter bufferedWriter=new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(info+"\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (outputStreamWriter != null)
                    outputStreamWriter.close();
                if (os != null)
                    os.close();
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GB2312
                String s = encode;
                return s;      //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {   //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {      //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "null";
    }
}