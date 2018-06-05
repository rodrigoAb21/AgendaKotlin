package com.uagrm.rodrigoab.agendakotlin.helpers

import android.content.ContentValues
import android.content.Context
import com.uagrm.rodrigoab.agendakotlin.interfaces.crudInterface
import com.uagrm.rodrigoab.agendakotlin.models.Evento

class CRUD_Evento(context: Context) : crudInterface{

    val dbHelper = DBHelper(context)
    val tabla = "evento"

    override fun agregar( valores: ContentValues): Long {
        val db = dbHelper.writableDatabase
        val id = db.insert(tabla, null, valores)
        db.close()
        return id
    }

    override fun actualizar(valores: ContentValues): Int {
        val db = dbHelper.writableDatabase
        val id = db.update(tabla, valores, "id =?", arrayOf(valores.get("id").toString()))
        db.close()
        return  id
    }

    override fun eliminar(id: String) {
        val db = dbHelper.writableDatabase
        db.delete(tabla, "id =?", arrayOf(id))
        db.close()
    }

    override fun getAllItems(): ArrayList<Evento> {
        val eventos_dia = ArrayList<Evento>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM tabla"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val evento = Evento()
                evento.id = cursor.getInt(cursor.getColumnIndex("id"))
                evento.color = cursor.getString(cursor.getColumnIndex("color"))
                evento.nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                evento.lugar = cursor.getString(cursor.getColumnIndex("lugar"))
                evento.inicio = cursor.getString(cursor.getColumnIndex("inicio"))
                evento.fin = cursor.getString(cursor.getColumnIndex("fin"))
                evento.alarma = cursor.getString(cursor.getColumnIndex("alarma"))
                evento.descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))

                eventos_dia.add(evento)
            }while (cursor.moveToNext())
        }
        db.close()
        return eventos_dia
    }

    override fun getItemsByDate(fecha: String): ArrayList<Evento> {
        val eventos_dia = ArrayList<Evento>()
        val db = dbHelper.writableDatabase
        val selectQuery = "SELECT * FROM evento WHERE inicio LIKE \"%" + fecha + "%\" ORDER BY inicio"
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val evento = Evento()
                evento.id = cursor.getInt(cursor.getColumnIndex("id"))
                evento.color = cursor.getString(cursor.getColumnIndex("color"))
                evento.nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                evento.lugar = cursor.getString(cursor.getColumnIndex("lugar"))
                evento.inicio = cursor.getString(cursor.getColumnIndex("inicio"))
                evento.fin = cursor.getString(cursor.getColumnIndex("fin"))
                evento.alarma = cursor.getString(cursor.getColumnIndex("alarma"))
                evento.descripcion = cursor.getString(cursor.getColumnIndex("descripcion"))

                eventos_dia.add(evento)
            }while (cursor.moveToNext())
        }
        db.close()
        return eventos_dia
    }


}