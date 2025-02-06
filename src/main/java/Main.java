import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class Main {

    private static final String FILE_NAME = "numbers.txt";
    private static final Object lock = new Object();
    private static final Random randomizer = new Random();

    public static void main(String[] args) {
        Thread evenWriter = new Thread(new EvenNumberWriter());
        Thread oddWriter = new Thread(new OddNumberWriter());
        Thread reader = new Thread(new NumberReader());

        evenWriter.start();
        oddWriter.start();
        reader.start();
    }

    static class EvenNumberWriter implements Runnable {
        @Override
        public void run() {
            while (true) {
                int number = randomizer.nextInt(1000) * 2;
                writeNumber(number);
            }
        }
    }

    static class OddNumberWriter implements Runnable {
        @Override
        public void run() {
            while (true) {
                int number = randomizer.nextInt(1000) * 2 + 1;
                writeNumber(number);
            }
        }
    }

    static class NumberReader implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))) {
                        String currentLine;
                        String lastLine = null;
                        while ((currentLine = bufferedReader.readLine()) != null) {
                            lastLine = currentLine;
                        }
                        if (lastLine != null) {
                            System.out.println("Last number: " + lastLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeNumber(int number) {
        synchronized (lock) {
            try (FileWriter fw = new FileWriter(FILE_NAME, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                out.println(number);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
