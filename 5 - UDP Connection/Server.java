import java.io.*;
import java.net.*;

public class Server 
{
    private static void print(Object o)
    {
        System.out.print(o);
    }
    
    private static String parseData(byte[] byteData)
    {
        String result="";
        int i=0;
        while(byteData[i]!=0)
        {
            result+=(char)byteData[i];
            i++;
        }
        return result;
    }

    public static void main(String args[]) throws IOException
    {
        final int dataLen=16384;//size of buffer
        byte[] byteData=new byte[dataLen];
        DatagramSocket sock;
        DatagramPacket dp=null;
        String line="";

        print("Starting UDP server...\n");
        sock=new DatagramSocket(6666);
        print("Server started!\n\n");
        
        while(!line.equals("bye"))
        {
            print("Waiting for message...\n");
            dp=new DatagramPacket(byteData, dataLen);
            sock.receive(dp);
            line=parseData(dp.getData());
            print("Message from client:"+line+"\n\n");
            byteData=new byte[dataLen];
        }

        sock.close();
        print("Connection aborted!\n");
    }
}
