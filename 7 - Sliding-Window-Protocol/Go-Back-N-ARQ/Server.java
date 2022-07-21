import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

	public static void print(Object o) {
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		int frameCount, seqCount, recievedCount = 0, ack = 0, recieved = 0;
		boolean ackNotSend = false;

		print("Starting server...\n");
		ServerSocket serSock = new ServerSocket(5555);
		print("Server started!\n\nWaiting for client...\n");
		Socket sock = serSock.accept();
		print("Client connected!\n\n");
		DataInputStream din = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		print("Enter the number of frames to recieve:");
		frameCount = sc.nextInt();
		print("Enter the sequence number count:");
		seqCount = sc.nextInt();

		while (recievedCount < frameCount) {
			recieved = din.readInt();
			print("\nFrame no:" + recieved + " arrived\n");
			if (ack == recieved) {
				print("Keep/Discard?(1/0):");
				if (sc.nextInt() == 1) {
					if (!ackNotSend)
						ackNotSend = true;
					ack = (ack + 1) % seqCount;
					recievedCount++;
					print("Recieved frame no:" + recieved + "\n");
				} else {
					if (ackNotSend) {
						ackNotSend = false;
						print("\nAck(" + ack + ") sending...\n");
						dout.writeInt(ack);
						dout.flush();
						print("Ack send!\n\n");
					}
					print("Dropped frame no:" + recieved + "\n");
				}
			}
			else {
				if (ackNotSend) {
					ackNotSend = false;
					print("\nAck(" + ack + ") sending...\n");
					dout.writeInt(ack);
					dout.flush();
					print("Ack send!\n\n");
				}
				print("Dropped frame no:" + recieved + "\n");
			}
		}
		if (ackNotSend) {
			print("\nAck(" + ack + ") sending...\n");
			dout.writeInt(ack);
			dout.flush();
			print("Ack send!\n\n");
		}

		print("All frames recieved and acknowlegements sent!\n");

		sc.close();
		dout.close();
		din.close();
		sock.close();
		serSock.close();
	}
}
