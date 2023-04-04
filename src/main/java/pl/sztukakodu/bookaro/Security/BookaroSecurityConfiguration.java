package pl.sztukakodu.bookaro.Security;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.sztukakodu.bookaro.user.domain.UserEntityRepository;

import java.util.List;

@Configuration
@EnableGlobalAuthentication
@AllArgsConstructor
@EnableConfigurationProperties(AdminConfig.class)
public class BookaroSecurityConfiguration {

    private final UserEntityRepository userEntityRepository;
    private final AdminConfig adminConfig;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/catalog/**", "/uploads/**", "/authors/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/orders/**", "/login", "/users").permitAll()
                .anyRequest()
                .authenticated()
        .and()
                .httpBasic()
        .and().addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @SneakyThrows
    private JsonUsernameAuthenticationFilter authenticationFilter() {
        JsonUsernameAuthenticationFilter filter = new JsonUsernameAuthenticationFilter();
        return filter;
    }
    @Bean
    AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        BookaroUserDetailService detailService = new BookaroUserDetailService(userEntityRepository, adminConfig);
        provider.setUserDetailsService(detailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    User systemUser() {
        return new User("systemUser", "", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}
