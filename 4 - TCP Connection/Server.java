import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server 
{

    private static void print(Object o)
    {
        System.out.print(o);
    }
    public static void main(String args[])  throws IOException  
    {
        ServerSocket serSock;
        Socket sock;
        DataInputStream din;
        String line="";
        Scanner sc=new Scanner(System.in);

        print("Starting server...\n");
        serSock=new ServerSocket(5555);
        print("Server started!\n\n");
        print("Waiting for client...\n");
        sock=serSock.accept();
        print("Client connected!\n\n");

        din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));

        while(!line.equals("bye"))//terminate server when bye is recieved
        {
            print("Waiting for message...\n");
            line=din.readUTF();
            print("Message from client:"+line+"\n\n");
        }
        
        din.close();
        sc.close();
        sock.close();
        serSock.close();
        print("Connection aborted!\n");
    }
}
