package com.livraria.erro

import java.lang.RuntimeException

class LivroNaoEncontradoException(override val message:String): RuntimeException() {
}