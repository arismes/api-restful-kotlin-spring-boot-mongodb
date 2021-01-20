package com.cursoapikotlin.pontointeligente.repositories

import com.cursoapikotlin.pontointeligente.documents.Lancamento
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface LancamentoRepository : MongoRepository<Lancamento,String> {
    fun findByFuncionarioId (funcionarioId: String, pageble: Pageable): Page<Lancamento>
}