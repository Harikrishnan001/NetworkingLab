import java.net.*;
import java.util.*;
import java.io.*;

public class Client{
	
	private static void print(Object o)
	{
		System.out.print(o);
	}
	
	public static void main(String args[])throws Exception
	{
		Socket sock=new Socket("127.0.0.1",5555);
		print("Client connected!\n");
		
		Scanner sc=new Scanner(System.in);
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		
		print("Enter n:");
		int maxFrames=sc.nextInt();
		print("Enter the number of frames to send:");
		int frames=sc.nextInt();
		int curFrame=-1,ack;
		boolean isError=false;

		for(int i=0;i<frames;i++)
		{
			print("\nEnter the frame no:");
			curFrame=sc.nextInt();
			print("Sending frame no "+curFrame+" ...\n");
			dout.writeInt(curFrame);
			dout.flush();
			print("Frame sent!\n");
			print("Waiting for acknowledgement...\n");
			ack=din.readInt();
			print("Acknowledment recieved("+ack+")!\n");
			if(ack!=(curFrame+1)%maxFrames)
			{
				isError=true;
				i--;
				print("ACK_ERROR:Try to resend the frame\n");
			}
			else
			{
				isError=false;
			}

		}

		print("Aborting connection...\n");
		dout.writeInt(-1);
		dout.flush();
		sc.close();
		dout.close();
		sock.close();
		print("Connection aborted!\n");
	}
}