public class Stats {
    private final int totalPatients;
    private int treatedCount = 0;
    private final Semaphore mutex = new Semaphore(1);          
    private final Semaphore allDoneSignal = new Semaphore(0); 
 
    public Stats(int totalPatients) {
        this.totalPatients = totalPatients;
    }
 
    public void patientTreated() throws InterruptedException {
        boolean last;
        mutex.acquire();
        treatedCount++;
        last = (treatedCount == totalPatients);
        mutex.release();
        if (last) {
            allDoneSignal.release();
        }
    }
 
    public boolean allDone() throws InterruptedException {
        mutex.acquire();
        boolean done = (treatedCount == totalPatients);
        mutex.release();
        return done;
    }
 
    public void waitUntilAllDone() throws InterruptedException {
        allDoneSignal.acquire();
        allDoneSignal.release();
    }
}