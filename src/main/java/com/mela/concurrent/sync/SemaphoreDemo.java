package com.mela.concurrent.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore-控制并发线程数
 *
 * 业务场景：
 *     假如现在有20个人去买票，但只有2个窗口，那么同时买票的只有两个人；
 *     当2个人当中的任意一个人买完票离开后，其余18个人有会有一个人可占用窗口继续买票。
 * 拆解：20个人即20个线程，窗口即共享的资源。
 * 实际含义：如何控制同一时间并发数为2；
 * Semaphore--信号量（控制并发线程数）
 *
 *
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        SemaphoreDemo demo = new SemaphoreDemo();
        demo.execute();
    }

    private void execute() {
        // 1. 定义窗口数量
        final Semaphore semaphore = new Semaphore(2);

        // 2. 线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        // 3. 模拟20个用户
        for (int i = 0; i < 20; i++) {
            executorService.submit(new SemaphoreRunnable(semaphore, (i + 1)));
        }

        // 4. 关闭线程池
        executorService.shutdown();

    }

    /**
     * 执行任务类
     * 获取信号量和释放信号量
     */
    class SemaphoreRunnable implements Runnable {
        private Semaphore semaphore; // 定义信号量
        private int userIndex; // 记录第几个用户

        public SemaphoreRunnable(Semaphore semaphore, int userIndex) {
            this.semaphore = semaphore;
            this.userIndex = userIndex;
        }

//        @Override
        public void run() {

            try {
                semaphore.acquire(); // 1. 获取信号量许可

                System.out.println("用户：[" + userIndex + "] 进入窗口买票");
                Thread.sleep((long) Math.random() * 2000);
                System.out.println("用户：[" + userIndex + "] 买票完票，准备离开...");
                Thread.sleep((long) Math.random() * 3000);
                System.out.println("用户：[" + userIndex + "] 离开窗口");

                semaphore.release(); // 2. 释放信号量
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
