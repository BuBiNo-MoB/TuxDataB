package com.database.TuxDataB.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig {

    @Bean
    PasswordEncoder stdPasswordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    AuthTokenFilter authenticationJwtToken() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       PasswordEncoder passwordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/distributions/search").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/registerAdmin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/distributions/{id}/like", "/distributions/{id}/unlike").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comments").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comments/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/{id}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comments/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/comments/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/comments/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/distributions").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/distributions/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/distributions/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/distributions/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/distributions/{id}/logo").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/distributions/{id}/logo").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/distributions/{id}/logo").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/distributions/{id}/desktopImage").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/distributions/{id}/desktopImage").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/distributions/{id}/desktopImage").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/distributions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/me").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/{id}/avatar").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}/avatar").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/{id}/avatar").permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authenticationJwtToken(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JavaMailSenderImpl getJavaMailSender(@Value("${gmail.mail.transport.protocol}" )String protocol,
                                                @Value("${gmail.mail.smtp.auth}" ) String auth,
                                                @Value("${gmail.mail.smtp.starttls.enable}" )String starttls,
                                                @Value("${gmail.mail.debug}" )String debug,
                                                @Value("${gmail.mail.from}" )String from,
                                                @Value("${gmail.mail.from.password}" )String password,
                                                @Value("${gmail.smtp.ssl.enable}" )String ssl,
                                                @Value("${gmail.smtp.host}" )String host,
                                                @Value("${gmail.smtp.port}" )String port){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));

        mailSender.setUsername(from);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);
        props.put("smtp.ssl.enable",ssl);

        return mailSender;
    }

}
