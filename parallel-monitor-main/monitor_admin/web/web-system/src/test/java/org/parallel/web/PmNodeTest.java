package org.parallel.web;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.parallel.web.modules.bmc.Kp;
import org.parallel.web.modules.bmc.kp.*;
import org.parallel.web.modules.pm.cabinet.PmCabinetQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuyifade
 * @description
 * @date 2021/9/10 下午12:30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PmNodeTest {
    @Autowired
    StorageInfo storageInfo;

    @Autowired
    NetworkInfo networkInfo;


    @Test
    public void test() {
        //networkInfo.getNetWork(2L);
        storageInfo.getStorage(2L);
    }

}
