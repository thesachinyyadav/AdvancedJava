import java.sql.*;

public class Program6 {
    public static void main(String[] args) {
        // JDBC connection parameters
        String url = "jdbc:mysql://localhost:3306/DigitalVotingSystem";
        String username = "root";
        String password = "Surya@12";
        
        // SQL join query: joining Voter and Vote tables
        String sql = "SELECT v.name, vt.vote_date FROM Voter v INNER JOIN Vote vt ON v.voter_id = vt.voter_id";
        
        try {
            // Load JDBC driver (optional for JDBC 4+)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try (Connection conn = DriverManager.getConnection(url, username, password);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                // Process the result set
                while (rs.next()) {
                    String name = rs.getString("name");
                    Date voteDate = rs.getDate("vote_date");
                    System.out.println("Voter: " + name + ", Vote Date: " + voteDate);
                }
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
        }
    }
}
