import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
public class ChatServer{
static CopyOnWriteArrayList<ClientHandler>clientList= new CopyOnWriteArrayList<>();
public static void main(String[] args) throws IOException{
ServerSocket serverSocket= new ServerSocket(5353);
System.out.println("Server started...");
new Thread(()->{Scanner sc= new Scanner(System.in);
while(true){
String msg=sc.nextLine();
broadcast("Server:"+msg,null);
}
}).start();
Socket clientSocket=serverSocket.accept();
ClientHandler handler= new ClientHandler(clientSocket);
clientList.add(handler);
handler.start();
}
}
public static void broadcast(String message, ClientHandler sender){
for(ClientHandler client:clientList){
if(client!=sender){
try{
client.dos.writeUTF(message);
}catch(IOException e){
System.out.println("Error sending message to"+client.name);
}
}
}
}
}
class ClientHandler extends Thread{
String name;
Socket socket;
DataInputStream dis;
DataOutputStream dos;
public ClientHandler(Socket socket) throws IOException{
this.socket=socket;
this.dis=new DataInputStream(socket.getInputStream());
this.dos=new DataOutputStream(socket.getOutputStream());
}
public void run(){
try{
dos.writeUTF("Enter your name:");
name=dis.readUTF().trim();
System.out.println(name+" joined.");
ChatServer.broadcast(name+" joined the chat.",this);
while(true){
String msg=dis.readUTF();
if("exit".equalsIgnoreCase(msg)){
System.out.println(name+" left.");
ChatServer.broadcast(name+" left the chat.",this);
break;
}
System.out.println(name + ":"+msg);
ChatServer.broadcast(name+":"+msg,this);
}
}catch(IOException e){
System.out.println(name+" disconnected.");
ChatServer.broadcast(name+" disconnected unexpectedly.",this);
}
finally{
try{
dis.close();
dos.close();
socket.close();
}
catch (IOException e){}
ChatServer.clientList.remove(this);
}
}
}
