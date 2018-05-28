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
import java.time.LocalDate
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
        val formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        txt_mes.setText(dateFormat.format(calendario.firstDayOfCurrentMonth).toUpperCase())

        db = DBHelper(this)


        btn_agregar.setOnClickListener {
            // var hoy = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).parse("29-05-2018 00:00:00")
            // Toast.makeText(this, hoy.toString(), Toast.LENGTH_LONG).show()
            val intent = Intent(this, formulario::class.java)
            intent.putExtra("id", -1)
            intent.putExtra("inicio", formato.format(Date()).toString())
            this.startActivity(intent)
        }

        actualizarDatos()


        calendario.setListener(object : CompactCalendarView.CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                Toast.makeText(this@MainActivity, formato.format(dateClicked).toString(), Toast.LENGTH_LONG).show()
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


    override fun onResume() {
        super.onResume()
        db = DBHelper(this)
        actualizarDatos()
    }





}

