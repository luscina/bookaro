package pl.sztukakodu.bookaro.users.web.application;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.sztukakodu.bookaro.user.domain.UserEntity;
import pl.sztukakodu.bookaro.user.domain.UserEntityRepository;
import pl.sztukakodu.bookaro.users.web.application.port.UserRegistrationUseCase;

@Service
@AllArgsConstructor
public class UserRegistrationService implements UserRegistrationUseCase {
    private final UserEntityRepository repository;
    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {
        if(repository.findByUsernameIgnoreCase(username).isPresent()) {
           return RegisterResponse.failure("User already exist");
        }
        UserEntity entity = new UserEntity(username, password);
        return RegisterResponse.success(repository.save(entity));
    }
}
