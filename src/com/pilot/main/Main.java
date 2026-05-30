package com.pilot.main;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Java Project!");
        boolean verbose = false;
        for (String arg : args) {
            if ("--verbose".equalsIgnoreCase(arg)) {
                verbose = true;
            }
        }
        System.out.println("Running... Press Ctrl+C to exit.");
        long seconds = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted while sleeping; keeping the app alive.");
                continue;
            }
            if (verbose) {
                seconds++;
                System.out.println("Running... (" + seconds + "s)");
            }
        }
    }
}
