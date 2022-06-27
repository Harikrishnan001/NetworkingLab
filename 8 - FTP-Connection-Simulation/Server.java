import java.io.*;
import java.util.*;
import java.net.*;

public class Server{
	private static void print(Object o){
		System.out.print(o);
	}

	public static void main(String args[])throws Exception{
        
		ServerSocket serSock=new ServerSocket(5555);
		print("Server initiated!\n");
		print("Waiting for client...\n");
		Socket sock=serSock.accept();
		print("Connection successful!\n\n");
		
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
	
		print("Waiting for request...\n");
		String filename=din.readUTF();
		print("Request obtained!\n");
		
		File file=new File(filename);
		if(file.exists())
		{
			dout.writeUTF("success");
			dout.flush();
		}
		else
		{
			dout.writeUTF("failed");
			dout.flush();
			print("Aborting server...\n");
			din.close();
			dout.close();
			sock.close();
			serSock.close();
			print("Connection aborted.\n");
			return ;
		}
		
		FileInputStream fin=new FileInputStream(file);
		int c;
		print("Sending file...");
		while((c=fin.read())!=-1)
		{
			dout.write(c);
		}
		dout.write(-1);
		dout.flush();
		print("Operation completed!");
		
		print("Aborting server...\n");
		fin.close();
		din.close();
		dout.close();
		sock.close();
		serSock.close();
		print("Connection aborted.\n");
		
	}
}