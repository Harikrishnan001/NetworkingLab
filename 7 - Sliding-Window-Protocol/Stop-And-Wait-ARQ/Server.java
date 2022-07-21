import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	
	private static void print(Object o)
	{
		System.out.print(o);
	}
	
	public static void main(String args[])throws Exception
	{
		Scanner sc=new Scanner(System.in);
		print("Enter n:");
		int maxFrames=sc.nextInt();

		ServerSocket serSock=new ServerSocket(5555);
		print("Server started.\n");
		print("Waiting for client....\n");
		
		Socket sock=serSock.accept();
		print("Client accepted!\n");
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new DataOutputStream(sock.getOutputStream()));

		int expecting=0,arrived;
		while(true)
		{
			print("\nWaiting for frames...\n");
			arrived=din.readInt();
			print("Arrived frame no:"+arrived+"\n");
			if(arrived==expecting)
			{
				expecting=(arrived+1)%maxFrames;
				print("Frame "+arrived+" recieved successfuly!\n");
				print("Sending ack("+expecting+")...\n");
				dout.writeInt(expecting);
				dout.flush();
				print("ACK_SENT\n");
			}
			else
			{
				if(arrived==-1)
					break;
				print("ERROR:Unexpected frame arrived!\n");
				dout.writeInt(expecting);
				dout.flush();
			}
		}
		
		print("Shutting down server...\n");
		din.close();
		sock.close();
		serSock.close();
		print("Server down!\n");
	}
}