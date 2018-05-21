package com.uagrm.rodrigoab.agendakotlin.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.uagrm.rodrigoab.agendakotlin.Modelos.Evento

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private val DATABASE_NAME = "CALENDARIO.db"
        private val DATABASE_VER = 1

        //Tabla
        private val TABLE_NAME = "evento"
        private val COL_ID = "id"
        private val COL_NOMBRE = "nombre"
        private val COL_DIR = "direccion"
        private val COL_DESCRIP = "descripcion"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY : String = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NOMBRE TEXT NOT NULL, $COL_DIR TEXT NOT NULL, $COL_DESCRIP TEXT NOT NULL)"
        p0!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0!!)
    }



    // CRUD de Eventos
    var db = this.writableDatabase

    // Listar eventos
    val all_eventos : List<Evento>
        get() {
            val lista_eventos = ArrayList<Evento>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()){
                do {
                    val evento = Evento()
                    evento.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    evento.nombre = cursor.getString(cursor.getColumnIndex(COL_NOMBRE))
                    evento.direccion = cursor.getString(cursor.getColumnIndex(COL_DIR))
                    evento.descripcion = cursor.getString(cursor.getColumnIndex(COL_DESCRIP))

                    lista_eventos.add(evento)
                }while (cursor.moveToNext())
            }
            db.close()
            return lista_eventos
        }


    fun agregarEvento(values : ContentValues) : Long{
        return db!!.insert(TABLE_NAME, null, values)
    }

    fun eliminarEvento(id : String){
        db!!.delete(TABLE_NAME, "$COL_ID= ?", arrayOf(id))
    }

    fun actualizarEvento(values : ContentValues) : Int{
        return db!!.update(TABLE_NAME, values, "$COL_ID =?", arrayOf(values.get("id").toString()))
    }



















}