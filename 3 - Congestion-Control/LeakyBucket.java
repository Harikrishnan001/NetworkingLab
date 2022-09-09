import java.util.*;

public class LeakyBucket {
    public static void main(String[] args) throws InterruptedException {
        int n,count,bucketSize,incoming,outgoing;
        int store=0;
        Scanner sc=new Scanner(System.in);

        System.out.print("Enter the bucket size,number of packets and outgoing rate:");
        bucketSize=sc.nextInt();
        count=sc.nextInt();
        outgoing=sc.nextInt();

        while(count!=0)
        {
            System.out.print("\nEnter incoming packet size:");
            incoming=sc.nextInt();

            if(incoming<=(bucketSize-store))
            {
                store+=incoming;
            }
            else
            {
                System.out.println("Packet loss:"+(bucketSize-store));
                System.out.println("Buffer is full!");
                store=bucketSize;
            }
            
            System.out.println("Status:"+store+"/"+bucketSize);
            store-=outgoing;
            System.out.println("Leaking packets...\nStatus:"+store+"/"+bucketSize);
            count--;
        }

    }
}