package se.payerl.primefinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public class FileSaver implements Runnable, Functional {
    private File file;
    private Queue<String> queue;
    private boolean keepGoing;

    public FileSaver(File file) {
        this.file = file;
        this.queue = new LinkedList<>();
        this.keepGoing = true;
    }

    @Override
    public void saveToFile(Long val) {
        if (val == null) {
            this.keepGoing = false;
            System.out.println("Objects left: " + this.queue.size());
        } else {
            this.queue.add(val + "");
        }
    }

    @Override
    public void run() {
        FileWriter fw = null;
        try {
            fw = new FileWriter(this.file, true);
            while (this.keepGoing || !this.queue.isEmpty()) {
                if (!this.queue.isEmpty()) {
                    fw.write(this.queue.poll() + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}
