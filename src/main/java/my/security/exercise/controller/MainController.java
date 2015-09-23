package my.security.exercise.controller;

import my.security.exercise.MyUserDetailsManager;
import my.security.exercise.model.AuthenticationAttempt;
import my.security.exercise.model.Registration;
import my.security.exercise.repository.AuthenticationAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {

    @Autowired
    private MyUserDetailsManager userDetailsManager;

    @Autowired
    private AuthenticationAttemptRepository repository;

    @RequestMapping(method=RequestMethod.GET, value={"/login"})
    public String login() {
        return "login";
    }

    @RequestMapping(method=RequestMethod.GET, value={"/","/index.html"})
    public String index(Map<String, Object> model, Principal principal) {
        String username = principal.getName();
        List<AuthenticationAttempt> attempts = lastFiveSuccessfulAttempts(username);
        model.put("authAttempts", attempts);
        model.put("principal", principal);
        model.put("isAdminRole", isAdminRole(principal));
        return "index";
    }

    @RequestMapping(method=RequestMethod.GET, value={"/register"})
    public String register( Map<String, Object> model) {
        model.put("registration", new Registration());
        return "register";
    }

    @RequestMapping(method=RequestMethod.POST, value={"/register"})
    public String register(@Valid Registration registration, BindingResult bindingResult) {
        String username = registration.getUsername();
        boolean isUserExists = userDetailsManager.userExists(username);
        if(isUserExists) {
            bindingResult.addError(new FieldError("registration", "username", "User already exists."));
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userDetailsManager.createUser(registration);
        return "redirect:/register?success";
    }

    @RequestMapping(method=RequestMethod.GET, value={"/manage"})
    public String manage(Map<String, Object> model) {
        model.put("users", userDetailsManager.listAllUsersWithRole(MyUserDetailsManager.USER_ROLE));
        return "manage";
    }

    @RequestMapping(method=RequestMethod.POST, value={"/manage"})
    @Transactional
    public String manage(@RequestParam String username) {
        Assert.notNull(username);
        boolean isUserExists = userDetailsManager.userExists(username);
        if(isUserExists) {
            userDetailsManager.deleteUser(username);
            repository.deleteByUsername(username);
        }
        return "redirect:/manage";
    }

    private List<AuthenticationAttempt> lastFiveSuccessfulAttempts(String username) {
        PageRequest pageRequest = new PageRequest(0, 5, Sort.Direction.DESC, "createdAt");
        Page<AuthenticationAttempt> searchResult = repository.findByUsernameAndAuthAttemptResult(username, AuthenticationAttempt.AuthAttemptResult.SUCCESS, pageRequest);
        return searchResult.getContent();
    }

    private boolean isAdminRole(Principal principal) {
        Authentication user = (Authentication) principal;
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        for (GrantedAuthority authority: authorities) {
            String role = authority.getAuthority();
            if(MyUserDetailsManager.SUPER_ADMIN_ROLE.equals(role)) {
                return true;
            }
        }
        return false;
    }

}
