import java.net.*;
import java.util.*;
import java.io.*;

class WindowCheck extends TimerTask{
	
	final Vector<Frame> v;
	final Vector<Integer> pointers;
	final int checkIndex; 
	
	public WindowCheck(Vector<Frame> v,Vector<Integer> pointers,int checkIndex)
	{
		this.v=v;
		this.checkIndex=checkIndex;
		this.pointers=pointers;
	}
	
	public void run() {
		if(!v.elementAt(checkIndex).isAck)
			pointers.set(1, pointers.elementAt(0));		
	}
}

class AckCheckThread extends Thread{
	
	final DataInputStream din;
	final Vector<Integer> pointers;
	final Vector<Frame> v;
	
	public AckCheckThread(DataInputStream din,Vector<Frame> v,Vector<Integer> pointers)
	{
		this.din=din;
		this.v=v;
		this.pointers=pointers;
	}
	
	public void run()
	{
		int ack;
		while(pointers.elementAt(0)!=v.size())
		{
			try {
			ack=din.readInt();//here ack indicates the frame number that was accepted by server
			for(int i=pointers.elementAt(0);i<pointers.elementAt(1);i++)
				if(ack==v.elementAt(i).number)
				{
					v.elementAt(i).isAck=true;
					System.out.println("Acknowledgement "+ack+" recieved!\n");
					for(int j=i+1;j<pointers.elementAt(1);j++)
						if(!v.elementAt(j).isAck)
						{
							System.out.println("First outgoing frame at index:"+j);
							System.out.println("Next frame to send at index:"+pointers.elementAt(1));
							pointers.set(0, j);
							break;
						}
					break;
				}
			}catch(IOException e)
			{
				System.out.println("ERROR:Something went wrong!\n");
			}
		}
	}
}

public class Client{
	
	final static long timeout=10000;//timeout for resending n frames

	private static void print(Object o)
	{
		System.out.print(o);
	}
	
	public static void main(String args[])throws Exception
	{
		int m,n;
		Vector<Integer> pointers=new Vector<Integer>();
		pointers.add(0);//index of first outstanding frame
		pointers.add(0);//index of next frame to send
		Vector<Frame> v=new Vector<>();
		String content;//used to fetch data of each frame
		Frame current_frame;
		Scanner sc=new Scanner(System.in);
		Timer timer=new Timer();
		
		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connected to server!\n");
		
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		
		print("Enter the count of sequence numbers:");
		m=sc.nextInt();
		print("Enter the number of frames to send:");
		n=sc.nextInt();

		print("\n");
		for(int i=0;i<n;i++)
		{
			print("Enter the content of frame no "+i+" : ");
			content=sc.next();
			v.add(new Frame(i%m,content));
		}

		new AckCheckThread(din, v, pointers).start();
		
		print("\n");
		while(pointers.elementAt(0)!=n)
		{
			if(pointers.elementAt(1)-pointers.elementAt(0)+1<=m-1)
			{
				current_frame=v.elementAt(pointers.elementAt(1));
				print("Sending frame no "+pointers.elementAt(1)+"...\n");
				dout.writeUTF(current_frame.number+" "+current_frame.data);
				dout.flush();
				timer.schedule(new WindowCheck(v,pointers,pointers.elementAt(1)), timeout);
				pointers.set(1,Math.min( pointers.elementAt(1)+1,n-1));
				print("Next frame to send at index:"+pointers.elementAt(1)+"\n");
				print("Frame send!\n");
			}
		}

		sc.close();
		din.close();
		dout.close();
		sock.close();
	}
}