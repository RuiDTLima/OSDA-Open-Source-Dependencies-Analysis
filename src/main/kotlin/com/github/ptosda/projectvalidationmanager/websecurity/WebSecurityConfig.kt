package com.github.ptosda.projectvalidationmanager.websecurity

import com.github.ptosda.projectvalidationmanager.websecurity.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Configuration for authentication
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig(
        val userDetailsService: UserDetailsServiceImpl
) : WebSecurityConfigurerAdapter(){
    @Bean
    fun bCryptPasswordEncoder() = BCryptPasswordEncoder()

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()

    override fun configure(http: HttpSecurity) {
        http
            .csrf()
                .ignoringAntMatchers("/{manager}/dependency/**", "/report", "/register", "/user/token", "/report/dependency/vulnerability/edit", "/projs/{project-id}/user/{username}", "/logout", "/login")
                .and()
            .authorizeRequests()
                .antMatchers("/{manager}/dependency/**", "/report", "/register", "/javascript/**", "/css/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder())
    }
}