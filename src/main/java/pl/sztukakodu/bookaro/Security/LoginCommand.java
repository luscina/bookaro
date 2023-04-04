package pl.sztukakodu.bookaro.Security;

import lombok.Data;

@Data
public class LoginCommand {
    private String username;
    private String password;
}
