import java.util.*;
import java.net.*;
import java.io.*;

public class Server{
	
	private static void print(Object o){
		System.out.print(o);
	}	
	
	private static String getMail(String s)
	{
		StringTokenizer tokenizer=new StringTokenizer(s,"<>");
		tokenizer.nextToken();
		return tokenizer.nextToken();
	}
	
	public static void main(String args[])throws Exception{
		
		ServerSocket serSock=new ServerSocket(5555);
		print("S : 220 hamburger.edu\n");
		
		Socket sock=serSock.accept();
		DataInputStream din=new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		DataOutputStream dout=new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));
		dout.writeUTF("220 hamburger.edu\n");
		dout.flush();
		
		int step=0;
		String mailContent="";
		while(true)
		{
			if(step==0)//HELO
			{
				String name=din.readUTF();
				StringTokenizer tokenizer=new StringTokenizer(name);
				tokenizer.nextToken();
				name=tokenizer.nextToken();
				print("S : Hello "+name+", pleased to meet you.\n");
				dout.writeUTF("250 "+"Hello "+name+", pleased to meet you");
			}
			else if(step==1)//mail from
			{
				String senderMail=din.readUTF();
				senderMail=getMail(senderMail);
				print("S : 250 "+senderMail+"... Sender ok\n");
				dout.writeUTF("250 "+senderMail+"... Sender ok");
			}
			else if(step==2)//rcpt to
			{
				String recieverMail=din.readUTF();
				recieverMail=getMail(recieverMail);
				print("S : 250 "+recieverMail+"... Recipient ok\n");
				dout.writeUTF("250 "+recieverMail+"... Recipient ok");
			}
			else if(step==3)//DATA
			{
				din.readUTF();
				print("S : 354 Enter mail, end with \".\" on a line by itself\n");
				dout.writeUTF("354 Enter mail, end with \".\" on a line by itself");
			}
			else if(step==4)//read message
			{
				String data="";
				do{
					data=din.readUTF();
					mailContent+=data;
				}while(!data.equals("."));
				print("S : 250 Message accepted for delivery\n");
				dout.writeUTF("250 Message accepted for delivery");
				
			}
			else//quit
			{
				din.readUTF();
				print("S : 221 hamburger.edu closing connection\n");
				dout.writeUTF("221 hamburger.edu closing connection");
				break;
			}
			dout.flush();
			step++;
		}
		
	}
	
}
