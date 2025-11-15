
package uz.pdp.todo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.todo.enums.Role;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthUser {
    private Long id;
    private String username;
    private String password;
    private Role role = Role.USER;
    private boolean isBlocked;
}