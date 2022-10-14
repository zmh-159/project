#!/bin/bash
apt install htop -y
success=$(apt list --installed 2> /dev/null | grep htop)
if [ $? -eq 0 ]; then
    echo "success"
else
    echo "failed"
fi
