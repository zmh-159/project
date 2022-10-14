package org.parallel.client.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CollectionInfo implements Runnable {
    /** 小采集间隔 */
    private int smallInterval = 5000;
    /** 大采集间隔 */
    private int bigInterval = 60000;
    /** 采样模式 */
    private int mode = 0;

    public void updateInterval(int smallInterval,int bigInterval,int mode){

    }


    @Override
    public void run() {

    }
}
