package my.security.exercise.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by attakorn on 2014-10-18.
 */
@Entity
public class AuthenticationAttempt {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String username;

    private AuthAttemptResult authAttemptResult;

    private Date createdAt;

    public AuthenticationAttempt() {
    }

    public AuthenticationAttempt(String username, AuthAttemptResult authAttemptResult) {
        this.username = username;
        this.authAttemptResult = authAttemptResult;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public AuthAttemptResult getAuthAttemptResult() {
        return authAttemptResult;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    void createdAt() {
        this.createdAt = new Date();
    }

    public static enum AuthAttemptResult {
        SUCCESS, FAILURE
    }

}
