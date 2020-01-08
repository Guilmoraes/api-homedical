package br.com.homedical.util;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class ThreadUtils {

    public static void awaitSilently(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Consumer<Long> awaitSilently(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return empty -> {

        };

    }

}
