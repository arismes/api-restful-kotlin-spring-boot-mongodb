package com.cursoapikotlin.pontointeligente.controllers

import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.documents.Funcionario
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
@RequestMapping("/api/cadastrar-pj")
class CadastroPJController(val empresaService: EmpresaService,
                           val funcionarioService: FuncionarioService) {

    @PostMapping
    fun cadastrar(@Valid @RequestBody cadastroPJDto: CadastroPJDto,
                result: BindingResult): ResponseEntity<Response<CadastroPJDto>>{
        val response: Response<CadastroPJDto> = Response<CadastroPJDto>()

        validarDadosExistentes(cadastroPJDto, result)
        if (result.hasErrors()){
            result.allErrors.forEach { erro -> erro.defaultMessage?.let { response.erros.add(it) } }
            return ResponseEntity.badRequest().body(response)
        }

        val  empresa: Empresa = converterDtoParaEmpresa(cadastroPJDto)
        empresaService.persistir(empresa)

        var funcionario: Funcionario = converterDtoParaFuncionario(cadastroPJDto,empresa)
        funcionario = funcionarioService.persistir(funcionario)
        response.data = converterCadastroPJDto(funcionario,empresa)
        return ResponseEntity.ok(response)
    }

    private fun validarDadosExistentes(cadastroPJDto: CadastroPJDto, result: BindingResult) {
        val empresa: Empresa? = empresaService.buscarPorCnpj(cadastroPJDto.cnpj)
        if (empresa != null){
            result.addError(ObjectError("empresa","Empresa já existe."))
        }

        val funcionarioCpf: Funcionario? = funcionarioService.buscarPorCpf(cadastroPJDto.cpf)
        if (funcionarioCpf != null){
            result.addError(ObjectError("funcionario","CPF já existe."))
        }

        val funcionarioEmail: Funcionario? = funcionarioService.buscarPorCpf(cadastroPJDto.email)
        if (funcionarioEmail != null){
            result.addError(ObjectError("funcionario","Email já existe."))
        }
    }

    private fun converterDtoParaEmpresa(cadastroPJDto: CadastroPJDto): Empresa =
        Empresa(cadastroPJDto.razaoSocial, cadastroPJDto.cnpj)

    private fun converterCadastroPJDto(funcionario: Funcionario, empresa: Empresa): CadastroPJDto =
        CadastroPJDto(funcionario.nome, funcionario.email,"",funcionario.cpf,
        empresa.cnpj,empresa.razaoSocial,funcionario.id)

    private fun converterDtoParaFuncionario(cadastroPJDto: CadastroPJDto, empresa: Empresa) =
        Funcionario(cadastroPJDto.nome, cadastroPJDto.email,
                    SenhaUtils().gerarBcrypt(cadastroPJDto.senha),cadastroPJDto.cpf,
                    PerfilEnum.ROLE_ADMIN, empresa.id.toString())

}