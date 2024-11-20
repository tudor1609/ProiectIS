package model.builder;

import model.Role;
import model.User;
import java.util.List;

public class UserBuilder {

    private User user;

    public UserBuilder(){
        user = new User();
    }

    public UserBuilder setId(Long id){
        user.setId(id);
        return this;
    }

    public UserBuilder setUsername(String username){
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty.");
        }
        user.setUsername(username);
        return this;
    }

    public UserBuilder setPassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        user.setPassword(password);
        return this;
    }

    public UserBuilder setRoles(List<Role> roles){
        user.setRoles(roles);
        return this;
    }

    public User build(){
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalStateException("Username must be set before building the User.");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalStateException("Password must be set before building the User.");
        }
        return user;
    }
}
