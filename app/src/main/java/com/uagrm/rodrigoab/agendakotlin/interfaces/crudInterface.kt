package com.uagrm.rodrigoab.agendakotlin.interfaces

import android.content.ContentValues
import com.uagrm.rodrigoab.agendakotlin.models.Evento

interface crudInterface {
    fun agregar(valores : ContentValues) : Long
    fun actualizar(valores : ContentValues) : Int
    fun eliminar(id : String)
    fun getAllItems() : ArrayList<Evento>
    fun getItemsByDate(fecha : String) : ArrayList<Evento>
}