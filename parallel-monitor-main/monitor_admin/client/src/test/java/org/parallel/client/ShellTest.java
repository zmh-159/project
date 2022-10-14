package org.parallel.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.parallel.client.service.ShellTool;
import org.parallel.common.utils.ShellUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShellTest {
    @Autowired
    private ShellTool st;
    @Test
    public void test() throws IOException {
//        File f = new File("htop.sh");
//        FileInputStream fi = new FileInputStream(f);
//        byte b[] = fi.readAllBytes();
//        System.out.println(new String(b,0,b.length));
//        st.execShell(new String(b,0,b.length));
//        st.execShell(new String(b,0,b.length));
        BufferedReader f = ShellUtils.execShell("ls");
        while(true){
            String s = f.readLine();
            if(s == null){
                break;
            }
            System.out.println(s);
        }

    }


}
