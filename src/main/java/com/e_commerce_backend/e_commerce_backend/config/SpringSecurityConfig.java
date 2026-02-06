package com.e_commerce_backend.e_commerce_backend.config;

import com.e_commerce_backend.e_commerce_backend.Utility.AuthEntryPointJwt;
import com.e_commerce_backend.e_commerce_backend.Utility.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtAuthFilter jwtAuthFilter;

    public SpringSecurityConfig(AuthEntryPointJwt unauthorizedHandler,
                                JwtAuthFilter jwtAuthFilter) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler)
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/login",
                                "/sign-up",
                                "/api/public/**",

                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers("/user").hasRole("USER")
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}



//package com.e_commerce_backend.e_commerce_backend.config;
//
//import com.e_commerce_backend.e_commerce_backend.Utility.AuthEntryPointJwt;
//import com.e_commerce_backend.e_commerce_backend.Utility.JwtAuthFilter;
//import jakarta.servlet.Filter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig {
//
//    private final AuthEntryPointJwt unauthorizedHandler;
//
//    private final JwtAuthFilter jwtAuthFilter;
//
//    public SpringSecurityConfig(AuthEntryPointJwt unauthorizedHandler, JwtAuthFilter jwtAuthFilter) {
//        this.unauthorizedHandler = unauthorizedHandler;
//        this.jwtAuthFilter = jwtAuthFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // Disable CSRF for stateless APIs
//                .csrf(csrf -> csrf.disable())
//
//                // Authorization rules
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login" ,"/sign-up","/api/admin/**","/api/public/**","/api/v1/**",
//                                "/swagger-ui/**",
//                                "/swagger-ui.html",
//                                "/v3/api-docs/**",
//                                "/v2/api-docs/**",
//                                "/v1/api-docs/**",
//                                "/swagger-resources/**",
//                                "/webjars/**").permitAll()
//
//                       // .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user").hasRole("USER")
//                        .anyRequest().authenticated()
//                )
//
//                // Enable HTTP Basic authentication
//                .httpBasic(Customizer.withDefaults())
//
//                // Make session stateless (good for REST APIs)
//                .sessionManagement(session ->
//                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//
//
//                // Handle unauthorized access using our AuthEntryPointJwt
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(unauthorizedHandler))
//
//
//                .addFilterBefore( jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    // Password Encoder Bean
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//////    In Memory Autheticator
////
////
////    @Bean
////    UserDetailsService userDetailsService (){
////        UserDetails user1= User.withUsername("admin")
////                .password(passwordEncoder().encode("admin"))
////                .roles("ADMIN")
////                .build();
////
////        UserDetails user2= User.withUsername("user")
////                .password(passwordEncoder().encode("User"))
////                .roles("USER")
////                .build();
////        return new InMemoryUserDetailsManager(user1,user2);
////    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//
//
//
//
//}
