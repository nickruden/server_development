package Lab3;

public class Lab3NoSync {
    public static final int ITERATIONS = 100000;
    private static int counter = 0;

    public static void main(String[] args) {
        int n = 5; // количество потоков, которые инкрементируют счетчик
        int m = 5; // количество потоков, которые декрементируют счетчик

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

        System.out.println("Значение счётчика: " + counter);
        System.out.println("Время выполнения: " + executionTime + " ms");
    }

    static class IncrementTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                int localCounter = counter;
                localCounter++;
                counter = localCounter;
            }
        }
    }

    static class DecrementTask implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < ITERATIONS; i++) {
                int localCounter = counter;
                localCounter--; 
                counter = localCounter;
            }
        }
    }
}