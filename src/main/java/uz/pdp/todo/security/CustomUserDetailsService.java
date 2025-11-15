package uz.pdp.todo.security;

import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.todo.dao.AuthUserDao;
import uz.pdp.todo.model.AuthUser;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserDao dao;

    public CustomUserDetailsService(AuthUserDao dao) {
        this.dao = dao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = dao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "user not found by username: " + username));

        if (authUser.isBlocked()) {
            throw new LockedException("User is blocked: " + username);
        }

        return User.withUsername(authUser.getUsername())
                .password(authUser.getPassword())
                .roles(authUser.getRole().name())
                .build();
    }
}
