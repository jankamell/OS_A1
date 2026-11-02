public class TreatmentRooms {
    private final Semaphore rooms;
 
    public TreatmentRooms(int numberOfRooms) {
        this.rooms = new Semaphore(numberOfRooms);
    }
 
    public boolean tryAcquire(long timeoutMs) throws InterruptedException {
        return rooms.tryAcquire(timeoutMs);
    }
 
    public void release() {
        rooms.release();
    }
}