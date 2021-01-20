package com.cursoapikotlin.pontointeligente.services

import com.cursoapikotlin.pontointeligente.documents.Lancamento
import com.cursoapikotlin.pontointeligente.enums.TipoEnum
import com.cursoapikotlin.pontointeligente.repositories.LancamentoRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

@SpringBootTest
class LancamentoServiceTest {

    @Autowired
    private val lancamentoService: LancamentoService? = null

    @MockBean
    private val lancamentoRepository: LancamentoRepository? = null

    private val id: String = "1"

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito
            .given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId(id, PageRequest.of(0, 10)))
            .willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById("1")).willReturn(Optional.of(lancamento()))
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java)))
            .willReturn(lancamento())

    }

    @Test
    fun testBuscarFuncionarioPorFuncionarioId(){
        val lancamento: Page<Lancamento>? = this.lancamentoService?.buscarPorFuncionarioId(id,PageRequest.of(0,10))
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testBuscarLancamentoPorId(){
        val lancamento: Lancamento? = this.lancamentoService?.buscarPorId(id)
        Assertions.assertNotNull(lancamento)

    }

    @Test
    fun testPersistirLancamento(){
        val lancamento: Lancamento? = this.lancamentoService?.persistir(lancamento())
        Assertions.assertNotNull(lancamento)
    }

    private fun lancamento(): Lancamento = Lancamento(Date(), TipoEnum.INICIO_TRABALHO, id)
}