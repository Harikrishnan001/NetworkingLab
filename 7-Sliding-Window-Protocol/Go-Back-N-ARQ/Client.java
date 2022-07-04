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
	final int end;
	
	public AckCheckThread(DataInputStream din,Vector<Frame> v,Vector<Integer> pointers,int end)
	{
		this.din=din;
		this.v=v;
		this.pointers=pointers;
		this.end=end;
	}
	
	public void run()
	{
		int ack;
		while(pointers.elementAt(0)!=end)
		{
			try {
			ack=din.readInt();
			for(int i=pointers.elementAt(0);i<pointers.elementAt(1);i++)
				if(ack==v.elementAt(i).number)
				{
					v.elementAt(i).isAck=true;
					System.out.println("Acknowledgement "+ack+" recieved!");
				}
			}catch(IOException e)
			{
				System.out.println("ERROR:Something went wrong!");
			}
		}
	}
}

public class Client{
	
	private static void print(Object o)
	{
		System.out.print(o);
	}
	
	public static void main(String args[])throws Exception
	{
		int m,n,send_count=0;
		Vector<Integer> pointers=new Vector<Integer>();
		pointers.add(0);
		pointers.add(0);
		Vector<Frame> v=new Vector<>();
		String content;
		Frame current_frame;
		Scanner sc=new Scanner(System.in);
		Timer timer=new Timer();
		
		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connected to server!\n");
		
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		
		print("Enter the window size:");
		m=sc.nextInt();
		print("Enter the number of frames to send:");
		n=sc.nextInt();
		
		for(int i=0;i<n;i++)
		{
			print("Enter the content of frame no "+i+" : ");
			content=sc.nextLine();
			v.add(new Frame(i,content));
		}
		
		

		while(send_count<n)
		{
			if(pointers.elementAt(1)-pointers.elementAt(0)+1<m-1)
			{
				current_frame=v.elementAt(pointers.elementAt(1));
				print("Sending frame no "+pointers.elementAt(1)+"...\n");
				dout.writeUTF(current_frame.number+" "+current_frame.data);
				dout.flush();
				timer.schedule(new WindowCheck(v,pointers,pointers.elementAt(1)), 2000);
				pointers.set(1, pointers.elementAt(1)+1);
				print("Frame send!");
			}
		}
	}
}