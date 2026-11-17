package com.ziguinchor.patrimoine.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/", "/sites", "/sites/{id:[0-9]+}", "/categories",
                        "/visites", "/circuits", "/microservices", "/reservations/billet/**",
                        "/login", "/register", "/css/**", "/images/**",
                        "/h2-console/**"
                ).permitAll()

                .requestMatchers(
                        "/sites/nouveau", "/sites/enregistrer",
                        "/sites/modifier/**", "/sites/supprimer/**",
                        "/sites/*/horaires",
                        "/categories/nouveau", "/categories/enregistrer",
                        "/categories/modifier/**", "/categories/supprimer/**"
                ).hasAnyRole("CONSERVATEUR", "ADMINISTRATEUR_COMMUNAL", "SUPER_ADMIN")

                .requestMatchers(
                        "/circuits/nouveau", "/circuits/enregistrer", "/circuits/mes-circuits",
                        "/circuits/publier/**", "/circuits/supprimer/**",
                        "/visites/planifier", "/visites/enregistrer", "/visites/supprimer/**"
                ).hasAnyRole("OPERATEUR_TOURISTIQUE", "CONSERVATEUR", "ADMINISTRATEUR_COMMUNAL", "SUPER_ADMIN")

                .requestMatchers("/guide/**").hasAnyRole("GUIDE", "ADMINISTRATEUR_COMMUNAL", "SUPER_ADMIN")

                .requestMatchers("/avis/*/repondre").hasAnyRole("GUIDE", "CONSERVATEUR", "ADMINISTRATEUR_COMMUNAL", "SUPER_ADMIN")
                .requestMatchers("/avis/*/signaler").authenticated()

                .requestMatchers("/admin/utilisateurs/**").hasRole("SUPER_ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMINISTRATEUR_COMMUNAL", "SUPER_ADMIN")

                .requestMatchers("/reservations/**", "/sites/*/avis").hasAnyRole("VISITEUR", "SUPER_ADMIN")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
}
