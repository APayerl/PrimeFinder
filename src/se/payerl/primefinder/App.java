package se.payerl.primefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private FileSaver saver;
    private File file;
    private List<Long> foundPrimes;

    public static void main(String[] args) throws Exception {
	    App app = new App();
	    app.execute();
    }

    public App() throws IOException {
        this.file = new File("primes.data");
        file.createNewFile();
        this.saver = new FileSaver(this.file);
        Runtime.getRuntime()
            .addShutdownHook(new Thread(() -> this.saver.saveToFile(null)));
    }

    public void execute() throws FileNotFoundException {
        init();

        for(long current = this.foundPrimes.get(this.foundPrimes.size() - 1);; current += 1l) {
            if(isPrime(current)) {
                this.foundPrimes.add(current);
                this.saver.saveToFile(Long.valueOf(current));
                System.out.println(this.foundPrimes.size() + ": " + current);
            }
        }
    }

    public List<Long> getEarlierIterations() throws FileNotFoundException {
        List<Long> list = new ArrayList<>();
        Scanner sc = null;
        sc = new Scanner(this.file);
        while(sc.hasNextLine()) {
            list.add(Long.parseLong(sc.nextLine()));
        }
        sc.close();
        return list;
    }

    public void init() throws FileNotFoundException {
        this.foundPrimes = getEarlierIterations();
        if(this.foundPrimes.isEmpty()) {
            this.foundPrimes.add(2l);
            new Thread(this.saver).start();
            this.saver.saveToFile(2l);
        }
    }

    public boolean isPrime(Long value) {
        boolean isPrime = true;
        for(int i = 0; i < this.foundPrimes.size(); i++) {
            double base = (double) this.foundPrimes.get(i);
            if(String.valueOf(value / base).split("\\.")[1].equals("0")) {
                isPrime = false;
                break;
            }
        }
        return isPrime;
    }
}