package com.mela.concurrent.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务场景：
 *      公司组织周末聚餐，首先各自从家到聚餐点，全部到齐后，才开始吃饭（同步点）。
 *      若人员未到齐，已到的人只能等待在那里（阻塞），直到所有人都到齐后才开始吃。
 * 并发包：CyclicBarrier：可循环的障碍物，同步点; 可重复使用
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        // 1. 3个人聚餐，限制同时3个人同时到达
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(3, new Runnable() {
            public void run() {
                System.out.println("人员全部到齐，拍照留念");

                System.out.println("人员全部到达，开始吃饭.....");
            }
        });

        // 2. 线程池
        ExecutorService executorService = Executors.newCachedThreadPool();


        // 模拟3个用户
        for (int i = 0; i < 3; i++) {
            final int userIndex = i+1;
            Runnable rb = new Runnable() {
                public void run() {
                    try {
                        // 1. 模拟每个人来的时间不同
                        Thread.sleep((long) Math.random() * 10000);
                        System.out.println("用户：" + userIndex + "到达聚点，当前已有：" + (cyclicBarrier.getNumberWaiting() + 1));


                        // 2. 阻塞
                        cyclicBarrier.await();


                        // 3. await()阻塞直到所有人都到达后，往下执行
                        System.out.println(userIndex + "吃完饭了，准备回家.....");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(rb);
        }
        // 4. 关闭线程池
        executorService.shutdown();
    }
}
