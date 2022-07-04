import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
    private static void print(Object o)
    {
        System.out.print(o);
    }

    public static void main(String args[]) throws Exception
    {
        int n,m,ack=0,recieved=0,option;
        String frame,frameNo,data="";
        Scanner sc=new Scanner(System.in);
        
        print("Starting server...\n");
        ServerSocket serSock=new ServerSocket(5555);
        print("Server started!\n");
        print("Waiting for client...\n");
        Socket sock=serSock.accept();
        print("Client connected!\n");

        DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

        print("Enter the count of sequence numbers:");
        m=sc.nextInt();
        print("Enter the number of frames to recieve:");
        n=sc.nextInt();

        while(recieved<n)
        {
            data="";
            print("\nWaiting for frames...\n");
            frame=din.readUTF();
            StringTokenizer tokenizer=new StringTokenizer(frame);
            frameNo= tokenizer.nextToken();
            while(tokenizer.hasMoreTokens())
                data+=tokenizer.nextToken()+" ";
            print("Frame no "+frameNo+" recieved!\n");
            print("Discard/Acknowledge?(0/1):");
            option=sc.nextInt();
            if(option==1)
            {
                if(Integer.parseInt(frameNo)!=ack)
                {
                    print("Trying to accept invalid frame no...\n");
                    print("Recieved frame no:"+frameNo+"\n");
                    print("Required frame no:"+ack+"\n");
                    print("Frame discarded and no ack send!\n");
                    continue;
                }
                print("Sending acknowledgement...\n");
                recieved++;
                dout.writeInt(ack);
                dout.flush();
                ack=(ack+1)%m;
                print("Acknowledgement send!\n");
            }
            else
            {
                print("Frame discarded.\n");
            }
        }

        sc.close();
        din.close();
        dout.close();
        sock.close();
        serSock.close();
    }
}
