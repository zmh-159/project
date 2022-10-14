package org.parallel.client.kernel.model;


import com.alibaba.fastjson.JSONObject;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public abstract class BaseInfo {
    protected static final SystemInfo si = new SystemInfo();
    protected static final HardwareAbstractionLayer hal = si.getHardware();
    protected static final OperatingSystem os = si.getOperatingSystem();
    protected static final CentralProcessor cpu = hal.getProcessor();

    protected static final float G = 1024 * 1024 * 1024L;
    protected static final float M = 1024 * 1024L;
    protected static final float K = 1024L;

    protected JSONObject staticInfo = new JSONObject();
    protected JSONObject dynamicInfo = new JSONObject();

    public static double strToDouble(String str) {
        double res;
        try {
            res = Double.parseDouble(str);
        } catch (Exception e) {
            res = 0;
        }
        return res;
    }

    public static double twoDecimal(double d) {
        return (double) Math.round(d * 100) / 100;
    }

    public static String getNumeric(String str) {
        str = str.trim();
        StringBuilder str1 = new StringBuilder();
        if (!"".equals(str)) {
            for (int i = 0; i < str.length(); i++) {
                if (str.charAt(i) >= 46 && str.charAt(i) <= 57) {
                    str1.append(str.charAt(i));
                }
            }
        }
        return str1.toString();
    }

    public static List<String> interceptList(int Rows, String matchString, List<String> String) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < String.size(); ++i) {
            String s = String.get(i);
            Pattern pattern1 = Pattern.compile(".*" + matchString + ".*");
            Matcher math = pattern1.matcher(s);
            if (math.find()) {
                res.add(s);
                for (int j = 1; j < Rows && i < String.size(); ++j) {
                    res.add(String.get(i + 1));
                    i++;
                }
            }
        }
        return res;
    }

    public static String interceptString(String matchString, List<String> String) {
        String res = null;
        for (java.lang.String s : String) {
            Pattern pattern1 = Pattern.compile(".*" + matchString + ".*");
            Matcher math = pattern1.matcher(s);
            if (math.find()) {
                res = s;
            }
        }
        return res;
    }
}
