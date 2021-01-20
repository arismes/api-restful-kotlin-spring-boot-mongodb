package com.cursoapikotlin.pontointeligente.repositories

import com.cursoapikotlin.pontointeligente.documents.Empresa
import org.springframework.data.mongodb.repository.MongoRepository

interface EmpresaRepository : MongoRepository<Empresa, String>{
    fun findByCnpj(cnpj: String): Empresa?
}