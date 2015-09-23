package my.security.exercise.config;

import my.security.exercise.MyUserDetailsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource datasource;

    @Autowired
    private UserDetailsManager userDetailsManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationEventPublisher authEventHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/register").permitAll()
                .antMatchers("/manage").hasAuthority(MyUserDetailsManager.SUPER_ADMIN_ROLE)
                .anyRequest().fullyAuthenticated()
                .and().formLogin().loginPage("/login").failureUrl("/login?error").permitAll()
                .and().logout().permitAll();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager).passwordEncoder(passwordEncoder);
        auth.jdbcAuthentication().dataSource(datasource);
        auth.authenticationEventPublisher(authEventHandler);
    }

}
