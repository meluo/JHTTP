package jhttp;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.omg.CORBA.Request;

//基于TCP协议实现HTTP服务器
//http协议是基于TCP协议的半双工协议
//由于时间有限仅实现了http服务器的GET方法
public class JHTTP extends Thread {
private File documentRootDirectory;//网站的文件地址
private String indexFileName="index.html";//默认打开的文件是index.html
private ServerSocket server;//定义TCP协议实现
private int numThreads=50;//开启的线程数

public JHTTP(File documentRootDirectory,int port,String indexFileName)throws IOException{
if(!documentRootDirectory.isDirectory()){//判断传入的文件目录是否合法 
throw new IOException(documentRootDirectory+"不是一个文件目录");
}
this.documentRootDirectory=documentRootDirectory;
this.indexFileName=indexFileName;
this.server=new ServerSocket(port);//实例化套接字
}
private JHTTP(File documentRootDirectory, int port)throws IOException {
this(documentRootDirectory,port,"index.html");
}
public void run(){
for (int i=0;i<numThreads;i++){
Thread t=new Thread(new RequestProcessor(documentRootDirectory,indexFileName));
t.start();
}
System.out.println("服务器的端口是 ："+server.getLocalPort());
System.out.println("服务目录是："+documentRootDirectory);
while(true){
try{
Socket request=server.accept();//接收来自浏览器的请求
RequestProcessor.processRequest(request);//把请求放到静态方法中
}catch(IOException e){
}
}
}
public static void main(String[] args) {
File docroot=new File("./NetFile");//设置默认网站根目录
int port=8080;//设置默认端口号为8080；
try{
JHTTP webserver=new JHTTP(docroot, port);
webserver.start();
}catch (IOException e){
System.out.println("服务器不能够启动，抛出的异常是： "+e.getClass());
System.out.println(e);
}
}
}