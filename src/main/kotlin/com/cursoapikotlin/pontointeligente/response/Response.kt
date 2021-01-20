package com.cursoapikotlin.pontointeligente.response

data class Response<T> (
    val erros: ArrayList<String> = arrayListOf(), //inicializa o array de lista
    var data: T? = null
)
