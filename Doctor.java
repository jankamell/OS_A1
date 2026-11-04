import java.util.concurrent.ThreadLocalRandom;

public class Doctor extends Thread {
    private final int id;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final Stats stats;

    public Doctor(int id, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms, Stats stats) {
        this.id = id;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.stats = stats;
        setName("Doctor " + id);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Patient patient = waitingRoom.removePatient();
                Logger.log(getName() + " is assigned to "+ patient.getName());
                Logger.log(getName() + " is trying to acquire a treatment room");

                boolean acquired = treatmentRooms.tryAcquire(3000);

                if (!acquired) {
                    Logger.log(getName() + " failed to acquire a treatment room");
                    Logger.log(getName() + " is sending " + patient.getName() + " back to the waiting room");
                    patient.returnToWaitingRoom();
                    continue; 
                }
                else {
                    Logger.log(getName() + " acquired a treatment room");
                }

                try {
                    Logger.log(getName() + " begins treating " + patient.getName());

                    int treatmentTime =ThreadLocalRandom.current().nextInt(1000, 5001);
                    Thread.sleep(treatmentTime); 

                    Logger.log(getName() + " finished treating " + patient.getName());
                } finally {
                    treatmentRooms.release();
                    Logger.log(getName() + " released the treatment room");
                }
                patient.finishTreatment();
                stats.patientTreated();
                Logger.log(getName() + " is now available");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
}
