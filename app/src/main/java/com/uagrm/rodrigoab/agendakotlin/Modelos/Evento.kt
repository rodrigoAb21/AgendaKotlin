package com.uagrm.rodrigoab.agendakotlin.Modelos

class Evento {
    var id : Int ?= null
    var nombre : String ?= null
    var direccion : String ?= null
    var descripcion : String ?= null

    constructor(){}

    constructor(id: Int, nombre: String, direccion: String, descripcion: String) {
        this.id = id
        this.nombre = nombre
        this.direccion = direccion
        this.descripcion = descripcion
    }


}