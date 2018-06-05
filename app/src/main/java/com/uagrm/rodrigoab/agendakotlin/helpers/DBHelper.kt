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
        val CREATE_TABLE_QUERY : String = "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_COLOR TEXT NOT NULL, $COL_NOMBRE TEXT NOT NULL, $COL_LUGAR TEXT NOT NULL, $COL_INICIO TEXT NOT NULL, $COL_FIN TEXT NOT NULL, $COL_ALARMA TEXT NOT NULL, $COL_DESCRIP TEXT NOT NULL)"

        p0!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(p0!!)
    }

}