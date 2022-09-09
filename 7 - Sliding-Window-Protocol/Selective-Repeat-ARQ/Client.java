import java.net.*;
import java.io.*;
import java.util.*;

class Client {
    public static void main(String a[]) throws Exception {
        Socket s = new Socket("localhost", 3333);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        Scanner sc = new Scanner(System.in);

        int i = 0, rptr = -1, nf, rws = 8;
        String rbuf[] = new String[8];
        String ch, ack;
        System.out.println();

        do {
            System.out.print("\nEnter the no of frames: ");
            nf = sc.nextInt();
            System.out.println();
            if (nf <= rws - 1) {
                // dout.writeUTF(Integer.toString(nf))
                for (i = 1; i <= nf; i++) {
                    rptr = (rptr + 1) % 8;

                    rbuf[rptr] = din.readUTF();
                    System.out.println("Recieved Frame" + rptr + " : " + rbuf[rptr]);
                }
                rws -= nf;
                System.out.println("\nAcknowledgment sent\n");
                ack = Integer.toString(rptr + 1);
                dout.writeUTF(ack);
                dout.flush();
                System.out.print(rptr + 1);
                rws += nf;
            } else
                break;
            System.out.println("\nDo you want to continue (Yes/No): ");
            ch = sc.next();
        } while (ch.equals("Yes"));
    }
}
