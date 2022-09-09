import java.net.*;
import java.io.*;
import java.util.*;

class Server {
    public static void main(String args[]) throws Exception {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        Scanner sc = new Scanner(System.in);

        String sbuff[] = new String[8];
        int sptr = 0, sws = 8, nf, i;
        String ch, ano;
        do {
            System.out.print("\n Enter the no. of frames : ");
            nf = sc.nextInt();
            if (nf <= sws - 1) {
                System.out.println("Enter " + nf + " Messages to be send\n");
                for (i = 0; i < nf; i++) {
                    sbuff[sptr] = sc.next();
                    dout.writeUTF(sbuff[sptr]);
                    dout.flush();
                    sptr = (sptr + 1) % 8;
                }
                i = 0;
                sws -= nf;
                ano = din.readUTF();
                System.out.print("Acknowledgement recieved: " + ano);

                System.out.println(" for " + ano + " frames");
                sws += nf;
            } else {
                System.out.println("The no. of frames exceeds window size");
                break;
            }
            System.out.print("\nDo you wants to send some more frames(Yes/No) : ");
            ch = sc.next();
        } while (ch.equals("Yes"));
        s.close();
        din.close();
        ss.close();
    }
}
