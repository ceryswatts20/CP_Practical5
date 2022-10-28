/*===========================================================
  Updated
  Hans Vandierendonck September 2014
===========================================================*/

// Solution to Q1
class Example1FullImpl {
    static final int MAX_NUMBER = 10;
    public static void main (String[] args) {

        System.out.println("Start of program.");
        PCMImpl pcm = new PCMImpl();
        ProducerImpl producer  = new ProducerImpl(pcm,0);
        ConsumerImpl consumer  = new ConsumerImpl(pcm,0);

        producer.start();
        consumer.start();
    }
}

class ProducerImpl extends Thread {
    private int number = 0;
    private PCMImpl pcm;
    private int id;

    public ProducerImpl(PCMImpl pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i=produce();
            pcm.put(i);
        } while (number  != Example1.MAX_NUMBER);
        System.out.println("P: finished");
    }

    public int produce() {
        number++;
        return number;
    }
}

class ConsumerImpl extends Thread {
    private int number = 0;
    private PCMImpl pcm;
    private int id;

    public ConsumerImpl(PCMImpl pcm, int id) {
        this.pcm = pcm;
        this.id = id;
    }

    public void run() {
        do {
            int i = pcm.get();
            consume(i);
        } while (number  != Example1.MAX_NUMBER);
        System.out.println("C: finished");
    }

    public void consume(int i) {
        number++;
    }
}

class PCMImpl {
  private int N = 4;
  private int[] buffer = new int [N];
  private int tail = 0, head = 0, count = 0;

  public synchronized void put (int i) {
      while (count == N) try {wait();} catch (InterruptedException e){}
      buffer[tail] = i;
      tail = (tail + 1) % N;
      count++;
      System.out.println("P: " + i + " put in buffer");
      notifyAll();
  }
  public synchronized int get() {
      int i;
      while (count == 0) try {wait();} catch (InterruptedException e){}
      i = buffer[head];
      head = (head + 1) % N;
      count--;
      System.out.println("C: " + i + " read from buffer");
      notifyAll();
      return i;
  }
}
