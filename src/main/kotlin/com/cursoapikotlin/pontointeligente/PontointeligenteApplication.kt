package com.cursoapikotlin.pontointeligente

import ch.qos.logback.core.net.SyslogOutputStream
import com.cursoapikotlin.pontointeligente.documents.Empresa
import com.cursoapikotlin.pontointeligente.documents.Funcionario
import com.cursoapikotlin.pontointeligente.documents.Lancamento
import com.cursoapikotlin.pontointeligente.enums.PerfilEnum
import com.cursoapikotlin.pontointeligente.enums.TipoEnum
import com.cursoapikotlin.pontointeligente.repositories.EmpresaRepository
import com.cursoapikotlin.pontointeligente.repositories.FuncionarioRepository
import com.cursoapikotlin.pontointeligente.repositories.LancamentoRepository
import com.cursoapikotlin.pontointeligente.utils.SenhaUtils
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication
import java.text.SimpleDateFormat
import java.util.*

@SpringBootApplication
class PontointeligenteApplication/*(val empresaRepository: EmpresaRepository,
								  val funcionarioRepository: FuncionarioRepository,
								  val lancamentoRepository: LancamentoRepository) : CommandLineRunner{

	override fun run(vararg args: String?){
		//Codigo para teste inicial
		empresaRepository.deleteAll()
		funcionarioRepository.deleteAll()
		lancamentoRepository.deleteAll()
		val date = Calendar.getInstance().time
		var empresa: Empresa = Empresa("Empresa","10443887000146")
		empresa = empresaRepository.save(empresa)

		var admin: Funcionario = Funcionario("Admin", "admin@empresa.com",
			SenhaUtils().gerarBcrypt("123456"),"25708317000",PerfilEnum.ROLE_ADMIN,empresa.id!!)
		admin = funcionarioRepository.save(admin)

		var funcionario: Funcionario = Funcionario("Funcionario", "funcionario@empresa.com",
			SenhaUtils().gerarBcrypt("123456"),"44325441557",PerfilEnum.ROLE_USUARIO,empresa.id!!)
		funcionario = funcionarioRepository.save(funcionario)

		var  lancamento: Lancamento = Lancamento(date,TipoEnum.INICIO_TRABALHO,funcionario.id!!,"","")
		lancamento = lancamentoRepository.save(lancamento)

		var lancamento1: Lancamento = Lancamento(date,TipoEnum.TERMINO_TRABALHO,funcionario.id!!,"","")
		lancamento1 = lancamentoRepository.save(lancamento1)

		System.out.println("Empresa ID: " + empresa.id)
		System.out.println("Admin ID: " + admin.id)
		System.out.println("Funcionario ID: " + funcionario.id)
		System.out.println("lANCAMENTO ID: " + lancamento.id)
		System.out.println("lANCAMENTO1 ID: " + lancamento1.id)
	}
}
*/
fun main(args: Array<String>) {
	runApplication<PontointeligenteApplication>(*args)
}
