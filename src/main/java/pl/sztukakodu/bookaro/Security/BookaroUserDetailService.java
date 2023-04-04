package pl.sztukakodu.bookaro.Security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.sztukakodu.bookaro.user.domain.UserEntityRepository;

@AllArgsConstructor
public class BookaroUserDetailService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final AdminConfig config;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(config.getUsername().equalsIgnoreCase(username)){
            return config.adminUser();
        }
        return userEntityRepository.findByUsernameIgnoreCase(username)
                .map(UserEntityDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
