package com.cursoapikotlin.pontointeligente.controllers

import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.documents.Funcionario
import com.cursoapikotlin.pontointeligente.dtos.CadastroPFDto
import com.cursoapikotlin.pontointeligente.dtos.CadastroPJDto
import com.cursoapikotlin.pontointeligente.enums.PerfilEnum
import com.cursoapikotlin.pontointeligente.response.Response
import com.cursoapikotlin.pontointeligente.services.EmpresaService
import com.cursoapikotlin.pontointeligente.services.FuncionarioService
import com.cursoapikotlin.pontointeligente.utils.SenhaUtils
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/cadastrar-pf")
class CadastroPFController (val empresaService: EmpresaService,
                            val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPFDto: CadastroPFDto,
                  result: BindingResult): ResponseEntity<Response<CadastroPFDto>>{
        val response: Response<CadastroPFDto> = Response<CadastroPFDto>()

        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPFDto.cnpj)
        validarDadosExistentes(cadastroPFDto,empresa, result)
        if (result.hasErrors()){
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        var funcionario: Funcionario = converterDtoParaFuncionario(cadastroPFDto,empresa!!)
        funcionario = funcionarioService.persistir(funcionario)
        response.data = converterCadastroPFDto(funcionario,empresa!!)
        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistentes(cadastroPfDto: CadastroPFDto, empresa: Empresa?, result: BindingResult) {
        if (empresa == null){
            result.addError(ObjectError("empresa","Empresa não cadastrada."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPfDto.cpf)
        if (funcionarioCpf != null){
            result.addError(ObjectError("funcionario","CPF já existe."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorCpf(cadastroPfDto.email)
        if (funcionarioEmail != null){
            result.addError(ObjectError("funcionario","Email já existe."))
        }
    }

    private fun converterDtoParaFuncionario(cadastroPFDto: CadastroPFDto, empresa: Empresa) =
        Funcionario(cadastroPFDto.nome, cadastroPFDto.email,
            SenhaUtils().gerarBcrypt(cadastroPFDto.senha),cadastroPFDto.cpf,
            PerfilEnum.ROLE_USUARIO, empresa.id.toString(),
            cadastroPFDto.valorHora?.toDouble(),cadastroPFDto.qtdHorasTrabalhoDia?.toFloat(),
            cadastroPFDto.qtdHorasAlmoco?.toFloat(),cadastroPFDto.id)

    private fun converterCadastroPFDto(funcionario: Funcionario, empresa: Empresa): CadastroPFDto =
        CadastroPFDto(funcionario.nome, funcionario.email,"",funcionario.cpf,
            empresa.cnpj,empresa.id.toString(),funcionario.valorHora.toString(),
            funcionario.qtdHorasTrabalhadasDia.toString(),
            funcionario.qtdHorasAlmoco.toString(),
            funcionario.id)
}