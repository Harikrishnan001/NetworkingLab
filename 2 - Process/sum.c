#include<stdio.h>
#include<stdlib.h>

int main(int argc,const char* argv[])
{
    int n=atoi(argv[1]);
    printf("Sum=%d\n",n*(n-1)/2);
}