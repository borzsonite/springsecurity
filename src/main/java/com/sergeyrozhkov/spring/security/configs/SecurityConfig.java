package com.sergeyrozhkov.spring.security.configs;

import com.sergeyrozhkov.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/read_profile").hasAuthority("READ_PROFILE")
                .antMatchers("/only_for_admins").hasRole("ADMIN")
                .and()
                //.httpBasic() // выводит стандартную спринговую форму аутентификации для неаутентифицированных пользователей
                .formLogin() // можно указть параметры для кастмной формы логина, если ничего не указывать сгенерится стандартная спринговая форма
                .loginProcessingUrl("/hellologin")// можно указать свой адрес вместо стандартного /login на который будет отправлятся форма
                .and()
                .logout().logoutSuccessUrl("/");  // можно настроить чтобы после логаута выбрасывало не на страницу логаута а куда-то еще, например в корень
    }

    ////////////////////////////////////inMemory authentication///////////////////////
//    @Bean
//    public UserDetailsService users() {
//        UserDetails user = User.builder() // UserDetails - это минимальная информация о пользователях: логин, пароль, роли
//                .username("user")
//                .password("{bcrypt}$2a$12$uvpIbPaj1DTUOwDTpFbn3eHnz3z7EHP.reyvqLYfYZ/fUj3ChDHgm")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$uvpIbPaj1DTUOwDTpFbn3eHnz3z7EHP.reyvqLYfYZ/fUj3ChDHgm")
//                .roles("ADMIN", "USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    //////////////////////////jdbc authentication////////////////////////
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource) {
////        UserDetails user = User.builder()
////                .username("user")
////                .password("{bcrypt}$2a$12$uvpIbPaj1DTUOwDTpFbn3eHnz3z7EHP.reyvqLYfYZ/fUj3ChDHgm")
////                .roles("USER")
////                .build();
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password("{bcrypt}$2a$12$uvpIbPaj1DTUOwDTpFbn3eHnz3z7EHP.reyvqLYfYZ/fUj3ChDHgm")
////                .roles("ADMIN", "USER")
////                .build();
//
//        JdbcUserDetailsManager users  = new JdbcUserDetailsManager(dataSource);
////        if(users.userExists(user.getUsername())) {
////            users.deleteUser(user.getUsername());
////        }
////        if(users.userExists(admin.getUsername())) {
////            users.deleteUser(admin.getUsername());
////        }
////        users.createUser(user);
////        users.createUser(admin);
//        return users;
//    }

    ////////////Dao authentication////////////////
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        //Роль daoAuthenticationProvider по получаемым логину и паролю определить существует ли такой пользователь и если существует положить его в springSecurityContext
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);

        return authenticationProvider;
    }
}
