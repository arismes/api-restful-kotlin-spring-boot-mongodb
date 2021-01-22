package com.cursoapikotlin.pontointeligente.controllers

import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.dtos.EmpresaDto
import com.cursoapikotlin.pontointeligente.response.Response
import com.cursoapikotlin.pontointeligente.services.EmpresaService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/empresas")
class EmpresaController(val empresaService: EmpresaService) {

    @GetMapping("/cnpj/{cnpj}")
    fun buscarEmpresaPorCnpj(@PathVariable("cnpj") cnpj: String):ResponseEntity<Response<EmpresaDto>>{
        val response: Response<EmpresaDto> = Response<EmpresaDto>()
        val empresa: Empresa? = empresaService.buscarPorCnpj(cnpj)

        if (empresa == null){
            response.erros.add("Empresa não encontrada para o CNPJ ${cnpj}")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }
        response.data = converterEmpresaDto(empresa)
        return ResponseEntity.ok(response)
    }

    private fun converterEmpresaDto(empresa: Empresa): EmpresaDto? = EmpresaDto(empresa.razaoSocial,empresa.cnpj, empresa.id)
}