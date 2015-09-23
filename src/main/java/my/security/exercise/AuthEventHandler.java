package my.security.exercise;

import my.security.exercise.model.AuthenticationAttempt;
import my.security.exercise.repository.AuthenticationAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;


public class AuthEventHandler implements AuthenticationEventPublisher {

    @Autowired
    private AuthenticationAttemptRepository repository;
    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();
        repository.save(new AuthenticationAttempt(username, AuthenticationAttempt.AuthAttemptResult.SUCCESS));
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException e, Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        repository.save(new AuthenticationAttempt(username, AuthenticationAttempt.AuthAttemptResult.FAILURE));
    }

}
