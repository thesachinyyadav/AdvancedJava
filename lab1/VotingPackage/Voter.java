package VotingPackage;

public interface Voter {
    boolean validateVoter(String username);
    boolean castVote(String username, String candidate);
}