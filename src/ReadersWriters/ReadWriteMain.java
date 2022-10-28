package ReadersWriters;

public class ReadWriteMain {

    public static void main(String[] args) {
        System.out.println("Start of program.");
        Reader reader = new Reader();
        Writer writer = new Writer();

        reader.start();
        writer.start();
    }
}
