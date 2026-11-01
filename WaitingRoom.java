import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private final int capacity;
    private final Queue<Patient> queue;
    private final Semaphore mutex;
    private final Semaphore empty;
    private final Semaphore full;
    private final Object admissionLock;

    public WaitingRoom(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.mutex = new Semaphore(1);
        this.empty = new Semaphore(capacity);
        this.full = new Semaphore(0);
        this.admissionLock = new Object();
    }

    public void enter(Patient patient) throws InterruptedException {
        boolean acquired;
        synchronized (admissionLock) {
            acquired = empty.tryAcquire();
            if (!acquired) {
                Logger.log("The waiting room is full.");
                Logger.log(patient.getName() + " is waiting outside for an available seat.");
            }
        }
        if (!acquired) {
            empty.acquire();
        }
        Logger.log(patient.getName() + " entered the waiting room.");
        mutex.acquire();
        queue.add(patient);
        mutex.release();
        full.release();
    }

    public void admit(Patient patient) throws InterruptedException {
        enter(patient);
    }

    public void insert(Patient patient) throws InterruptedException {
        enter(patient);
    }

    public void reEnter(Patient patient) throws InterruptedException {
        enter(patient);
    }

    public Patient removePatient() throws InterruptedException {
        full.acquire();
        mutex.acquire();
        Patient patient = queue.poll();
        mutex.release();
        empty.release();
        return patient;
    }

    public Patient getPatient() throws InterruptedException {
        return removePatient();
    }

    public int getCapacity() {
        return capacity;
    }

    public int getOccupancy() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public boolean isFull() {
        return queue.size() >= capacity;
    }
}
