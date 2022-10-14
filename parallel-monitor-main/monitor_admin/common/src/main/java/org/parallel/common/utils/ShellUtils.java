package org.parallel.common.utils;

import lombok.Data;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author yuyifade
 * @description
 * @date 2021/3/18 下午6:54
 */
@Data
public class ShellUtils {
    private static final Runtime runtime = Runtime.getRuntime();

    @SneakyThrows
    public static BufferedReader execShell(String[] shell) {
        Process p = runtime.exec(shell);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return reader;
    }

    @SneakyThrows
    public static BufferedReader execShell(String shell) {
        Process p = runtime.exec(shell);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        return reader;
    }

    @SneakyThrows
    public static BufferedReader execShellError(String shell) {
        Process p = runtime.exec(shell);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        return reader;
    }

    @SneakyThrows
    public static BufferedReader execShellError(String[] shell) {
        Process p = runtime.exec(shell);
        p.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        return reader;
    }
}