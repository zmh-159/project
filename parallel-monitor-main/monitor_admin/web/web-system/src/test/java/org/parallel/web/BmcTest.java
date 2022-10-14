package org.parallel.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parallel.web.modules.bmc.kp.MainBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BmcTest {

    @Autowired
    MainBoard mainboard;

    @Test
    public void test(){
        System.out.println(mainboard.getMainBoard(1L));
    }
}
