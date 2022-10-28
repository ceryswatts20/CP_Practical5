/*===========================================================
  Updated
  Hans Vandierendonck September 2014
===========================================================*/

import java.util.Random;

// Q1: Replace while loops with if statements to show signal-continue erroneous behaviour
class Example2 {
    static final int MAX_NUMBER = 10;

    public static void main(String[] args) {

        System.out.println("Start of program.");
        PCM2 pcm = new PCM2();
        Producer2 producer = new Producer2(pcm, 0);
        Consumer2 consumer0 = new Consumer2(pcm, 0);
        Consumer2 consumer1 = new Consumer2(pcm, 1);

        producer.start();
        consumer0.start();
        consumer1.start();
    }
}

class Producer2 extends Thread {
    private Random rnd = new Random();
    private int number = 0;
    private PCM2 pcm;
    private int id;

    public Producer2(PCM2 pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i = produce();
            pcm.put(i);
            Time.delay(rnd.nextInt(120));
        } while (number != Example2.MAX_NUMBER);
        System.out.println("P: finished");
    }

    public int produce() {
        number++;
        return number;
    }
}

class Consumer2 extends Thread {
    private Random rnd = new Random();
    private int number = 0;
    private PCM2 pcm;
    private int id;

    public Consumer2(PCM2 pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i = pcm.get(id);
            consume(i);
            Time.delay(rnd.nextInt(120));
        } while (number != Example2.MAX_NUMBER);
        System.out.println("C: finished");
    }

    public void consume(int i) {
        number++;
    }
}

class PCM2 {
    private int N = 4;
    private int[] buffer = new int[N];
    private int tail = 0, head = 0;
    private int count = 0;

    public synchronized void put(int i) {
//    while (count == N) try {wait();} catch (InterruptedException e){}
        if (count == N) try {
            wait();
        } catch (InterruptedException e) {
        }
        buffer[tail] = i;
        tail = (tail + 1) % N;
        count++;
        System.out.println("P : " + i + " put in buffer");
        notifyAll();
    }

    public synchronized int get(int id) {
        int i;
//    while (count == 0) try {wait();} catch (InterruptedException e){}
        if (count == 0) try {
            wait();
        } catch (InterruptedException e) {
        }
        i = buffer[head];
        head = (head + 1) % N;
        count--;
        System.out.println("C" + id + ": " + i + " read from buffer");
        notifyAll();
        return i;
    }
}
