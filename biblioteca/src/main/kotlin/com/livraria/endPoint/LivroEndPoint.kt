package com.livraria.endPoint

import com.livraria.BibliotecaRequest
import com.livraria.BibliotecaResponse
import com.livraria.BibliotecaServiceGrpc
import com.livraria.erro.ErrorHandler
import com.livraria.extension.toModel
import com.livraria.repository.LivroRepository
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
}