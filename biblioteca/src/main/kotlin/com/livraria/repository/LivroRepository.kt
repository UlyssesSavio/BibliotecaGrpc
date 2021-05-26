package com.livraria.repository

import com.livraria.model.Livro
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository


@Repository
interface LivroRepository : JpaRepository<Livro, Long>{

}