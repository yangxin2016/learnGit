package com.security.wrr.controller;

public class TestRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + "线程被调用了。");
        for(int i=0;i<10;i++){
            System.out.println(i);
        }
    }
}
