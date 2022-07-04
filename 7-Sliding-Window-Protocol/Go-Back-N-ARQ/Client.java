import java.util.*;
import java.io.*;
import java.net.*;

class SlidingWindow
{
	private final DataOutputStream dout;
	private final int frameCount;
	public final int sequenceCount;
	private int start,end;

	public SlidingWindow(int frameCount,int windowSize,DataOutputStream dout)
	{
		this.frameCount=frameCount;
		this.sequenceCount=windowSize+1;
		this.dout=dout;
		start=0;//first outstanding frame
		end=windowSize;//next frame to send
	}

	public void sendFrames() throws IOException
	{
		if(start==end && start==frameCount)
			return;
		for(int i=start;i<=end-1;i++)
		{
			print("Sending frame no:"+i+"...\n");
			dout.write(i%sequenceCount);
			print("Frame send!\n");
		}
	}

	public void slideWindow(int offset)
	{
		if(end==frameCount)
		{
			start++;
			return;
		}
		start+=Math.min(offset,frameCount-end);
		end+=Math.min(offset,frameCount-end);
	}

	public boolean areAllSend()
	{
		return end==start && start==frameCount;
	}

	//Check if a sequence number is between the range of sequence numbers already sent
	public boolean isInRange(int sequenceNumber)
	{
		for(int i=start;i<=end-1;i++)
			if(sequenceNumber==i%sequenceCount)
				return true;
		return false;
	}

	public int getOffsetFromFirstOutStandingFrame(int sequenceNumber)
	{
		int offset=1;
		for(int i=start+1;i<=end-1;i++)
			if(i==sequenceNumber)
				break;
			else
				offset++;
		return offset;
	}

	private void print(Object o)
	{
		System.out.print(o);
	}
}

class AckCheckThread extends Thread
{
	private final DataInputStream din;
	private final SlidingWindow senderWindow;
	private final int frameCount;

	public AckCheckThread(DataInputStream din,SlidingWindow senderWindow,int frameCount)
	{
		this.din=din;
		this.senderWindow=senderWindow;
		this.frameCount=frameCount;
	}

	public void run()
	{
		int ack,ackCount=0,expected=1;
		while(ackCount!=frameCount-1)
		{
			try
			{
				ack=din.readInt();
				if(ack==expected)
				{
					expected=(expected+1)%senderWindow.sequenceCount;
					ackCount++;
					senderWindow.slideWindow(1);
					senderWindow.sendFrames();
				}
				else if(senderWindow.isInRange(ack-1<0?senderWindow.sequenceCount-1:ack-1))
				{
					int prevSeqNo=ack-1<0?senderWindow.sequenceCount-1:ack-1;
					int offset=senderWindow.getOffsetFromFirstOutStandingFrame(prevSeqNo)+1;
					ackCount+=offset;
					senderWindow.slideWindow(offset);
					senderWindow.sendFrames();
				}
			}
			catch(IOException e)
			{
				System.out.println("ERROR:Something went wrong");
			}

		}
	}
}

public class Client{

	private static final int timeout=5000;

	private static void print(Object obj)
	{
		System.out.print(obj);
	}

	public static void main() throws Exception
	{
		int windowSize,frameCount;
		Scanner sc=new Scanner(System.in);
		
		print("Enter the window size:");
		windowSize=sc.nextInt();
		print("Enter the number of frames to send:");
		frameCount=sc.nextInt();

		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connection successful!\n");
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		SlidingWindow senderWindow=new SlidingWindow(frameCount, windowSize,dout);
		
		new AckCheckThread(din, senderWindow, frameCount).start();

		while(!senderWindow.areAllSend())
		{
			senderWindow.sendFrames();
			Thread.sleep(timeout);
		}

		sc.close();
		din.close();
		dout.close();
		sock.close();
	}
}