package my.security.exercise.config;

import my.security.exercise.AuthEventHandler;
import my.security.exercise.MyUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
public class MainConfig {

    @Autowired
    private DataSource dataSource;

    @Bean(initMethod = "init")
    public MyUserDetailsManager userDetailsManager() {
        return new MyUserDetailsManager(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEventPublisher authEventHandler() {
        return new AuthEventHandler();
    }
}
