package tp1_javafx.tp1_prog2_v2.Models;

import java.io.Serializable;

public class User implements Serializable {
    public String username;
    public String password;
    public Role role;

    /* User constructor */
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public String toString() {
        return this.username + ":" + this.password + ":" + this.role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

