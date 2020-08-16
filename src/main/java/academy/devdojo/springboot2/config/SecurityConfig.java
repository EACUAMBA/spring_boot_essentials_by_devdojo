package academy.devdojo.springboot2.config;

import academy.devdojo.springboot2.service.DevDojoUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;

@EnableWebSecurity
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DevDojoUserService devDojoUserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .disable()
//                .and()
                .authorizeRequests()
                .antMatchers("/animes/admin/**").hasRole("ADMIN")
                .antMatchers("/animes/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        log.info("The encoded password result is: {}", passwordEncoder.encode("12345"));
        //@formatter:off
        auth.inMemoryAuthentication()
                .withUser("eac")
                .password(passwordEncoder.encode("12345"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("devdojo")
                .password(passwordEncoder.encode("12345"))
                .roles("USER");
        //@formatter:on

        auth.userDetailsService(this.devDojoUserService).passwordEncoder(passwordEncoder);
    }
}
