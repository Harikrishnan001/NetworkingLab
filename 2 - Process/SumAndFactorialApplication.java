/*
    Note:
    Commands to run before executing this file:
    gcc sum.c -o sum
    gcc fact.c -o fact
*/

import java.io.*;
import java.util.*;

public class SumAndFactorialApplication {
    public static void main(String args[]) throws IOException, InterruptedException{
        
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter a number:");
        int n=sc.nextInt();

        //Process for calculating the sum
        ProcessBuilder pb1=new ProcessBuilder("D:\\Study\\NSSCE\\S6\\NetworkLab\\2 - Process\\sum.exe",String.valueOf(n));
        pb1.inheritIO();
        try{
            Process p=pb1.start();
            p.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }

        //Process for calculating the factorial
        ProcessBuilder pb2=new ProcessBuilder("D:\\Study\\NSSCE\\S6\\NetworkLab\\2 - Process\\fact.exe",String.valueOf(n));
        pb2.inheritIO();
        try{
            Process p=pb2.start();
            p.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        System.out.println("Done");
        sc.close();

    }
}
