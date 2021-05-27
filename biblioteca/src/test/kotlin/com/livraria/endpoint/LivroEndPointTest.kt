package com.livraria.endpoint

import com.google.protobuf.Timestamp
import com.livraria.BibliotecaBuscaRequest
import com.livraria.BibliotecaRequest
import com.livraria.BibliotecaServiceGrpc
import com.livraria.extension.toModel
import com.livraria.model.Livro
import com.livraria.repository.LivroRepository
import io.grpc.ManagedChannel
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Singleton


@MicronautTest(transactional = false)
internal class LivroEndPointTest(val repository: LivroRepository){

    @Inject
    lateinit var grpc: BibliotecaServiceGrpc.BibliotecaServiceBlockingStub

    @BeforeEach
    fun antesDeCada(){
        repository.deleteAll()
    }

    @Test
    fun `deve cadastrar um livro`(){

        val livroRequest = geraLivroRequest("nome teste", "teste", 20.0)
        val response = grpc.cadastraLivro(livroRequest)

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(nome, livroRequest.nome)
            Assertions.assertEquals(descricao, livroRequest.descricao)

        }

    }

    @Test
    fun `nao deve cadastrar um livro nome invalido`(){

        val livroRequest = geraLivroRequest("","descricao", 20.0)

        Assertions.assertThrows(StatusRuntimeException::class.java) {
            grpc.cadastraLivro(livroRequest)
        }
    }

    @Test
    fun `nao deve cadastrar um livro descricao invalida`(){

        val livroRequest = geraLivroRequest("tenho um nome","", 20.0)

        Assertions.assertThrows(StatusRuntimeException::class.java) {
            grpc.cadastraLivro(livroRequest)
        }
    }




    @Test
    fun `deve buscar um livro`(){

        val livroRequest = geraLivroRequest("livro buscado", "descricao", 20.0)
        val livro = livroRequest.toModel()
        repository.save(livro)

        val response = grpc.buscaLivro(BibliotecaBuscaRequest.newBuilder()
                .setIbsn(livro.isbn)
                .build())

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(nome, livro.nome)
        }

    }

    @Test
    fun `nao deve buscar um livro`(){

        Assertions.assertThrows(StatusRuntimeException::class.java){
            grpc.buscaLivro(BibliotecaBuscaRequest.newBuilder()
                    .setIbsn("nao existo")
                    .build())
        }
    }

    @Test
    fun `deve deletar um livro`(){
        val livroRequest = geraLivroRequest("livro buscado","descricao",20.0)
        val livro = livroRequest.toModel()
        repository.save(livro)

        val response = grpc.deletaLivro(BibliotecaBuscaRequest.newBuilder()
                .setIbsn(livro.isbn)
                .build())

        with(response){
            Assertions.assertNotNull(this)
            Assertions.assertEquals(isbn, livro.isbn)
            Assertions.assertFalse(repository.existsByIsbn(livro.isbn))
        }


    }

    @Test
    fun `nao deve deletar um livro`(){
        Assertions.assertThrows(StatusRuntimeException::class.java){
            grpc.deletaLivro(BibliotecaBuscaRequest.newBuilder()
                    .setIbsn("nao existo")
                    .build())
        }
    }


    fun geraLivroRequest(nome: String, descricao: String, preco:Double): BibliotecaRequest {
        return BibliotecaRequest.newBuilder()
                .setDataLancamento(Timestamp.newBuilder().setSeconds(1).setSeconds(1L).build())
                .setNome(nome)
                .setDescricao(descricao)
                .setPreco(preco)
                .build()
    }



    @Factory
    class cliente {
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): BibliotecaServiceGrpc.BibliotecaServiceBlockingStub {
            return BibliotecaServiceGrpc.newBlockingStub(channel)
        }
    }

}