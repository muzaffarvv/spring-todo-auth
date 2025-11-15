package uz.pdp.todo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.sql.DataSource;

@Configuration
public class SecurityConfigurer {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticatedFailureHandler customAuthenticatedFailureHandler;
    private final DataSource dataSource;

    public SecurityConfigurer(CustomUserDetailsService customUserDetailsService,
                              CustomAuthenticatedFailureHandler customAuthenticatedFailureHandler, DataSource dataSource) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticatedFailureHandler = customAuthenticatedFailureHandler;
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/auth/login", "/api/**" ));

        http.userDetailsService(customUserDetailsService);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/css/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().fullyAuthenticated()
        );


        http.formLogin(form -> form
                .loginPage("/auth/login")
                .usernameParameter("uname")
                .passwordParameter("pswd")
                .defaultSuccessUrl("/todos", true)
                .failureHandler(customAuthenticatedFailureHandler)
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/auth/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/auth/login?logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
        );

        http.rememberMe(r -> r
                .rememberMeParameter("remember")
                .tokenRepository(persistentTokenRepository(dataSource))
                .tokenValiditySeconds(24 * 60 * 60)
                .userDetailsService(customUserDetailsService)
        );

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

}
