import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client{

	private static void print(Object o){
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception{
		Scanner sc=new Scanner(System.in);
		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connected to server!\n\n");

		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		String message="",data="";
		while(!message.equals("bye") && !data.equals("bye"))
		{
			print("Client:");
			data=sc.nextLine();
			dout.writeUTF(data);
			dout.flush();
			message=din.readUTF();
			print("Server:"+message+"\n");
		}

		sc.close();
		din.close();
		dout.close();
		sock.close();
		print("Connection terminated!\n");
	}
}