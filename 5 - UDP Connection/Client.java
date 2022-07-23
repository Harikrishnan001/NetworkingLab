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
        DatagramSocket sock;
        String line="";
        InetAddress localAddress=InetAddress.getLocalHost();
        Scanner sc=new Scanner(System.in);
        
        print("Creating datagram socket...\n");
        sock=new DatagramSocket();
        print("Socket created!\n\n");

        while(!line.equals("bye"))
        {
            print("Message to server:");
            line=sc.nextLine();
            print("Sending message...\n");
            sock.send(new DatagramPacket(line.getBytes(),line.getBytes().length,localAddress,6666));
            print("Message sent!\n");
        }

        sc.close();
        sock.close();
        print("Connection aborted!\n");
    }
}
