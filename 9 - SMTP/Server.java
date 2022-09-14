import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static void print(Object o) {
        System.out.print(o);
    }

    private static void send(int code,String message,DataOutputStream dout) throws IOException{
        print("\nS: "+code+" "+message+"\n");
        dout.writeInt(code);
        dout.writeUTF(message);
        dout.flush();
    }

    private static String extractMail(String message){
        StringTokenizer tokenizer=new StringTokenizer(message,"<>");
        tokenizer.nextToken();
        return tokenizer.nextToken();
    }

    public static void main(String args[]) throws Exception {
        ServerSocket serverSocket=new ServerSocket(5555);
        Socket socket=serverSocket.accept();
        DataInputStream din=new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream dout=new DataOutputStream(socket.getOutputStream());

        send(220,"hamburger.edu",dout);

        String message="",sender="",reciever="",client="",mail="";
        while(true){
            message=din.readUTF();
            print("C: "+message);
            if(message.startsWith("HELO")){
                client=message.substring(5);
                send(250,"Hello "+client+". Pleased to meet to you",dout);
            }else if(message.startsWith("MAIL")){
                sender=extractMail(message);
                send(250,sender+"... Sender ok",dout);
            }else if(message.startsWith("RCPT")){
                reciever=extractMail(message);
                send(250,reciever+"... Recipient ok",dout);
            }else if(message.startsWith("DATA")){
                send(354,"Enter mail and end with \".\" on a line by itself",dout);
                do{
                    message=din.readUTF();
                    print("C: "+message+"\n");
                    mail+=message;
                }while(!message.equals("."));
                send(250,"Message accepted for delivery",dout);
            }else if(message.equals("QUIT")){
                send(221,"hamburger.edu closing connection",dout);
                break;
            }
        }

        din.close();
        dout.close();
        socket.close();
        serverSocket.close();
    }
}
