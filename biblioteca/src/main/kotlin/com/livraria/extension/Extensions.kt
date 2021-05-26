package com.livraria.extension

import com.livraria.BibliotecaRequest
import com.livraria.model.Livro
import java.time.*


fun BibliotecaRequest.toModel(): Livro{

    return Livro(this.nome, this.descricao, this.preco,
        LocalDateTime.ofEpochSecond(this.dataLancamento.seconds, this.dataLancamento.nanos, ZoneOffset.UTC)
        )

}
