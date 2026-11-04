import java.util.Scanner;
public class Hospital {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Waiting room capacity: ");
        int capacity = scanner.nextInt();

        System.out.print("Number of doctors: ");
        int numberOfDoctors = scanner.nextInt();

        System.out.print("Number of patients: ");
        int numberOfPatients = scanner.nextInt();

        if (capacity <= 0 || numberOfDoctors <= 0 || numberOfPatients < 0) {
            System.out.println("Invalid input. Please enter positive integers for capacity and number of doctors, and a non-negative integer for number of patients.");
            scanner.close();
            return;
        }

        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms treatmentRooms = new TreatmentRooms(2);     
        Stats stats = new Stats(numberOfPatients);

        Doctor[] doctors = new Doctor[numberOfDoctors];
        for (int i = 0; i < numberOfDoctors; i++) {
            doctors[i] = new Doctor(i + 1, waitingRoom, treatmentRooms, stats);
        }

        Patient[] patients = new Patient[numberOfPatients];
        for (int i = 0; i < numberOfPatients; i++) {
            patients[i] = new Patient(i + 1, waitingRoom);
        }

        System.out.println("\n Patients arriving: ");
        for (Patient patient : patients) {
            System.out.print(patient.getName() + " ");
        }
        System.out.println();

        try {

            for (Doctor doctor : doctors) {
                doctor.start();
            }

            for (Patient patient : patients) {
                patient.start();
            }

            if (numberOfPatients > 0) {
                stats.waitUntilAllDone();
            }

            for (Doctor doctor : doctors) {
                doctor.interrupt();
            }

            for (Doctor doctor : doctors) {
                doctor.join();
            }

            for (Patient patient : patients) {
                patient.join();
            }

            Logger.log("All patients have been treated");
            Logger.log("Simulation completed");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.out.println("Hospital was interrupted unexpectedly by the user, Closing the hospital");

        } finally {

            scanner.close();
        }
    
    }
    
}
