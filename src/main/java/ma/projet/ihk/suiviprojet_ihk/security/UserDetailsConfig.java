package ma.projet.ihk.suiviprojet_ihk.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserDetailsConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails secretaire = User.builder()
                .username("secretaire")
                .password(passwordEncoder.encode("secretaire123"))
                .roles("SECRETAIRE")
                .build();

        UserDetails directeur = User.builder()
                .username("directeur")
                .password(passwordEncoder.encode("directeur123"))
                .roles("DIRECTEUR")
                .build();

        UserDetails chefProjet = User.builder()
                .username("chef")
                .password(passwordEncoder.encode("chef123"))
                .roles("CHEF_PROJET")
                .build();

        UserDetails comptable = User.builder()
                .username("comptable")
                .password(passwordEncoder.encode("comptable123"))
                .roles("COMPTABLE")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(secretaire, directeur, chefProjet, comptable, admin);
    }
}