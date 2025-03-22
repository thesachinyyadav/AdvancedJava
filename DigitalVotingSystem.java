import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DigitalVotingSystem {

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
        // Creating a voting system instance for Integer IDs
        VotingSystem<Integer> votingSystem = new VotingSystem<>();

        // Adding candidates
        votingSystem.addCandidate(new Candidate<>(1, "Surya"));
        votingSystem.addCandidate(new Candidate<>(2, "Sachin"));
        votingSystem.addCandidate(new Candidate<>(3, "Hema"));

        // Adding voters
        votingSystem.addVoter(new Voter<>(101, "John"));
        votingSystem.addVoter(new Voter<>(102, "Alice"));
        votingSystem.addVoter(new Voter<>(103, "Bob"));

        // Display Voters and Candidates
        votingSystem.displayVoters();
        votingSystem.displayCandidates();

        // Casting votes
        votingSystem.castVote(101, 1);  // John votes for Surya
        votingSystem.castVote(102, 2);  // Alice votes for Sachin
        votingSystem.castVote(103, 1);  // Bob votes for Surya

        // Display voting results
        votingSystem.displayResults();
    }
}
