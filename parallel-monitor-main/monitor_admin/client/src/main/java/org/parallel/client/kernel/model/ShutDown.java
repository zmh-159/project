package org.parallel.client.kernel.model;

import lombok.Data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Data
public class ShutDown {
    public static void Shutdown() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("halt -p");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {

        }
    }
}
