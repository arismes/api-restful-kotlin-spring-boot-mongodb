package com.cursoapikotlin.pontointeligente.services

import com.cursoapikotlin.pontointeligente.documents.Empresa

interface EmpresaService {
    fun buscarPorCnpj(cnpj: String): Empresa?

    fun persistir(empresa: Empresa): Empresa
}