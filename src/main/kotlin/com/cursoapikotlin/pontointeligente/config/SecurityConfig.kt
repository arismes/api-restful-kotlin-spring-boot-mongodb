package com.cursoapikotlin.pontointeligente.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {


    //Porque desabilitar?   https://pt.wikipedia.org/wiki/Cross-site_request_forgery
    //Para API não tem formulário, mas mesmo assim é utilizado o csrf, para API podemos desativar, visto que não tem formulário
    @Throws(java.lang.Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
    }

}