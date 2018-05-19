import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int queueSize = 20;
    private int port = 50001;
    private ServerSocket serverSocket;

    public Server(){
        try{
            serverSocket = new ServerSocket();
            //关闭serverSocket时，立即释放serverSocket绑定端口以便端口重用，默认为false
            serverSocket.setReuseAddress(true);
            //accept等待连接超时时间为1000毫秒，默认为0，永不超时
//            serverSocket.setSoTimeout(10000);
            //为所有accept方法返回的socket对象设置接收缓存区大小，单位为字节，默认值和操作系统有关
            serverSocket.setReceiveBufferSize(128*1024);
            //设置性能参数，可设置任意整数，数值越大，相应的参数重要性越高（连接时间，延迟，带宽）
            serverSocket.setPerformancePreferences(3, 2, 1);
            //服务端绑定至端口，queueSize为服务端连接请求队列长度
            serverSocket.bind(new InetSocketAddress(port), queueSize);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void service(SensitivewordFilter filter,ExecutorService pool){
        while(true){
            Socket socket = null;
            try{
                //从连接请求队列中取出一个客户连接请求，创建与客户连接的socket对象
                //如果队列中没有请求，accept方法就会一直等待
                socket = serverSocket.accept();
                InetAddress address=socket.getInetAddress();
                System.out.println(address.getHostAddress());
                Callable<Void> task=new ThreadTask(socket,filter);
                pool.submit(task);
//                ThreadTask threadTask=new ThreadTask(socket,filter);
                //Thread thread = new Thread(new ServiceHandler(socket,filter));
                //thread.start();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorService pool= Executors.newFixedThreadPool(50);//使用线程池防止DDRS使服务器停止工作
        final SensitivewordFilter filter = new SensitivewordFilter();
        Server server = new Server();
        server.service(filter,pool);
    }
}
