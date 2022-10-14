#### 主要特性
####编译运行####
第一步：编译go文件：go build -buildmode=c-archive -o http.so http.go
编译成功会产生一个http.so和http.h文件供c程序使用
第二步：编译c文件：gcc nlscpu.c http.so -o nlscpu -lpthread

