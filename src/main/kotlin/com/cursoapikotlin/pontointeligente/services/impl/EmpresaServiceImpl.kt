package com.cursoapikotlin.pontointeligente.services.impl

import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.repositories.EmpresaRepository
import com.cursoapikotlin.pontointeligente.services.EmpresaService
import org.springframework.stereotype.Service

@Service
class EmpresaServiceImpl(val empresaRepository: EmpresaRepository) : EmpresaService {
    override fun buscarPorCnpj(cnpj: String): Empresa? = empresaRepository.findByCnpj(cnpj)
    override fun persistir(empresa: Empresa): Empresa = empresaRepository.save(empresa)
}