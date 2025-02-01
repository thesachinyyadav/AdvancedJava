import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

// Thread Class: Voting Process
class VotingThread extends Thread {
    private String voterName;
    private String candidate;
    private static Map<String, Integer> voteCount = new HashMap<>();
    private static Set<String> votedVoters = new HashSet<>();
    private static final String[] validCandidates = {"Hema", "Arjun", "Chandana", "Gayathri"};

    public VotingThread(String voterName, String candidate) {
        this.voterName = voterName;
        this.candidate = candidate;
    }

    public static synchronized void countVote(String candidate) {
        voteCount.put(candidate, voteCount.getOrDefault(candidate, 0) + 1);
    }

    @Override
    public void run() {
        if (!votedVoters.contains(voterName)) {
            System.out.println(voterName + " is casting vote...");
            try {
                Thread.sleep(1000); // Simulate voting process
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countVote(candidate);
            votedVoters.add(voterName);  // Add voter to the set after voting
            System.out.println(voterName + " successfully voted for " + candidate);
        } else {
            System.out.println(voterName + " has already voted. You can only vote once.");
        }
    }

    public static void displayResults() {
        System.out.println("\n--- Voting Results ---");
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " votes");
        }
    }

    public static boolean isValidCandidate(String candidate) {
        for (String validCandidate : validCandidates) {
            if (validCandidate.equalsIgnoreCase(candidate)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isValidVoterName(String name) {
        return name.matches("[a-zA-Z]+"); // Only letters are allowed in the name
    }

    public static boolean isValidVoterId(String voterId) {
        return voterId.matches("[0-9]+"); // Only digits are allowed in the voter ID
    }
}

// Thread Communication: Voting Notification
class VotingNotification {
    private boolean votingOpen = false;

    public synchronized void startVoting() throws InterruptedException {
        while (votingOpen) {
            wait();
        }
        System.out.println("Voting is now open!");
        votingOpen = true;
        notifyAll();
    }

    public synchronized void stopVoting() throws InterruptedException {
        while (!votingOpen) {
            wait();
        }
        System.out.println("Voting has been closed.");
        votingOpen = false;
        notifyAll();
    }

    public boolean isVotingOpen() {
        return votingOpen;
    }
}

public class DigitalVotingSystem2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VotingNotification votingNotification = new VotingNotification();

        while (true) {
            System.out.println("\n--- Digital Voting System Menu ---");
            System.out.println("1. Cast Vote");
            System.out.println("2. Display Results");
            System.out.println("3. Start Voting");
            System.out.println("4. Stop Voting (Exit)");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    // Input: Voter Name
                    String voterName;
                    while (true) {
                        System.out.print("Enter your name: ");
                        voterName = scanner.nextLine();
                        if (VotingThread.isValidVoterName(voterName)) {
                            break;
                        } else {
                            System.out.println("Invalid name. Please enter only alphabetic characters.");
                        }
                    }

                    // Input: Voter ID
                    String voterId;
                    while (true) {
                        System.out.print("Enter your Voter ID (numeric only): ");
                        voterId = scanner.nextLine();
                        if (VotingThread.isValidVoterId(voterId)) {
                            break;
                        } else {
                            System.out.println("Invalid Voter ID. Please enter only numeric digits.");
                        }
                    }

                    // Ensure voting has started
                    if (!votingNotification.isVotingOpen()) {
                        System.out.println("Voting has not started yet. Please wait until it is open.");
                        break;
                    }

                    // Input: Candidate
                    System.out.println("Available candidates: Hema, Arjun, Chandana, Gayathri");
                    System.out.print("Enter candidate name: ");
                    String candidate = scanner.nextLine();

                    // Validate candidate
                    if (VotingThread.isValidCandidate(candidate)) {
                        VotingThread votingThread = new VotingThread(voterName, candidate);
                        votingThread.start();
                        try {
                            votingThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Invalid candidate. Please choose from the available candidates.");
                    }
                    break;

                case 2:
                    VotingThread.displayResults();
                    break;

                case 3:
                    new Thread(() -> {
                        try {
                            votingNotification.startVoting();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    break;

                case 4:
                    new Thread(() -> {
                        try {
                            votingNotification.stopVoting();
                            System.out.println("Exiting the system. Thank you!");
                            System.exit(0); // Exit the program when voting is stopped
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
