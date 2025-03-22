import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Program3 {

    // Generic class for Voter
    static class Voter<T> {
        private T voterId;  
        private String name;

        public Voter(T voterId, String name) {
            this.voterId = voterId;
            this.name = name;
        }

        public T getVoterId() {
            return voterId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Voter [ID=" + voterId + ", Name=" + name + "]";
        }
    }

    // Generic class for Candidate
    static class Candidate<T> {
        private T candidateId;
        private String name;

        public Candidate(T candidateId, String name) {
            this.candidateId = candidateId;
            this.name = name;
        }

        public T getCandidateId() {
            return candidateId;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Candidate [ID=" + candidateId + ", Name=" + name + "]";
        }
    }

    // Voting system class
    static class VotingSystem<T> {
        private List<Voter<T>> voters;
        private List<Candidate<T>> candidates;
        private HashMap<T, T> votes;  // Key: Voter ID, Value: Candidate ID

        public VotingSystem() {
            voters = new ArrayList<>();
            candidates = new ArrayList<>();
            votes = new HashMap<>();
        }

        // Add Voter
        public void addVoter(Voter<T> voter) {
            voters.add(voter);
        }

        // Add Candidate
        public void addCandidate(Candidate<T> candidate) {
            candidates.add(candidate);
        }

        // Cast Vote
        public void castVote(T voterId, T candidateId) {
            boolean validVoter = false;
            boolean validCandidate = false;

            // Check if voter is valid
            for (Voter<T> voter : voters) {
                if (voter.getVoterId().equals(voterId)) {
                    validVoter = true;
                    break;
                }
            }

            // Check if candidate is valid
            for (Candidate<T> candidate : candidates) {
                if (candidate.getCandidateId().equals(candidateId)) {
                    validCandidate = true;
                    break;
                }
            }

            // If both are valid, cast the vote
            if (validVoter && validCandidate) {
                votes.put(voterId, candidateId);
                System.out.println("Vote cast successfully!");
            } else {
                System.out.println("Invalid voter or candidate.");
            }
        }

        // Display Results
        public void displayResults() {
            System.out.println("\nVoting Results:");
            HashMap<T, Integer> voteCounts = new HashMap<>();

            // Count votes for each candidate
            for (T candidateId : votes.values()) {
                voteCounts.put(candidateId, voteCounts.getOrDefault(candidateId, 0) + 1);
            }

            // Display vote counts for each candidate
            for (Candidate<T> candidate : candidates) {
                int votesForCandidate = voteCounts.getOrDefault(candidate.getCandidateId(), 0);
                System.out.println("Candidate " + candidate.getName() + " has " + votesForCandidate + " votes.");
            }
        }

        // Display Candidates
        public void displayCandidates() {
            System.out.println("\nCandidates:");
            for (Candidate<T> candidate : candidates) {
                System.out.println(candidate);
            }
        }

        // Display Voters
        public void displayVoters() {
            System.out.println("\nVoters:");
            for (Voter<T> voter : voters) {
                System.out.println(voter);
            }
        }
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        // Creating a voting system instance for Integer IDs
        VotingSystem<Integer> votingSystem = new VotingSystem<>();

        // Pre-adding candidates
        votingSystem.addCandidate(new Candidate<>(1, "arjun"));
        votingSystem.addCandidate(new Candidate<>(2, "kundu"));
        votingSystem.addCandidate(new Candidate<>(3, "Hema"));

        // Pre-adding voters
        votingSystem.addVoter(new Voter<>(101, "surya"));
        votingSystem.addVoter(new Voter<>(102, "sachin"));
        votingSystem.addVoter(new Voter<>(103, "sai")); 

        int choice;
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Display Voters");
            System.out.println("2. Display Candidates");
            System.out.println("3. Cast Vote");
            System.out.println("4. Display Voting Results");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch(choice) {
                case 1:
                    votingSystem.displayVoters();
                    break;
                case 2:
                    votingSystem.displayCandidates();
                    break;
                case 3:
                    System.out.print("Enter Voter ID: ");
                    Integer voterId = scanner.nextInt();
                    System.out.print("Enter Candidate ID: ");
                    Integer candidateId = scanner.nextInt();
                    votingSystem.castVote(voterId, candidateId);
                    break;
                case 4:
                    votingSystem.displayResults();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while(choice != 5);

        scanner.close();
    }
}
