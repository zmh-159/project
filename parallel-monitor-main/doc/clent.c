/*
 ============================================================================
 Name    : client.c
 Author   : king
 Version   :
 Copyright  : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */
#include <time.h> 
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>    /* inet(3) functions */
 
int handle(int fd);
 
int main(void) {
 
  int nsd;
  char buf[1024];
 
  char * myaddr = "127.0.0.1";
  struct sockaddr_in addr;
 
  printf("welcome to echo client\n");
  nsd = socket(AF_INET, SOCK_STREAM, 0);
  printf("connect start1 \n");
  //bzero(addr, sizeof(*addr));
  memset(&addr,0,sizeof(addr));
  printf("connect start2 \n");
  addr.sin_family = AF_INET;
  addr.sin_port = htons(5051);
  addr.sin_addr.s_addr=inet_addr(myaddr);
 
  printf("connect start3 \n");
  if (connect(nsd, (struct sockaddr *)&addr, sizeof(struct sockaddr)) < 0) {
    printf("connect error \n");
    return -1;
  }
  printf("handle start\n");
  handle(nsd);
  close(nsd);
  return EXIT_SUCCESS;
}
 
int handle(int fd) {
  int length = 8;
  int n=0;
  char * sendl;
  char * buf;
  int max = 15 ;
  sendl = (char*)malloc(max+1);
  int j = 0;
  while(length<= 15){
	 for(n=0; n< length; n++)//生成随机字符
  	 sendl[n] = rand()%26+'a';
   	 sendl[length]='\0';
	 /*start = clock();
	 double time = (double)start/CLOCKS_PER_SEC;
  	 printf("开始时间: %f\n",time);*/
	 time_t now;
  	time(&now);
  	printf("开始时间: %ld\n",now);
	 for (j=0;j<1000000;j++){ 
    		/*printf("第%d次wirte start\n",j);*/
    		write(fd, sendl, length);
  	}
	length+=1;
	/*end = clock();
	double time = (double)(end-start)/CLOCKS_PER_SEC/4;*/
	free(sendl);
	printf("length=%dB \n",length-1);
 	/*printf("开始时间%fs \n",time);*/

  }
  return 0;
}
