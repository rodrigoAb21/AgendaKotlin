package com.uagrm.rodrigoab.agendakotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.uagrm.rodrigoab.agendakotlin.Adaptador.AdaptadorEvento
import com.uagrm.rodrigoab.agendakotlin.DBHelper.DBHelper
import com.uagrm.rodrigoab.agendakotlin.Modelos.Evento

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    internal lateinit var db : DBHelper
    internal var listaEventos : List<Evento> = ArrayList<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DBHelper(this)


        btn_agregar.setOnClickListener {
            iniciarActivity(this, formulario::class.java)
        }

        actualizarDatos()

    }

    private fun actualizarDatos() {
        listaEventos = db.all_eventos
        val adapter = AdaptadorEvento(this, listaEventos)
        list_eventos.adapter = adapter
    }

    private fun iniciarActivity(activity : Activity, nextActivity: Class<*>){
        val intent = Intent(activity, nextActivity)
        intent.putExtra("id", -1)
        activity.startActivity(intent)
    }



    override fun onResume() {
        super.onResume()
        db = DBHelper(this)
        actualizarDatos()
    }


}

