package com.cursoapikotlin.pontointeligente

import ch.qos.logback.core.net.SyslogOutputStream
import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.documents.Funcionario
import com.cursoapikotlin.pontointeligente.enums.PerfilEnum
import com.cursoapikotlin.pontointeligente.repositories.EmpresaRepository
import com.cursoapikotlin.pontointeligente.repositories.FuncionarioRepository
import com.cursoapikotlin.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PontointeligenteApplication(val empresaRepository: EmpresaRepository,
								  val funcionarioRepository: FuncionarioRepository) : CommandLineRunner{

	override fun run(vararg args: String?){
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()

		val empresa: Empresa = Empresa("Empresa","10443887000146")
		empresaRepository.save(empresa)

		val admin: Funcionario = Funcionario("Admin", "admin@empresa.com",
			SenhaUtils().gerarBcrypt("123456"),"25708317000",PerfilEnum.ROLE_ADMIN,empresa.id!!)
		funcionarioRepository.save(admin)

		val funcionario: Funcionario = Funcionario("Funcionario", "funcionario@empresa.com",
			SenhaUtils().gerarBcrypt("123456"),"44325441557",PerfilEnum.ROLE_USUARIO,empresa.id!!)
		funcionarioRepository.save(funcionario)

		System.out.println("Empresa ID: " + empresa.id)
		System.out.println("Admin ID: " + admin.id)
		System.out.println("Funcionario ID: " + funcionario.id)
	}
}

fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
