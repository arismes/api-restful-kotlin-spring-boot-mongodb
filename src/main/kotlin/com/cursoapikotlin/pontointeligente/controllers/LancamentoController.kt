package com.cursoapikotlin.pontointeligente.controllers

import com.cursoapikotlin.pontointeligente.documents.Funcionario
import com.cursoapikotlin.pontointeligente.documents.Lancamento
import com.cursoapikotlin.pontointeligente.dtos.LancamentoDto
import com.cursoapikotlin.pontointeligente.enums.TipoEnum
import com.cursoapikotlin.pontointeligente.response.Response
import com.cursoapikotlin.pontointeligente.services.FuncionarioService
import com.cursoapikotlin.pontointeligente.services.LancamentoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.validation.BindingResult
import org.springframework.http.ResponseEntity
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import javax.validation.Valid

@RestController
@RequestMapping("/api/lancamentos")
class LancamentoController(val lancamentoService: LancamentoService,
                           val funcionarioService: FuncionarioService) {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Value("\${paginacao.qtd_por_pagina}")
    val qtdPorPaginas: Int = 15

    @PostMapping
    fun adicionar(@Valid @RequestBody lancamentoDto: LancamentoDto,
                  result: BindingResult): ResponseEntity<Response<LancamentoDto>>{

        val response: Response<LancamentoDto> = Response<LancamentoDto>()
        validarFuncionario(lancamentoDto, result)

        if (result.hasErrors()){
            result.allErrors.forEach {erro -> erro.defaultMessage?.let { response.erros.add(it) }}
            return ResponseEntity.badRequest().body(response)
        }

        val lancamento: Lancamento = converterDtoParaLancamento(lancamentoDto,result)
        lancamentoService.persistir(lancamento)
        response.data = converterLancamentoDTO(lancamento)
        return ResponseEntity.ok(response)
    }

    private fun converterLancamentoDTO(lancamento: Lancamento): LancamentoDto =
        LancamentoDto(dateFormat.format(lancamento.data),lancamento.tipo.toString(),
            lancamento.descricao, lancamento.localizacao,
            lancamento.funcionarioId, lancamento.id)

    private fun converterDtoParaLancamento(lancamentoDto: LancamentoDto, result: BindingResult): Lancamento {
        if (lancamentoDto.id != null){
            val lanc: Lancamento? = lancamentoService.buscarPorId(lancamentoDto.id!!)
            if (lanc == null) result.addError(ObjectError("lancamento","Lançamento não encontrado"))
        }
        return Lancamento(dateFormat.parse(lancamentoDto.data),TipoEnum.valueOf(lancamentoDto.tipo!!),
            lancamentoDto.funcionarioId!!, lancamentoDto.descricao,
            lancamentoDto.localizacao, lancamentoDto.id)
    }

    private fun validarFuncionario(lancamentoDto: LancamentoDto, result: BindingResult) {
        if (lancamentoDto.funcionarioId == null) {
            result.addError(ObjectError("funcionario", "Funcionário não informado."))
            return
        }
        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDto.funcionarioId)
        if (funcionario == null){
            result.addError(ObjectError("funcionario","Funcionário não encontrado. ID inexistente."))
        }
    }
}