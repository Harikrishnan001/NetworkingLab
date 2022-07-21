import java.io.*;
import java.net.*;
import java.util.*;

class TimeOutEvent extends TimerTask {
	private final Vector<Integer> buffer;
	private final DataOutputStream dout;
	private int frameNo;

	public TimeOutEvent(Vector<Integer> buffer, DataOutputStream dout, int frameNo) {
		this.buffer = buffer;
		this.dout = dout;
		this.frameNo = frameNo;
	}

	public void run() {
		System.out.println("\nTimer of frameNo:" + frameNo+"");
		System.out.print("Resending frame nos:");
		for (int i = 0; i < buffer.size(); i++)
			System.out.print(buffer.elementAt(i) + " ");
		System.out.println();

		for (int i = 0; i < buffer.size(); i++) {
			try {
				dout.writeInt(buffer.elementAt(i));
				dout.flush();
			} catch (IOException e) {
				System.out.print("ERROR:Something went wrong!\n");
			}
		}
		System.out.println("Frames resend!\n");
	}
}

class AcknowledgementCheckerThread extends Thread {
	private final DataInputStream din;
	private final Vector<Integer> buffer;
	private final Vector<Timer> timers;
	private final int seqCount;
	private final int frameCount;
	public int ackCount = 0;

	public AcknowledgementCheckerThread(DataInputStream din, Vector<Integer> buffer, Vector<Timer> timers, int seqCount,
			int frameCount) {
		this.din = din;
		this.buffer = buffer;
		this.timers = timers;
		this.seqCount = seqCount;
		this.frameCount = frameCount;
	}

	public void run() {
		while (ackCount < frameCount) {
			try {
				int ack = din.readInt();
				System.out.println("\nAcknowlegement no:" + ack + " recieved!");
				ack = ack > 0 ? ack - 1 : seqCount - 1;
				for (int i = 0; i < buffer.size(); i++)
					if (buffer.elementAt(i) == ack) {
						while (buffer.elementAt(0) != ack) {
							buffer.removeElementAt(0);
							timers.elementAt(0).cancel();
							timers.removeElementAt(0);
							ackCount++;
						}
						buffer.removeElementAt(0);
						timers.elementAt(0).cancel();
						timers.removeElementAt(0);
						ackCount++;
						break;
					}
			} catch (IOException e) {
				System.out.println("Something went wrong here!");
			}
		}
	}
}

public class Client {
	private static long timeOutDelay = 5000;

	public static void print(Object o) {
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		int frameCount, seqCount, sendCount = 0, next = 0;
		Vector<Integer> buffer = new Vector<Integer>();
		Vector<Timer> timers = new Vector<Timer>();

		print("Connecting to server...\n");
		Socket sock = new Socket("localhost", 5555);
		print("Connected to server!\n\n");
		DataInputStream din = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

		print("Enter the number of frames to send:");
		frameCount = sc.nextInt();
		print("Enter the count of sequence numbers:");
		seqCount = sc.nextInt();

		AcknowledgementCheckerThread tAckChecker = new AcknowledgementCheckerThread(din, buffer, timers, seqCount,
				frameCount);
		tAckChecker.start();

		print("\n");
		while (sendCount < frameCount) {
			if (buffer.size() < seqCount - 1 && sendCount < frameCount) {
				print("Sending frame no:" + next + " ...\n");
				dout.writeInt(next);
				dout.flush();
				buffer.add(next);
				timers.add(new Timer());
				timers.elementAt(buffer.size() - 1).scheduleAtFixedRate(new TimeOutEvent(buffer, dout, next),
						timeOutDelay, timeOutDelay);
				print("Frame no:" + next + " send!\n\n");
				next = (next + 1) % seqCount;
				sendCount++;
				Thread.sleep(1000);
			}
		}

		print("All frames sent!\nWaiting for last ack...\n");
		while (tAckChecker.ackCount != frameCount)
			System.out.flush();

		sc.close();
		dout.close();
		din.close();
		sock.close();
	}
}