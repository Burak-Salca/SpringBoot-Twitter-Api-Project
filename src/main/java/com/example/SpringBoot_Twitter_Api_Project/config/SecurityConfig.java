package com.example.SpringBoot_Twitter_Api_Project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
        return http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    // Giriş yapmaya gerek olmayan endpointler
                    auth.requestMatchers("/user/register", "/user/login").permitAll(); // /register ve /login herkese açık

                    // Tweetleri listeleme ve detaylarını görmek herkese açık
                    auth.requestMatchers(HttpMethod.GET, "/tweet").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/tweet/findById").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/tweet/findByUserId").permitAll();

                    // Yorumları ve beğenileri görmek herkese açık
                    auth.requestMatchers(HttpMethod.GET, "/comment").permitAll();
                    auth.requestMatchers(HttpMethod.GET, "/like").permitAll();

                    // Tweet oluşturma, güncelleme, silme gibi işlemler giriş gerektirir
                    auth.requestMatchers(HttpMethod.POST, "/tweet").authenticated();
                    auth.requestMatchers(HttpMethod.PUT, "/tweet/{id}").authenticated();
                    auth.requestMatchers(HttpMethod.DELETE, "/tweet/{id}").authenticated();

                    // Yorum yapma, güncelleme, silme işlemleri giriş gerektirir
                    auth.requestMatchers(HttpMethod.POST, "/comment").authenticated();
                    auth.requestMatchers(HttpMethod.PUT, "/comment/{id}").authenticated();
                    auth.requestMatchers(HttpMethod.DELETE, "/comment/{id}").authenticated();

                    // Beğeni ekleme ve kaldırma işlemleri giriş gerektirir
                    auth.requestMatchers(HttpMethod.POST, "/like").authenticated();
                    auth.requestMatchers(HttpMethod.POST, "/dislike").authenticated();

                    // Retweet işlemleri giriş gerektirir
                    auth.requestMatchers(HttpMethod.POST, "/retweet").authenticated();
                    auth.requestMatchers(HttpMethod.DELETE, "/retweet/{id}").authenticated();

                    // Diğer tüm URL'ler kimlik doğrulaması gerektirir
                    auth.anyRequest().authenticated();
                })
                .formLogin(Customizer.withDefaults()) // Eğer form login kullanmayacaksanız
                .httpBasic(Customizer.withDefaults()) // Eğer basic auth kullanmayacaksanız
                .build();
    }
}
