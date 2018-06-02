package com.uagrm.rodrigoab.agendakotlin.models

class Evento {
    var id : Int ?= null
    var color : String ?= null
    var nombre : String ?= null
    var lugar : String ?= null
    var inicio : String ?= null
    var fin : String ?= null
    var alarma : String ?= null
    var descripcion : String ?= null

    constructor(){}

    constructor(id: Int, color: String, nombre: String, lugar: String, inicio: String, fin: String, alarma: String, descripcion: String) {
        this.id = id
        this.color = color
        this.nombre = nombre
        this.lugar = lugar
        this.inicio = inicio
        this.fin = fin
        this.alarma = alarma
        this.descripcion = descripcion
    }


}