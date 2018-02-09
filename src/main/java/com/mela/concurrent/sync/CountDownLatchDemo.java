package com.mela.concurrent.sync;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) {
        // 倒计时计数器
        final CountDownLatch countDownLatch = new CountDownLatch(2);

        // 任务1
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("任务1正在执行。。。");
                    Thread.sleep((long) Math.random() * 10000);
                    System.out.println("任务1执行完毕。。。");

                    countDownLatch.countDown(); // 任务1执行完，倒计时计数器减1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // 任务2
        new Thread() {
            @Override
            public void run() {
                try {
                    System.out.println("任务2正在执行。。。");
                    Thread.sleep((long) Math.random() * 10000);
                    System.out.println("任务2执行完毕。。。");

                    countDownLatch.countDown(); // 任务2执行完，倒计时计数器减1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // 主线程
        System.out.println("主线程等待其他两个任务执行完毕，才开始执行任务 " + Thread.currentThread().getName());

        // 阻塞点
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("主线程等待其他2个任务执行完毕，主线程执行任务 " + Thread.currentThread().getName());

    }

}
