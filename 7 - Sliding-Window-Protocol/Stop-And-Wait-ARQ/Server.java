import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	private static void print(Object o) {
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception {
		int data;
		Scanner sc = new Scanner(System.in);
		ServerSocket serSock = new ServerSocket(5555);
		Socket sock = serSock.accept();
		DataInputStream din = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		try {
			while (true) {
				data = din.readInt();
				print("\nRecieved packet " + data + ".\n");
				Thread.sleep(2000);
				dout.writeInt(data + 1);
				dout.flush();
				print("Acknowledgement sent.\n");
			}
		} catch (EOFException e) {
		}

		din.close();
		dout.close();
		sock.close();
		serSock.close();
		sc.close();
	}
}