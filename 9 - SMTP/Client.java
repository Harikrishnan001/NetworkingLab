import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private static void print(Object o) {
        System.out.print(o);
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket sock = new Socket("localhost", 5555);
        DataInputStream din = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
        DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

        int code;
        String message, data = "";

        while (!data.equals("QUIT")) {
            code = din.readInt();
            message = din.readUTF();
            print("S: " + code + " " + message + "\n");
            if (code == 354) {
                while (!data.equals(".")) {
                    print("C: ");
                    data = sc.nextLine();
                    dout.writeUTF(data);
                    dout.flush();
                }
                continue;
            }
            print("C: ");
            data = sc.nextLine();
            dout.writeUTF(data);
            dout.flush();
        }

        code = din.readInt();
        message = din.readUTF();
        print("S: " + code + " " + message + "\n");

        sc.close();
        din.close();
        dout.close();
        sock.close();
    }

}
