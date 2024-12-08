import UserPackage.*;
import VotingPackage.*;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LoginManager loginManager = new LoginManager();
        VotingManager votingManager = new VotingManager();

        System.out.println("Welcome to the Digital Voting System!");
      

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        while(true){
            if (loginManager.authenticate(username, password)) {
                System.out.println("Login successful!");
                System.out.println("Please choose an option:");
                System.out.println("1. Cast Vote");
                System.out.println("2. View Vote Tally");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
    
                if (choice == 1) {
                    System.out.println("Candidates:Sachin, Surya, Arjun");
                    System.out.print("Enter your vote (candidate name): ");
                    String candidate = scanner.nextLine();
    
                    if (votingManager.castVote(username, candidate)) {
                        System.out.println("Your vote has been successfully cast for " + candidate + ".");
                    }
                } else if (choice == 2) {
                    // Display vote tally
                    System.out.println("Vote Tally:");
                    Map<String, Integer> voteTally = votingManager.getVoteTally();
                    for (Map.Entry<String, Integer> entry : voteTally.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue() + " votes");
                    }
                } else {
                    System.out.println("Invalid choice.");
                }
            } else {
                System.out.println("Invalid username or password.");
            }
        }
        scanner.close();
    }
}