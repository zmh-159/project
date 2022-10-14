package org.parallel.common.utils.cmd;

import java.text.DecimalFormat;

public class StringFormat {
    public static String progressBarFormat(String s, int length) {
        StringBuilder res = new StringBuilder();
        res.append(s);
        int num = s.length() / 9;
        if (num < length) {
            res.append(" ".repeat(length - num));
        }
        return res.toString();
    }

    public static String sufInsertSpaceFormat(String s, int length) {
        StringBuilder res = new StringBuilder();
        res.append(s);
        if (s.length() < length) {
            res.append(" ".repeat(length - s.length()));
        }
        return res.toString();
    }

    public static String preInsertSpaceFormat(String s, int length) {
        StringBuilder res = new StringBuilder();
        if (s.length() < length) {
            res.append(" ".repeat(length - s.length()));
        }
        res.append(s);
        return res.toString();
    }

    public static String progressBar(double value, int color) {
        String s = "||||||||||||||||||||||||||||||||||||||||||||||||||";
        StringBuilder res = new StringBuilder();
        int size = (int) Math.round(value);
        if (size % 2 != 0 && size < 100) {
            size = size + 1;
        }
        for (int i = 0; i < size * s.length() / 100; i++) {
            res.append("\u001B[").append(color).append("m").append("|").append("\u001B[m");
        }
        return res.toString();
    }

    public static String stringRed(String str) {
        return "\u001B[91m" + str + "\u001B[m";
    }

    public static String stringGreen(String str) {
        return "\u001B[92m" + str + "\u001B[m";
    }

    public static Double roundByScale(double v, int scale) {
        return Double.parseDouble(new DecimalFormat("0." + "0".repeat(Math.max(0, scale))).format(v));
    }

}
