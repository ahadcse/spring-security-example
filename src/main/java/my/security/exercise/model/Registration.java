package my.security.exercise.model;

import javax.validation.constraints.Pattern;

/**
 * Created by attakorn on 2014-10-17.
 */
public class Registration {

    @Pattern(regexp = "^[a-z0-9_-]{3,15}$", message = "Unsupported username.")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Unsupported password.")
    private String password;

    public Registration() {
    }

    public Registration(String username, String password) {
        this.username = username;
        this.password = password;
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

}
