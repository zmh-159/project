#!/bin/bash
#判断是否安装mpi
has_install=$(command -v mpicc)
if [ ! "$has_install" ]; then
  echo "mpi未安装！"
  echo "success"
  exit 0
fi
#apt卸载

list=$(apt list --installed | grep mpich)
if [ "$list" ]; then
  apt remove mpich -y
  echo "success"
  exit 0
fi
#yum卸载
list=$(yum list installed | grep mpich)
if [ "$list" ]; then
  yum remove mpich -y
  echo "success"
  exit 0
fi
