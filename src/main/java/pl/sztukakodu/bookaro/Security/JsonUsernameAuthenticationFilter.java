package pl.sztukakodu.bookaro.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JsonUsernameAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    private final ObjectMapper mapper = new ObjectMapper();
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginCommand loginCommand = mapper.readValue(request.getReader(), LoginCommand.class);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                loginCommand.getUsername(),
                loginCommand.getPassword()
        );
        return this.getAuthenticationManager().authenticate(token);
    }
}
