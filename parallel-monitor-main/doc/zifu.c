#include <stdio.h>
#include <stdlib.h>  /* malloc, free, rand, system */
#include <mpi.h>
int main (int argc, char *argv[])
{
     MPI_Status status;
     // 初始化MPI环境
     MPI_Init (&argc, &argv);
     int length = 8;
     int max = 15;
     char * buffer,* get;
     int myrank;
     int n;
     MPI_Comm_rank (MPI_COMM_WORLD, &myrank);
     /*buffer = (char*)malloc(i+1);  // 字符串最后包含 \0
     get = (char*)malloc(i+1);
     for(n=0; n<i; n++)//生成随机字符
     buffer[n] = rand()%26+'a';
     buffer[i]='\0';*/
     buffer = (char*)malloc(max+1);  // 字符串最后包含 \0
     get = (char*)malloc(max+1);
     double start,end;
     MPI_Barrier(MPI_COMM_WORLD);
     start = MPI_Wtime();
     while(length <= 15){
     	for(n=0; n<length; n++)//生成随机字符
     		buffer[n] = rand()%26+'a';
     	buffer[length]='\0';
        /*double start,end;
        MPI_Barrier(MPI_COMM_WORLD);
        start = MPI_Wtime();*/
        for(int j = 0;j<1000000;j++){
                if(myrank == 0)
                {
                        MPI_Send (buffer, length, MPI_CHAR, 1, 99, MPI_COMM_WORLD);
                }
                else{
                        MPI_Recv (get, length, MPI_CHAR, 0, 99, MPI_COMM_WORLD, &status);
   
                }
        }
       /* MPI_Barrier(MPI_COMM_WORLD);
        end = MPI_Wtime();
        if (myrank == 0) {
                printf("%d\n",length);
                printf("Runtime = %f\n", end-start);
                printf("---------------------------\n");
        }*/
        length+=1;
     }
     MPI_Barrier(MPI_COMM_WORLD);
     end = MPI_Wtime();
     if(myrank == 0){
	     printf("time:%f\n",end-start);
     }
     free(buffer);
     free(get);
     MPI_Finalize ();
     return 0;
 }

