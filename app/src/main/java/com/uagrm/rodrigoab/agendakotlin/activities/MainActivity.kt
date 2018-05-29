package com.uagrm.rodrigoab.agendakotlin.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import com.uagrm.rodrigoab.agendakotlin.R
import com.uagrm.rodrigoab.agendakotlin.adapters.AdaptadorEvento
import com.uagrm.rodrigoab.agendakotlin.helpers.DBHelper
import com.uagrm.rodrigoab.agendakotlin.models.Evento

import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    internal lateinit var db : DBHelper
    internal var listaEventos : List<Evento> = ArrayList<Evento>()
    var calendario : CompactCalendarView ?= null
    var formato_dia : SimpleDateFormat ?= null
    var formato : SimpleDateFormat ?= null
    var diaSeleccionado : String ?= null
    var diaSeleccionado2 : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calendario = findViewById(R.id.compactcalendar_view) as CompactCalendarView
        calendario!!.setUseThreeLetterAbbreviation(true)
        calendario!!.shouldSelectFirstDayOfMonthOnScroll(false)

        formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        formato_dia =  SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        diaSeleccionado = formato_dia!!.format(Date()).toString()
        diaSeleccionado2 = formato!!.format(Date()).toString()

        val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())

        txt_mes.setText(dateFormat.format(calendario!!.firstDayOfCurrentMonth).toUpperCase())

        db = DBHelper(this)


        listaEventos = db.eventosDia(formato_dia!!.format(Date()).toString())
        val adapter = AdaptadorEvento(this@MainActivity, listaEventos)
        list_eventos.adapter = adapter


        btn_agregar.setOnClickListener {

            val intent = Intent(this, Formulario::class.java)
            intent.putExtra("id", -1)
            intent.putExtra("inicio", diaSeleccionado2!!)
            this.startActivity(intent)

        }

        actualizarDatos()


        calendario!!.setListener(object : CompactCalendarView.CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                listaEventos = db.eventosDia(formato_dia!!.format(dateClicked).toString())
                diaSeleccionado = formato_dia!!.format(dateClicked).toString()
                diaSeleccionado2 = formato!!.format(dateClicked).toString()
                val adapter = AdaptadorEvento(this@MainActivity, listaEventos)
                list_eventos.adapter = adapter
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                txt_mes.setText(dateFormat.format(calendario!!.firstDayOfCurrentMonth).toUpperCase())

                val formato_mes = SimpleDateFormat("MM-yyyy", Locale.getDefault())
                val listaEventoMes = db!!.eventosMes(formato_mes.format(calendario!!.firstDayOfCurrentMonth))
                calendario!!.removeAllEvents()
                for (evento: Evento in listaEventoMes) {
                    calendario!!.addEvent(Event(getColor(evento.color!!), SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse(evento.inicio).time))
                    
                }
            }
        })


    }

    private fun actualizarDatos() {
        listaEventos = db.all_eventos
        val adapter = AdaptadorEvento(this, listaEventos)
        list_eventos.adapter = adapter
    }


    override fun onResume() {
        super.onResume()

        db = DBHelper(this)

        calendario!!.removeAllEvents()

        val formato_mes = SimpleDateFormat("MM-yyyy", Locale.getDefault())
        val listaEventoMes = db!!.eventosMes(formato_mes.format(calendario!!.firstDayOfCurrentMonth))

        for (evento: Evento in listaEventoMes) {
            calendario!!.addEvent(Event(getColor(evento.color!!), SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse(evento.inicio).time))
        }

        listaEventos = db.eventosDia(diaSeleccionado!!)
        val adapter = AdaptadorEvento(this@MainActivity, listaEventos)
        list_eventos.adapter = adapter

    }


    private fun getColor(color : String) : Int{
        when(color){
            "Azul" -> {
                return Color.rgb(24, 93, 133)
            }
            "Rojo" -> {
                return Color.rgb(199, 0, 57)
            }
            "Verde" -> {
                return Color.rgb(0, 128, 0)
            }
            "Amarillo" -> {
                return Color.rgb(255, 193, 7)
            }
        }
        return Color.GRAY
    }





}

