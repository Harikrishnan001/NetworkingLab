#include<stdio.h>
#include<stdlib.h>

int main(int argc,const char* argv[])
{
    int n=atoi(argv[1]);
    int f=1;
    for(int i=2;i<=n;i++)
        f*=i;
    printf("factorial=%d\n",f);
}