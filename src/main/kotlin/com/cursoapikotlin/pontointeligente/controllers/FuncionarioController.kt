package com.cursoapikotlin.pontointeligente.controllers

import com.cursoapikotlin.pontointeligente.documents.Funcionario
import com.cursoapikotlin.pontointeligente.dtos.FuncionarioDto
import com.cursoapikotlin.pontointeligente.response.Response
import com.cursoapikotlin.pontointeligente.services.FuncionarioService
import com.cursoapikotlin.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/funcionarios")
class FuncionarioController (var funcionarioService: FuncionarioService){

    @PutMapping("/{id}")
    fun atualizar(@PathVariable("id") id: String, @Valid @RequestBody funcionarioDto: FuncionarioDto,
            result: BindingResult): ResponseEntity<Response<FuncionarioDto>>{

        val response: Response<FuncionarioDto> = Response<FuncionarioDto>()
        val funcionario: Funcionario? = funcionarioService.buscarPorId(id)

        if (funcionario == null){
            result.addError(ObjectError("funcionario","Funcionário não encontrado."))
        }

        if (result.hasErrors()) {
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val funcAtualizar: Funcionario = atualizarDadosFuncionarios(funcionario!!,funcionarioDto)
        funcionarioService.persistir(funcAtualizar)
        response.data = converterFuncionarioDto(funcAtualizar)
        return ResponseEntity.ok(response)
    }


    private fun atualizarDadosFuncionarios(funcionario: Funcionario, funcionarioDto: FuncionarioDto): Funcionario {
        var senha: String
        if (funcionarioDto.senha == null){
            senha = funcionario.senha
        }else{
            senha = SenhaUtils().gerarBcrypt(funcionarioDto.senha)
        }

        return Funcionario(funcionarioDto.nome, funcionarioDto.email, senha,
            funcionario.cpf,funcionario.perfil,funcionario.empresaId,
            funcionarioDto.valorHora?.toDouble(),
            funcionarioDto.qtdHorasTrabalhoDia?.toFloat(),
            funcionarioDto.qtdHorasAlmoco?.toFloat(),
            funcionario.id)
    }

    private fun converterFuncionarioDto(funcionario: Funcionario): FuncionarioDto = FuncionarioDto(
        funcionario.nome, funcionario.email, "",
        funcionario.valorHora.toString(),
        funcionario.qtdHorasTrabalhadasDia.toString(),
        funcionario.qtdHorasAlmoco.toString(),
        funcionario.id)
}