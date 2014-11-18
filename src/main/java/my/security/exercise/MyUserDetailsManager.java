package my.security.exercise;

import my.security.exercise.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * Created by attakorn on 2014-10-17.
 */
public class MyUserDetailsManager extends JdbcUserDetailsManager {

    public static final String SUPER_ADMIN_ROLE = "SUPER";
    public static final String USER_ROLE = "USER";

    public static final String DEF_USERS_WITH_AUTHORITY_SQL = "select distinct username FROM authorities where authority = ?";

    private String usersWithAuthoritySql = DEF_USERS_WITH_AUTHORITY_SQL;

    @Value("${default.super.admin.user}")
    private String defaultAdminUsername;

    @Value("${default.super.admin.password}")
    private String defaultAdminPassword;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public MyUserDetailsManager(DataSource dataSource) {
        setDataSource(dataSource);
    }

    public void init() {
        if(!userExists(defaultAdminUsername)) {
            Registration registration = new Registration(defaultAdminUsername, defaultAdminPassword);
            createUser(createUserDetails(registration, SUPER_ADMIN_ROLE));
        }
    }

    public void createUser(Registration registration) {
        createUser(createUserDetails(registration, USER_ROLE));
    }

    public String[] listAllUsersWithRole(String userRole) {
        Assert.hasText(userRole);
        return (String[]) getJdbcTemplate().queryForList(usersWithAuthoritySql, new String[] {userRole}, String.class).toArray(new String[0]);
    }

    private UserDetails createUserDetails(Registration registration, String userRole) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));
        return new User(registration.getUsername(), passwordEncoder.encode(registration.getPassword()), authorities);
    }

}
