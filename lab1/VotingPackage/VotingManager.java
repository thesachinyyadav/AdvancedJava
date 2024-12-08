package VotingPackage;

import java.util.HashMap;
import java.util.Map;

public class VotingManager implements Voter {
    private Map<String, Integer> voteTally; // Map to store votes for each candidate
    private Map<String, Boolean> votedUsers; // Track if a user has already voted

    public VotingManager() {
        this.voteTally = new HashMap<>();
        this.votedUsers = new HashMap<>();
        // Initialize candidates
        voteTally.put("Sachin", 0);
        voteTally.put("Surya", 0);
        voteTally.put("Arjun", 0);
    }

    // Validate if the user has voted already
    @Override
    public boolean validateVoter(String username) {
        return !votedUsers.containsKey(username);
    }

    // Cast vote for a candidate
    @Override
    public boolean castVote(String username, String candidate) {
        if (validateVoter(username)) {
            if (voteTally.containsKey(candidate)) {
                voteTally.put(candidate, voteTally.get(candidate) + 1);
                votedUsers.put(username, true); // Mark user as voted
                return true;
            }
            System.out.println("Invalid candidate name.");
            return false;
        }
        System.out.println("You have already voted.");
        return false;
    }

    // Get the total votes for each candidate
    public Map<String, Integer> getVoteTally() {
        return voteTally;
    }
}