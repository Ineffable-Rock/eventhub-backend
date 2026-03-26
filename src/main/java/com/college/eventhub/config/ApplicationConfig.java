package com.college.eventhub.config;

import com.college.eventhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    //Why use it? Spring Security is "dumb."
    //It doesn't know you are using a Database, or Postgres, or that your table is called _user. It just needs a UserDetails object
    //You are teaching Spring: "Hey, whenever you need to find a user, run this specific SQL query using my Repository."
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService configUserDetailsService,
            PasswordEncoder configPasswordEncoder
    ) {
        // 1. Pass UserDetailsService in the constructor (Required by your version)
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(configUserDetailsService);

        // 2. Pass PasswordEncoder via setter (As seen on line 110 of your file)
        authProvider.setPasswordEncoder(configPasswordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}