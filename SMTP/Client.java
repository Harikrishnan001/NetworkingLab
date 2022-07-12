import java.util.*;
import java.io.*;
import java.net.*;

public class Client{
	private static void print(Object o){
		System.out.print(o);
	}

	public static void main(String args[]) throws Exception{
		Scanner sc=new Scanner(System.in);
	
		print("Connecting to server...\n");
		Socket sock=new Socket("localhost",5555);
		print("Connection successful!\n");
		
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream())); 
	
		String data;
		int step=0;
		while(true){
			String replyCode="",replyData="";
			data=din.readUTF();
			
			StringTokenizer tokenizer=new StringTokenizer(data);
			replyCode=tokenizer.nextToken();
			while(tokenizer.hasMoreTokens())
				replyData+=tokenizer.nextToken()+" ";
			
			if(replyCode.equals("220"))
			{
				print("C : ");
				data=sc.nextLine();
				dout.writeUTF(data);
				dout.flush();
			}
			else if(replyCode.equals("250"))
			{
				if(step==0)
				{
					print("C : MAIL FROM :");
					data=sc.nextLine();
					dout.writeUTF("MAIL FROM: "+data);
					dout.flush();
				}
				else if(step==1)
				{
					print("C : RCPT TO :");
					data=sc.nextLine();
					dout.writeUTF("RCPT TO: "+data);
					dout.flush();
				}
				else if(step==2)//DATA
				{
					print("C : ");
					data=sc.nextLine();
					dout.writeUTF(data);
					dout.flush();
				}
				else if(step==3)
				{
					print("C : ");
					data=sc.nextLine();
					dout.writeUTF("QUIT");
				}
				step++;
			}
			else if(replyCode.equals("221")||replyCode.equals("421"))
			{
				break;
			}
			else if(replyCode.equals("354"))
			{
				data="";
				while(!data.equals("."))
				{
					print("C : ");
					data=sc.nextLine();
					dout.writeUTF(data);
					dout.flush();
				}
			}
			
		}
		
		sc.close();
		din.close();
		dout.close();
		sock.close();
		return ;
	}
}
