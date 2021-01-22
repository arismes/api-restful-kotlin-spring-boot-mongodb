package com.cursoapikotlin.pontointeligente.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity //Habilitando o WebSecurity na aplicação
/*
excluida
class SecurityConfig : WebSecurityConfigurerAdapter() {


    //Porque desabilitar?   https://pt.wikipedia.org/wiki/Cross-site_request_forgery
    //Para API não tem formulário, mas mesmo assim é utilizado o csrf, para API podemos desativar, visto que não tem formulário
    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
    }

}*/
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    val funcionarioDetailsService: FuncionarioDetailsService) : WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider())
    }

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.
        antMatchers("/api/cadastrar-pj", "/api/cadastrar-pf")?.
        permitAll()?.
        anyRequest()?.
        authenticated()?.and()?.
        httpBasic()?.and()?.
        sessionManagement()?.
        sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()?.
        csrf()?.disable()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(funcionarioDetailsService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

}
