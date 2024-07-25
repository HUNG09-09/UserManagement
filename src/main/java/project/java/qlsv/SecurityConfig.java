package project.java.qlsv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtTokenFilter jwtTokenFilter;

    // cau hinh security xac thuc
    @Autowired
    public void config(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    //để xác thực (authenticate) các yêu cầu đến từ người dùng.
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return  authenticationConfiguration.getAuthenticationManager();
    }

    // cau hinh phan quyen , pthuc xac thuc
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/admin/**","/cache/**")
                .hasAnyAuthority("ADMIN")
                .requestMatchers("/member/**").authenticated()
                .anyRequest().permitAll());
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER));
        http.httpBasic(withDefaults());
        http.csrf(csrf -> csrf.disable());

        // chen filter truoc filter login cua spring security
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


}
