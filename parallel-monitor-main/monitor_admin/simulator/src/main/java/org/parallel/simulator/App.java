package org.parallel.simulator;

public class App {
    public static void main(String[] args) {
        int len = args.length;
        int nums = 12;
        if (len > 0) {
            String clientNums = args[0];
            try {
                nums = Integer.parseInt(clientNums);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (nums > 1000) {
            System.out.println("模拟节点最大为1000");
            nums = 1000;
        }
        Net net = new Net("127.0.0.1", 6007, 6008, nums);
        net.start();
    }
}
