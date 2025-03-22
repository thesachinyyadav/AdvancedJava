import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.InputMismatchException;


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
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countVote(candidate);
            votedVoters.add(voterName);  
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
        return name.matches("[a-zA-Z]+"); 
    }

    public static boolean isValidVoterId(String voterId) {
        return voterId.matches("[0-9]+"); 
    }

    // Newly added method to check if a voter has already voted
    public static boolean hasVoted(String voterName) {
        return votedVoters.contains(voterName);
    }
    
    // Newly added method to reset voting: clears vote counts and voter records
    public static synchronized void resetVoting() {
        voteCount.clear();
        votedVoters.clear();
        System.out.println("Voting has been reset.");
    }
}


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
            System.out.println("5. Check Voting Status");
            System.out.println("6. Reset Voting");
            System.out.print("Enter your choice: ");
            
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, please enter a valid number.");
                scanner.nextLine(); // clear the invalid input
                continue;
            }
            
            switch (choice) {
                case 1:
                  
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

                    if (!votingNotification.isVotingOpen()) {
                        System.out.println("Voting has not started yet. Please wait until it is open.");
                        break;
                    }

                   
                    System.out.println("Available candidates: Hema, Arjun, Chandana, Gayathri");
                    System.out.print("Enter candidate name: ");
                    String candidate = scanner.nextLine();

                    
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
                            System.exit(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                    break;

                case 5:
                    // Newly added feature to check voting status of a voter
                    System.out.print("Enter your name to check voting status: ");
                    String nameToCheck = scanner.nextLine();
                    if (VotingThread.hasVoted(nameToCheck)) {
                        System.out.println(nameToCheck + " has already voted.");
                    } else {
                        System.out.println(nameToCheck + " has not voted yet.");
                    }
                    break;
                    
                case 6:
                    // Newly added feature to reset the voting system
                    VotingThread.resetVoting();
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
