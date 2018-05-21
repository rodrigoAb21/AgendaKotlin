package com.uagrm.rodrigoab.agendakotlin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.uagrm.rodrigoab.agendakotlin.Adaptador.AdaptadorEvento
import com.uagrm.rodrigoab.agendakotlin.DBHelper.DBHelper
import com.uagrm.rodrigoab.agendakotlin.Modelos.Evento

import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    internal lateinit var db : DBHelper
    internal var listaEventos : List<Evento> = ArrayList<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendario : CompactCalendarView = findViewById(R.id.compactcalendar_view) as CompactCalendarView
        calendario.setUseThreeLetterAbbreviation(true)

        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        txt_mes.setText(dateFormat.format(calendario.firstDayOfCurrentMonth).toUpperCase())

        db = DBHelper(this)


        btn_agregar.setOnClickListener {
            iniciarActivity(this, formulario::class.java)
        }

        actualizarDatos()


        calendario.setListener(object : CompactCalendarView.CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                //
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                txt_mes.setText(dateFormat.format(calendario.firstDayOfCurrentMonth).toUpperCase())
            }
        })


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

