package com.uagrm.rodrigoab.agendakotlin.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uagrm.rodrigoab.agendakotlin.models.Evento

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        // DB
        private val DATABASE_NAME = "CALENDARIO.db"
        private val DATABASE_VER = 1

        // Tabla
        private val TABLE_NAME = "evento"
        private val COL_ID = "id"
        private val COL_COLOR = "color"
        private val COL_NOMBRE = "nombre"
        private val COL_LUGAR = "lugar"
        private val COL_INICIO = "inicio"
        private val COL_FIN = "fin"
        private val COL_ALARMA = "alarma"
        private val COL_DESCRIP = "descripcion"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY : String = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_COLOR TEXT NOT NULL, $COL_NOMBRE TEXT NOT NULL, $COL_LUGAR TEXT NOT NULL, $COL_INICIO TEXT NOT NULL, $COL_FIN TEXT NOT NULL, $COL_ALARMA INTEGER NOT NULL, $COL_DESCRIP TEXT NOT NULL)"

        p0!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0!!)
    }



    // ---------------------------------- CRUD de Eventos ------------------------------------------
    var db = this.writableDatabase

    // Devuelve todos los eventos
    val all_eventos : List<Evento>
        get() {
            val lista_eventos = ArrayList<Evento>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val cursor = db!!.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()){
                do {
                    val evento = Evento()
                    evento.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    evento.color = cursor.getString(cursor.getColumnIndex(COL_COLOR))
                    evento.nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                    evento.lugar = cursor.getString(cursor.getColumnIndex(COL_LUGAR))
                    evento.inicio = cursor.getString(cursor.getColumnIndex(COL_INICIO))
                    evento.fin = cursor.getString(cursor.getColumnIndex(COL_FIN))
                    evento.alarma = cursor.getInt(cursor.getColumnIndex(COL_ALARMA))
                    evento.descripcion = cursor.getString(cursor.getColumnIndex(COL_DESCRIP))

                    lista_eventos.add(evento)
                }while (cursor.moveToNext())
            }
            return lista_eventos
        }

    // No es necesario que devuelva un valor, pero esta solo para la verificacion.
    fun agregarEvento(values : ContentValues) : Long{
        return db!!.insert(TABLE_NAME, null, values)
    }

    fun eliminarEvento(id : String){
        db!!.delete(TABLE_NAME, "$COL_ID= ?", arrayOf(id))
    }

    fun actualizarEvento(values : ContentValues) : Int{
        return db!!.update(TABLE_NAME, values, "$COL_ID =?", arrayOf(values.get("id").toString()))
    }

    fun eventosDia(diaMesAno : String) : ArrayList<Evento>{
        val eventos_dia = ArrayList<Evento>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_INICIO LIKE \"" + diaMesAno + "%\" "
        val cursor = db!!.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val evento = Evento()
                evento.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                evento.color = cursor.getString(cursor.getColumnIndex(COL_COLOR))
                evento.nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                evento.lugar = cursor.getString(cursor.getColumnIndex(COL_LUGAR))
                evento.inicio = cursor.getString(cursor.getColumnIndex(COL_INICIO))
                evento.fin = cursor.getString(cursor.getColumnIndex(COL_FIN))
                evento.alarma = cursor.getInt(cursor.getColumnIndex(COL_ALARMA))
                evento.descripcion = cursor.getString(cursor.getColumnIndex(COL_DESCRIP))

                eventos_dia.add(evento)
            }while (cursor.moveToNext())
        }
        return eventos_dia
    }

    fun eventosMes(mesAno : String) : ArrayList<Evento>{
        val eventos_mes = ArrayList<Evento>()
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_INICIO LIKE \"%" + mesAno +"%\""
        val cursor = db!!.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val evento = Evento()
                evento.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                evento.color = cursor.getString(cursor.getColumnIndex(COL_COLOR))
                evento.nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                evento.lugar = cursor.getString(cursor.getColumnIndex(COL_LUGAR))
                evento.inicio = cursor.getString(cursor.getColumnIndex(COL_INICIO))
                evento.fin = cursor.getString(cursor.getColumnIndex(COL_FIN))
                evento.alarma = cursor.getInt(cursor.getColumnIndex(COL_ALARMA))
                evento.descripcion = cursor.getString(cursor.getColumnIndex(COL_DESCRIP))

                eventos_mes.add(evento)
            }while (cursor.moveToNext())
        }
        return eventos_mes
    }



















}