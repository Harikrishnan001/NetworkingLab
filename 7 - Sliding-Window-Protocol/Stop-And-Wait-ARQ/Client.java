import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

	private static void print(Object o) {
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception {
		int n;
		Scanner sc=new Scanner(System.in);
		Socket sock=new Socket("localhost",5555);
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		print("Enter the number of packets to send:");
		n=sc.nextInt();
		for(int i=0;i<n;i++)
		{
			print("\nSending packet "+i);
			dout.writeInt(i);
			dout.flush();
			Thread.sleep(2000);
			print(".\n");
			Thread.sleep(2000);
			din.readInt();
			print("Acknowledgement recieved.\n");
		}

		din.close();
		dout.close();
		sock.close();
		sc.close();
	}
}