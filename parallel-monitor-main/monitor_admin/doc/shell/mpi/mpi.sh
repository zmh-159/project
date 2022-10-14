#!/bin/bash
#判断是否安装mpi
has_install=$(command -v mpicc)
if [ "$has_install" ]; then
  echo "mpi已安装！"
  exit 0
fi
#apt安装
installer=$(command -v apt)
if [ "$installer" ]; then #apt安装
  apt install mpich -y
  success=$(command -v mpicc)
  if [ $success ]; then
    echo "mpi安装成功！"
    echo "success"
  else
    echo "mpi安装失败！"
  fi
fi

installer=$(command -v yum)
if [ "$installer" ]; then #apt安装
  yum install mpich -y
  success=$(command -v mpicc)
  if [ $success ]; then
    echo "mpi安装成功！"
    echo "success"
  else
    echo "mpi安装失败！"
  fi
fi