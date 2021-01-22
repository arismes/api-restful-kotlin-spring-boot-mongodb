package com.cursoapikotlin.pontointeligente.security

import com.cursoapikotlin.pontointeligente.documents.Funcionario
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class FuncionarioPrincipal(val funcionario: Funcionario) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = mutableListOf<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(funcionario.perfil.toString()))
        return authorities
    }

    override fun getPassword(): String = funcionario.senha

    override fun getUsername(): String = funcionario.email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true // Usuário ativo ou não ** Neste caso, sempre ativo

}