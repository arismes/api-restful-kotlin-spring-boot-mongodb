package com.cursoapikotlin.pontointeligente.services.impl

import com.cursoapikotlin.pontointeligente.documents.Lancamento
import com.cursoapikotlin.pontointeligente.repositories.LancamentoRepository
import com.cursoapikotlin.pontointeligente.services.LancamentoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class LancamentoServiceImpl(val lancamentoRepository: LancamentoRepository) : LancamentoService {
    override fun buscarPorFuncionarioId(funcionarioId: String, pageRequest: PageRequest): Page<Lancamento> =
        lancamentoRepository.findByFuncionarioId(funcionarioId,pageRequest)

    override fun buscarPorId(id: String): Lancamento? = lancamentoRepository.findById(id).get()

    override fun persistir(lancamento: Lancamento): Lancamento? = lancamentoRepository.save(lancamento)

    override fun remover(id: String) = lancamentoRepository.deleteById(id)
}