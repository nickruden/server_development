package Lab3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

public class Lab3ReentrantLock {
    public static final int ITERATIONS = 100000;
    private static int counter = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        int[] threadCounts = { 1, 2, 4, 8 };

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Lab8.txt"))) {
            writer.write("Streams (n, m) | Counter | Time (мс)\n");

            for (int n : threadCounts) {
                for (int m : threadCounts) {
                    long startTime = System.currentTimeMillis();

                    Thread[] incrementThreads = new Thread[n];
                    Thread[] decrementThreads = new Thread[m];

                    // Создание и запуск потоков, которые инкрементируют счетчик
                    for (int i = 0; i < n; i++) {
                        incrementThreads[i] = new Thread(new IncrementTask());
                        incrementThreads[i].start();
                    }

                    // Создание и запуск потоков, которые декрементируют счетчик
                    for (int i = 0; i < m; i++) {
                        decrementThreads[i] = new Thread(new DecrementTask());
                        decrementThreads[i].start();
                    }

                    // Ожидание завершения всех потоков
                    try {
                        for (int i = 0; i < n; i++) {
                            incrementThreads[i].join();
                        }
                        for (int i = 0; i < m; i++) {
                            decrementThreads[i].join();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    long endTime = System.currentTimeMillis();
                    long executionTime = endTime - startTime;

                    writer.write(String.format("(%d, %d) | %d | %d ms\n", n, m, counter, executionTime));
                    counter = 0; // сброс счетчика для следующего запуска
                }
            }

            writer.write("\nCPU: " + System.getProperty("os.arch") + "\n");
            writer.write("RAM: " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " MB\n");
            writer.write("OC: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class IncrementTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                lock.lock();
                counter++;
                lock.unlock();
            }
        }
    }

    static class DecrementTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                lock.lock();
                counter--;
                lock.unlock();
            }
        }
    }
}