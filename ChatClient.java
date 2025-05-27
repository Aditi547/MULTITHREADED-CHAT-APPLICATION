import java.io.*;
import java.net.*;
import java.util.Scanner;
public class ChatClient{
public static void main(String[] args) throws IOException{
Socket socket=new Socket("localhost",5353);
DataInputStream dis=new DataInputStream(socket.getInputStream());
DataOutputStream dos=new DataOutputStream(socket.getOutputStream());
Scanner sc= new Scanner(System.in);
System.out.print(dis.readUTF());
String name=sc.nextLine();
dos.writeUTF(name);
new Thread(()->{
try{
while(true){
String msg=dis.readUTF();
System.out.println(msg);
}
}catch (IOException e){
System.out.println("Disconnected from server.");
System.exit(0);
}
}).start();
while(true){
String input = sc.nextLine();
dos.writeUTF(input);
if("exit".equalsIgnoreCase(input)){
break;
}
}
dos.close();
dis.close();
socket.close();
sc.close();
}
}
