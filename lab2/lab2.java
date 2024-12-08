import java.util.Scanner;

class VoteCounter {
    private int votes = 0;
    private boolean votingOpen = true;

    public synchronized void castVote(String voterName) throws InterruptedException {
        if (!votingOpen) {
            throw new IllegalStateException("Voting is closed!");
        }
        votes++;
        System.out.println("✅ " + voterName + " successfully cast their vote!");
        notify();
    }

    public synchronized void countVotes() throws InterruptedException {
        while (votes == 0 && votingOpen) {
            wait();
        }
        System.out.println("📊 Total votes counted so far: " + votes);
    }

    public synchronized void closeVoting() {
        votingOpen = false;
        notifyAll();
        System.out.println("\n⚠️ Voting has been officially closed!");
    }

    public synchronized int getTotalVotes() {
        return votes;
    }

    public synchronized boolean isVotingOpen() {
        return votingOpen;
    }
}

class Voter implements Runnable {
    private VoteCounter voteCounter;
    private String voterName;

    public Voter(VoteCounter voteCounter, String voterName) {
        this.voteCounter = voteCounter;
        this.voterName = voterName;
    }

    @Override
    public void run() {
        try {
            voteCounter.castVote(voterName);
            Thread.sleep(200);
        } catch (IllegalStateException | InterruptedException e) {
            System.err.println("❌ " + voterName + " failed to cast their vote: " + e.getMessage());
        }
    }
}

class VoteCounterThread extends Thread {
    private VoteCounter voteCounter;

    public VoteCounterThread(VoteCounter voteCounter) {
        this.voteCounter = voteCounter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                voteCounter.countVotes();
                Thread.sleep(300);
                if (!voteCounter.isVotingOpen()) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.err.println("❌ Vote counter thread interrupted: " + e.getMessage());
        }
    }
}

public class DigitalVotingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VoteCounter voteCounter = new VoteCounter();

        System.out.println("🗳️ Welcome to the Digital Voting System");
        System.out.println("======================================");
        System.out.println("🔹 Voting will remain open for a short period.");
        System.out.println("🔹 Voters can cast their votes simultaneously.\n");

        VoteCounterThread counterThread = new VoteCounterThread(voteCounter);
        counterThread.start();

        System.out.print("Enter the number of voters participating: ");
        int numberOfVoters = scanner.nextInt();
        scanner.nextLine();

        Thread[] voterThreads = new Thread[numberOfVoters];
        for (int i = 0; i < numberOfVoters; i++) {
            System.out.print("Enter the name of Voter-" + (i + 1) + ": ");
            String voterName = scanner.nextLine();
            voterThreads[i] = new Thread(new Voter(voteCounter, voterName));
            voterThreads[i].start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.err.println("❌ Main thread interrupted: " + e.getMessage());
        }

        voteCounter.closeVoting();

        for (Thread voter : voterThreads) {
            try {
                voter.join();
            } catch (InterruptedException e) {
                System.err.println("❌ Failed to join voter thread: " + e.getMessage());
            }
        }

        try {
            counterThread.join();
        } catch (InterruptedException e) {
            System.err.println("❌ Failed to join vote counter thread: " + e.getMessage());
        }

        System.out.println("\n🎉 Voting has concluded successfully!");
        System.out.println("======================================");
        System.out.println("🏆 Total votes counted: " + voteCounter.getTotalVotes());
        System.out.println("Thank you for participating in the digital voting system!");
    }
}
