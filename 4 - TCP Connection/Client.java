import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client 
{
    private static void print(Object o)
    {
        System.out.print(o);
    }    

    public static void main(String args[]) throws IOException
    {
        Socket sock;
        DataOutputStream dout;
        String line="";
        Scanner sc=new Scanner(System.in);

        print("Connecting to server...\n");
        sock=new Socket("localhost",5555);
        print("Connected to server!\n\n");

        dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

        while(!line.equals("bye"))//send bye to terminate client
        {
            print("Message to server:");
            line=sc.nextLine();
            print("Sending message...\n");
            dout.writeUTF(line);
            dout.flush();
            print("Message sent!\n\n");
        }
        
        dout.close();
        sc.close();
        sock.close();
        print("Connection aborted!\n");
    }
}
