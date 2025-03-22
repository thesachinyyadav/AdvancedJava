import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

class VotingThread extends Thread {
    private final String voterId;
    private final String candidate;
    private static final Map<String, Integer> voteCount = new ConcurrentHashMap<>();
    private static final Set<String> votedVoters = Collections.synchronizedSet(new HashSet<>());
    private static final List<String> candidates = Arrays.asList("Hema", "Arjun", "Chandana", "Gayathri");

    public VotingThread(String voterId, String candidate) {
        this.voterId = voterId;
        this.candidate = candidate;
    }

   
    private static synchronized void countVote(String candidate) {
        voteCount.put(candidate, voteCount.getOrDefault(candidate, 0) + 1);
    }

    @Override
    public void run() {
        synchronized (votedVoters) {
            if (!votedVoters.contains(voterId)) {
                System.out.println("Processing vote for " + voterId + "...");
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countVote(candidate);
                votedVoters.add(voterId);
                System.out.println("Vote successfully cast for " + candidate);
            } else {
                System.out.println("Error: Voter ID " + voterId + " has already voted!");
            }
        }
    }

    public static void displayResults() {
        System.out.println("\n--- Voting Results ---");

        if (voteCount.isEmpty()) {
            System.out.println("No votes have been cast yet.");
            return;
        }

        int totalVotes = voteCount.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("Total Votes Cast: " + totalVotes);

        
        voteCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.printf("%s: %d votes (%.2f%%)\n", 
                    entry.getKey(), entry.getValue(), (entry.getValue() * 100.0) / totalVotes));
    }

    public static boolean isValidCandidate(String candidate) {
        return candidates.contains(candidate);
    }
}


class VotingControl {
    private volatile boolean votingOpen = false;

    public synchronized void startVoting() {
        if (!votingOpen) {
            System.out.println("Voting is now OPEN!");
            votingOpen = true;
        } else {
            System.out.println("Voting is already open.");
        }
    }

    public synchronized void stopVoting() {
        if (votingOpen) {
            System.out.println("Voting has been CLOSED.");
            votingOpen = false;
        } else {
            System.out.println("Voting is already closed.");
        }
    }

    public boolean isVotingOpen() {
        return votingOpen;
    }
}

public class DigitalVotingSystem4{
    private static final Map<String, String> registeredVoters = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        VotingControl votingControl = new VotingControl();
        registerVoters();

        while (true) {
            System.out.println("\n1. Cast Vote");
            System.out.println("2. Display Results");
            System.out.println("3. Start Voting");
            System.out.println("4. Stop Voting & Exit");
            System.out.print("Enter choice: ");
            
            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); 
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    if (!votingControl.isVotingOpen()) {
                        System.out.println("Voting is not open yet.");
                        break;
                    }
                    castVote();
                    break;

                case 2:
                    VotingThread.displayResults();
                    break;

                case 3:
                    new Thread(votingControl::startVoting).start();
                    break;

                case 4:
                    new Thread(() -> {
                        votingControl.stopVoting();
                        System.out.println("Exiting the system. Thank you.");
                        System.exit(0);
                    }).start();
                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void registerVoters() {
        registeredVoters.put("V123", "Surya");
        registeredVoters.put("V456", "Kundu");
        registeredVoters.put("V789", "Sachin");
    }

    private static void castVote() {
        System.out.print("Enter your Voter ID: ");
        String voterId = scanner.nextLine().trim();

        
        Predicate<String> isValidVoter = id -> registeredVoters.containsKey(id);
        if (!isValidVoter.test(voterId)) {
            System.out.println("Invalid Voter ID! You are not registered.");
            return;
        }

        System.out.println("Candidates: Hema, Arjun, Chandana, Gayathri");
        System.out.print("Enter candidate name: ");
        String candidate = scanner.nextLine().trim();

        
        Predicate<String> isValidCandidate = VotingThread::isValidCandidate;
        if (!isValidCandidate.test(candidate)) {
            System.out.println("Invalid candidate! Choose from available options.");
            return;
        }

        VotingThread votingThread = new VotingThread(voterId, candidate);
        votingThread.start();
        try {
            votingThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}