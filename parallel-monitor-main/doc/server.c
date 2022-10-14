/*
============================================================================
Name : server.c
Author : king
Version :
Copyright : Your copyright notice
Description : Hello World in C, Ansi-style
============================================================================
*/
#include <stdlib.h>
#include <pthread.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h> /* inet(3) functions */

#include <stdlib.h>
#include <errno.h>
#include <stdio.h>
#include <string.h>
 
int handle(int point);
 
int main(void) {
int sfd, ind;
struct sockaddr_in addr;
struct sockaddr_in clent;
char resv[1024], sendbuf[1024];
char buf[1024];
char * myaddr = "127.0.0.1";
 
int ret; // 返回值设置
socklen_t lent;
int pid;
addr.sin_family = AF_INET; //IPv4 Internet protocols
 
addr.sin_port = htons(5051); //这里输入服务器端口号
 
addr.sin_addr.s_addr = inet_addr(myaddr);
//INADDR_ANY表示本机IP
 
//獲取socket描述符，IPV4asd
printf("socket start \n");
sfd = socket(AF_INET, SOCK_STREAM, 0);
 
if (sfd < 0) {
	printf("socket error \n");
	return -1;
}
printf("bind start \n");
//将套接子与指定端口链接
if (bind(sfd, (struct sockaddr *) &addr, sizeof(struct sockaddr)) < 0) {
	printf("bind error \n");
	return -1;
}
 
//监听套接子
printf("listen start \n");
	if (listen(sfd, 1024) < 0) {
		printf("listen error \n");
	return -1;
} 
for (;;) {
//接受来自客户端的信息
	printf("accept start \n");
	memset(&clent, 0, sizeof(clent));
	lent = sizeof(clent);
	ind = accept(sfd, (struct sockaddr *) &clent, &lent);
	if (ind < 0) {
		printf("accept error %d \n", ind);
	return -1;
	}
 
	printf("infor \n");
	printf("clent addr%s porit %d\n",
		inet_ntop(AF_INET, &clent.sin_addr, buf, sizeof(buf)),
		ntohs(clent.sin_port));
 
	pid = fork();
 
	if (pid == 0) {
	//子进程
		close(sfd);
		handle(ind);
	} else if (pid < 0) {
	//error
		close(ind);
	} else {
	//父进程
	}
}
 
return EXIT_SUCCESS;
 
}
 
int handle(int point) {
	int retn;
	int max_size = 16;
	char * buf = NULL;
	buf = (char *)malloc(max_size+1);
	memset(buf, '\0', max_size);
	int init;
	/*
 	int start,end;
	start = time(NULL);*/
	for (init = 8;init<=15;init++) {
		int i;
		for(i=0;i<1000000;i++){
		retn = read(point, buf, init);
		}
		if (retn < 0) {
			printf("read error \n");
			close(point);
		break;
		} else if (retn == 0) {
			printf("client exit \n");
			close(point);
		break;
		}
		if (strcmp("exit", buf) == 0) {
			printf("exit \n");
			close(point);
		return 0;
	 	}
	}
	time_t  now;
	time(&now);
	printf("总共花费%lds\n",now);
	return 0;
}
