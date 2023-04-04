package pl.sztukakodu.bookaro.users.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sztukakodu.bookaro.users.web.application.port.UserRegistrationUseCase;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {

    private final UserRegistrationUseCase registrationUseCase;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command){
        return registrationUseCase.register(command.username, command.password)
                .handle(
                        entity -> ResponseEntity.accepted().build(),
                        error -> ResponseEntity.badRequest().body(error)
                );
    }

    @Data
    static class RegisterCommand {
        @Email
        String username;
        @Size(min = 3, max =100)
        String password;
    }
}
