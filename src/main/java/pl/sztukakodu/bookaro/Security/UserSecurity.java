package pl.sztukakodu.bookaro.Security;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {
    public boolean isOwnerOrAdmin(String objectOwner, User user){
        return isAdmin(user) || isOwner(objectOwner, user);
    }

    private boolean isOwner(String objectOwner,User user) {
        return user.getUsername().equalsIgnoreCase(objectOwner);
    }

    private boolean isAdmin(User user) {
        return user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
    }
}
