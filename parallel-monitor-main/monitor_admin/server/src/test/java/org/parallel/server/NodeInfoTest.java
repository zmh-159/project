package org.parallel.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parallel.server.cmd.model.GetNodeInfo;
import org.parallel.server.service.IntervalConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuyifade
 * @description
 * @date 2021/9/11 下午12:57
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NodeInfoTest {

    public NodeInfoTest() {
    }


//    private final NodeInfoCache nic;

    @Autowired
    private IntervalConfig c;
    @Autowired
    private GetNodeInfo cld;

    @Test
    public void test() {

        //cld.dynamicNodeInfo();
    }
}
