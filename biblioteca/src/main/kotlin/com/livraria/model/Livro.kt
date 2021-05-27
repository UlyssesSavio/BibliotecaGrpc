package com.livraria.model

import com.google.protobuf.Timestamp
import com.livraria.BibliotecaResponse
import io.micronaut.core.annotation.Introspected
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Introspected
class Livro(@field:NotBlank val nome:String,
            @field:NotBlank val descricao:String,
            @field:NotNull val preco:Double,
            @field:NotNull val dataLancamento: LocalDateTime) {

    @Id
    @GeneratedValue
    var id:Long? = null

    var isbn:String = UUID.randomUUID().toString()



        fun toResponse():BibliotecaResponse{
            return BibliotecaResponse.newBuilder()
                    .setNome(this.nome)
                    .setDescricao(this.descricao)
                    .setPreco(this.preco)
                    .setDataLancamento(
                            Timestamp.newBuilder().setNanos(this.dataLancamento.nano)
                                    .setSeconds(this.dataLancamento.second.toLong()).build()
                    )
                    .setIsbnGerado(this.isbn)
                    .build()


    }
}