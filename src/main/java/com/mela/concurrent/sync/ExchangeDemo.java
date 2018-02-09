package com.mela.concurrent.sync;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 业务场景：
 *      绑架案：目的为了用钱赎人。
 *      张三团伙绑架了小乔，放言用1000万赎回小乔；
 *      张三团伙和小乔同时到达约定地点，然后进行交易。
 * 转化：两个线程，在同一个点（阻塞点），交换数据
 * 并发包：Exchanger：两个线程进行数据交换。
 * （1）用于两个线程数据交换
 * （2）用与校对工作
 */
public class ExchangeDemo {

    public static void main(String[] args) {
        // 1. 交换器，交换String类型
        final Exchanger<String> exchanger = new Exchanger<String>();

        // 2. 线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        // 张三团伙，用小乔交换1000万
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    String returnStr = exchanger.exchange("小乔");
                    System.out.println("绑架者用小乔换来：" + returnStr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 大乔，用1000万交换小乔
        executorService.execute(new Runnable() {
            public void run() {
                try {
                    // 此处不可等，两个交换者必须同时到达
//                    Thread.sleep((long) Math.random() * 10000);
//                    Thread.sleep((long) Math.random() * 10000);

                    String returnStr = exchanger.exchange("1000万");
                    System.out.println("大乔用1000万换来：" + returnStr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 关闭线程池
        executorService.shutdown();
    }

}
