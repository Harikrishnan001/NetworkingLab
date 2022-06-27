import java.io.*;
import java.util.*;
import java.net.*;

public class Client{
	private static void print(Object o){
		System.out.print(o);
	}

	public static void main(String args[])throws Exception{
		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connection successful!\n\n");
		
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
	
		Scanner sc=new Scanner(System.in);
		print("Enter the filename:");
		String filename=sc.nextLine();
		
		print("Requesting to server...\n");
		dout.writeUTF(filename);
		dout.flush();
		print("Request sent!\n");
		print("Waiting for reply...\n");
		String reply=din.readUTF();
		if(!reply.equals("success"))
		{
			print("ERROR:File not found!\n");
			print("Aborting...\n");
			din.close();
			dout.close();
			sock.close();
			print("Client aborted.\n");
			return ;
		}
		
		print("Enter the file to write to:");
		filename=sc.nextLine();
		FileOutputStream fout=new FileOutputStream(filename);
		int c;
		print("Recieving and writing file...\n");
		while((c=din.read())!=-1)
		{
			fout.write(c);
		}
		print("Operation successful!\n");
		
		print("Closing connection...\n");
		fout.close();
		dout.close();
		din.close();
		sock.close();
		print("Client finished.\n");
		
	}
}