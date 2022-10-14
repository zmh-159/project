#include "http.h" //引入go代码导出的生成的C头文件
#include <stdio.h>
#include <string.h>
int main(int argc, char **argv)
{
    char cvalue[4096];
    memset(cvalue, 0, 4096);
    if (argc > 1)
    {
        strcat(cvalue, "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=memoryStatic+");
        strcat(cvalue, argv[1]);
    }
    else
    {
        strcat(cvalue, "http://192.168.31.183:8001/api/cmdRun/sendCmd?cmd=memoryStatic");
    }
    
    int length = strlen(cvalue);
    GoString value = {cvalue, length}; //go中的字符串类型在c中为GoString
    go_http(value);

    return 0;
}