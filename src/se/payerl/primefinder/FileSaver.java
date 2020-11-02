package se.payerl.primefinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileSaver extends Thread implements Functional {
    private File file;
    private List<String> list;
    private boolean keepGoing;
    private Instant lastWrite;
    // private SignalHandler sH;

    public FileSaver(File file) throws IOException {
        this.file = file;
        // this.queue = new LinkedList<>();
        this.list = new LinkedList<>();
        this.keepGoing = true;
        this.lastWrite = Instant.now();
        // sH = new SignalHandler() {
        //     @Override
        //     public void handle(Signal sig) {
        //         if(sig.equals(new Signal("INT"))) {
        //             keepGoing = false;
        //         }
        //     }
        // };
    }

    @Override
    public void saveToFile(Long val) {
        if (val == null) {
            this.keepGoing = false;
            //System.out.println("Objects left: " + this.queue.size());
            save();
        } else {
            synchronized(this.list) {
                this.list.add(val + "");
            }
        }
    }

    private void save() {
        synchronized(this.list) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(this.file));
                String s = null;
                // System.out.println(list.size());
                while(!list.isEmpty() && (s = list.remove(0)) != null) {
                    bw.append(s + "\n");
                    System.out.println(s);
                }
                bw.flush();
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (this.keepGoing || !this.list.isEmpty()) {
            save();
        }
    }
}