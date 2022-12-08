package com.itmo.se.recommendationservice.config;

import com.itmo.se.recommendationservice.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter requestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/recommendations/personal/**").permitAll()
                .antMatchers(HttpMethod.POST, "/recommendations/personal/**/items/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/recommendations/personal/**/items/**").permitAll()
                .antMatchers(HttpMethod.GET, "/recommendations/trending/items").permitAll()
                .antMatchers(HttpMethod.GET, "/recommendations/trending/items/**").permitAll()
                .antMatchers(HttpMethod.POST, "/trending").permitAll()
                .antMatchers(HttpMethod.POST, "/trending/items/**").permitAll()
                .antMatchers(HttpMethod.GET, "/trending/items").permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable()
                .and()
                .addFilterAfter(requestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//     пока не знаю зачем это
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("*")
//                        .allowedMethods("*")
//                        .allowedHeaders("*")
//                        .maxAge(3600);
//            }
//        };
//    }
}