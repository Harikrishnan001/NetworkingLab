import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server{

	private static void print(Object o){
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception{
		Scanner sc=new Scanner(System.in);
		ServerSocket serSock=new ServerSocket(5555);
		print("Waiting for client to connect...\n");
		Socket sock=serSock.accept();
		print("Client connected!\n\n");

		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		String data="",message="";
		while(!message.equals("bye")  && !data.equals("bye"))
		{
			message=din.readUTF();
			print("Client:"+message+"\n");
			print("Server:");
			data=sc.nextLine();
			dout.writeUTF(data);
			dout.flush();
		}

		sc.close();
		din.close();
		dout.close();
		sock.close();
		serSock.close();
		print("Connection terminated!\n");

	}
}