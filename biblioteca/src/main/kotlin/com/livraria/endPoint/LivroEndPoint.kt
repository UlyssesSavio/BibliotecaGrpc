package com.livraria.endPoint

import com.livraria.*
import com.livraria.erro.ErrorHandler
import com.livraria.erro.LivroNaoEncontradoException
import com.livraria.extension.toModel
import com.livraria.repository.LivroRepository
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@ErrorHandler
@Singleton
class LivroEndPoint(val repository:LivroRepository): BibliotecaServiceGrpc.BibliotecaServiceImplBase() {
    override fun cadastraLivro(request: BibliotecaRequest, responseObserver: StreamObserver<BibliotecaResponse>) {

        val livro = request.toModel()
        repository.save(livro)

        responseObserver.onNext(livro.toResponse())
        responseObserver.onCompleted()

    }

    override fun buscaLivro(request: BibliotecaBuscaRequest, responseObserver: StreamObserver<BibliotecaResponse>) {

        val livro = repository.findByIsbn(request.ibsn)
                ?: throw LivroNaoEncontradoException("Livro nao encontrado")

        responseObserver.onNext(livro.toResponse())
        responseObserver.onCompleted()

    }

    override fun deletaLivro(request: BibliotecaBuscaRequest, responseObserver: StreamObserver<BibliotecaDeletaResponse>) {

        if(!repository.existsByIsbn(request.ibsn)) throw LivroNaoEncontradoException("Livro nao encontrado")
        repository.deleteByIsbn(request.ibsn)

        responseObserver.onNext(BibliotecaDeletaResponse.newBuilder().setIsbn(request.ibsn).build())
        responseObserver.onCompleted()

    }
}