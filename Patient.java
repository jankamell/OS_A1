import java.util.concurrent.CountDownLatch;

public class Patient extends Thread {

    private final int id;
    private final WaitingRoom waitingRoom;
    private final CountDownLatch treatmentFinished = new CountDownLatch(1);

    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        setName("P" + id);
    }

    @Override
    public void run() {
        try {
            Logger.log(getName() + " arrived.");

         
            waitingRoom.enter(this);

            treatmentFinished.await();

            Logger.log(getName() + " left.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    
    public void returnToWaitingRoom() {
        new Thread(() -> {
            try {
                waitingRoom.reEnter(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, getName() + "-reenter").start();
    }

    public void finishTreatment() {
        treatmentFinished.countDown();
    }

    public int getPatientId() {
        return id;
    }
}