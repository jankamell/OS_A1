public class Semaphore {
    private int permits;

    public Semaphore(int count) {
        this.permits = count;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits == 0) {
            wait();
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notifyAll();
    }

    public synchronized boolean tryAcquire() {
        if (permits > 0) {
            permits--;
            return true;
        }
        return false;
    }

    public synchronized boolean tryAcquire(long timeout) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long remainingTime = timeout;

        while (permits == 0 && remainingTime > 0) {
            wait(remainingTime);
            long elapsedTime = System.currentTimeMillis() - startTime;
            remainingTime = timeout - elapsedTime;
        }
        if (permits > 0) {
            permits--;
            return true;
        }
        return false;
    }
}
