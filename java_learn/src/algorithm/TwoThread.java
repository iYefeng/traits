package algorithm;

/**
 * Created by yefeng on 16/10/23.
 */



public class TwoThread {

    private boolean flag = true;
    private Object lock = new Object();


    class Task1 implements Runnable {

        char[] cArray;

        public Task1(String str) {
            cArray = str.toCharArray();
        }

        public void run() {
            synchronized (lock) {
                for (int i = 0; i < cArray.length; ++i) {
                    System.out.println(cArray[i]);
                    if ((i % 2 == 1 /*&& flag*/) || i == cArray.length -1) {
                        try {
                            //flag = false;
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                lock.notify();
            }

        }
    }

    class Task2 implements Runnable {

        char[] cArray;

        public Task2(String str) {
            cArray = str.toCharArray();
        }

        public void run() {
            synchronized (lock) {
                for (int i = 0; i < cArray.length; ++i) {
                    System.out.println(cArray[i]);

                    if ((i % 2 == 1 /*&& !flag*/) || i == cArray.length -1) {
                        try {
                            //flag = true;
                            lock.notify();
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                lock.notify();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String str1 = "abcdefghi";
        String str2 = "123456789";
        TwoThread tt = new TwoThread();
        Task1 t1 = tt.new Task1(str1);
        Thread thread1 = new Thread(t1);

        Task2 t2 = tt.new Task2(str2);
        Thread thread2 = new Thread(t2);

        thread1.start();
        thread2.start();

    }

}
