import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.Date;

public class Client {
    public static final String IP_ADDR = "localhost";//服务器地址
    public static final int PORT = 50001;//服务器端口号
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

    public static String gb2312ToUtf8(String str) {

        String urlEncode = "" ;

        try {

            urlEncode = URLEncoder.encode (str, "UTF-8" );

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        }

        return urlEncode;

    }

    public static void main(String[] args) {
        Socket socket = null;

        try {
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(IP_ADDR, PORT);
            OutputStreamWriter outputStreamWriter=null;
            outputStreamWriter=new OutputStreamWriter(socket.getOutputStream(),"UTF-8");
            BufferedWriter bw=new BufferedWriter(outputStreamWriter);
            JSONObject jsonObject=new JSONObject();
            String send="用户名;密码：789\n毛泽东是伟人";
            jsonObject.put("data",send);
            jsonObject.put("time",(new Date()).toString());
            jsonObject.put("number",100);
            bw.write(jsonObject.toString()+"\r\n");
            bw.flush();
            socket.shutdownOutput();//关闭输出流
            //3.获取输入流，并读取服务器端的响应信息
            InputStream is=socket.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String info=null;
            while((info=br.readLine())!=null){
                System.out.println(info);
            }
            //4.关闭资源
            br.close();
            is.close();
            outputStreamWriter.close();
        } catch (Exception e) {
            System.out.println("客户端异常:" + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    //inSR.close();
                } catch (IOException e) {
                    socket = null;
                    System.out.println("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
    }
}

