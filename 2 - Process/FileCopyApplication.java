import java.io.*;

public class FileCopyApplication {
    public static void main(String args[]) throws Exception
    {
        //Reading the file
        FileInputStream fin=new FileInputStream("D:\\myfile.txt");
        //Determining file size in bytes
        int size=fin.available();
        byte buffer[]=new byte[size];
        fin.read(buffer);
        fin.close();

        //Writing to file
        FileOutputStream fout=new FileOutputStream("D:\\output.txt");
        fout.write(buffer);
        fout.close();
    }

}
