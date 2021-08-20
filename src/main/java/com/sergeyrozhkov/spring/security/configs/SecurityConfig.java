package com.sergeyrozhkov.spring.security.configs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/profile/**").authenticated()
                .and()
                //.httpBasic() // выводит стандартную спринговую форму аутентификации для неаутентифицированных пользователей
                .formLogin() // можно указть параметры для кастмной формы логина, если ничего не указывать сгенерится стандартная спринговая форма
                .and()
                .logout().logoutSuccessUrl("/");  // можно настроить чтобы после логаута выбрасывало не на страницу логаута а куда-то еще, например в корень
    }
}
