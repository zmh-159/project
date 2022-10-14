package org.parallel.client.kernel.model;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class ReBoot {
    public static void Reboot() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("reboot");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {

        }
    }
}
