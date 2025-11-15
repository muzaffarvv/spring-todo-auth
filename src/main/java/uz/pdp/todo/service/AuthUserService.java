package uz.pdp.todo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.todo.dao.AuthUserDao;
import uz.pdp.todo.dto.UserRegisterDTO;
import uz.pdp.todo.enums.Role;
import uz.pdp.todo.model.AuthUser;

import java.util.List;
import java.util.Optional;

@Service
public class AuthUserService {

    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;

    public AuthUserService(AuthUserDao authUserDao, PasswordEncoder passwordEncoder) {
        this.authUserDao = authUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    public Long add(AuthUser authUser) {
        String encodedPassword = passwordEncoder.encode(authUser.getPassword());
        authUser.setPassword(encodedPassword);
        return authUserDao.save(authUser);
    }

    public Optional<AuthUser> getByUsername(String username) {
        return authUserDao.findByUsername(username);
    }

    public List<AuthUser> getAll() {
        return authUserDao.getAll();
    }

    public void delete(Long id) {
        authUserDao.delete(id);
    }

    public boolean update(Long id, AuthUser updatedUser) {
        return authUserDao.update(id, updatedUser) > 0;
    }

    public void changeRole(Long id, Role newRole) {
        authUserDao.changeRole(id, newRole);
    }

    public void toggleBlockStatus(Long id) {
        authUserDao.toggleBlockStatus(id);
    }

    public boolean existsByUsername(String username) {
        return authUserDao.findByUsername(username).isPresent();
    }

    public boolean login(String username, String password) {
        return authUserDao.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    public void register(UserRegisterDTO dto) {
        if (existsByUsername(dto.username())) {
            throw new IllegalArgumentException("Username already taken!");
        }

        AuthUser authUser = AuthUser.builder()
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .role(Role.USER)
                .build();

        authUserDao.save(authUser);
    }


}
