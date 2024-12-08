package UserPackage;

import java.util.ArrayList;
import java.util.List;

public class LoginManager {
    private List<User> users;

    public LoginManager() {
        this.users = new ArrayList<>();
        users.add(new User("sachin", "2341551", "sachin@gmail.com"));
        users.add(new User("surya", "2341566", "surya@gmail.com"));
    }

    public boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}