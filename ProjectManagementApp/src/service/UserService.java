package service;

import model.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private static final String USER_FILE = "ProjectManagementApp/src/data/users.txt";
    private static User currentUser;
    
    public static boolean register(String username, String password, String email) {
        if (isUserExists(username)) {
            return false;
        }
        
        User newUser = new User(username, password, email);
        saveUser(newUser);
        return true;
    }
    
    public static boolean login(String username, String password) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }
    
    public static void logout() {
        currentUser = null;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    private static boolean isUserExists(String username) {
        List<User> users = loadUsers();
        return users.stream().anyMatch(u -> u.getUsername().equals(username));
    }
    
    private static void saveUser(User user) {
        try (FileWriter fw = new FileWriter(USER_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(user.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USER_FILE);
        
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3) {
                    User user = new User(data[0], data[1], data[2]);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }
}
