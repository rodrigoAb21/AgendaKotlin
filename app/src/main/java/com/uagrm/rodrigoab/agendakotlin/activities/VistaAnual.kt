package com.uagrm.rodrigoab.agendakotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.ik024.calendar_lib.custom.YearView
import com.github.ik024.calendar_lib.listeners.YearViewClickListeners
import com.uagrm.rodrigoab.agendakotlin.R
import com.uagrm.rodrigoab.agendakotlin.helpers.CRUD_Evento
import com.uagrm.rodrigoab.agendakotlin.interfaces.crudInterface
import com.uagrm.rodrigoab.agendakotlin.models.Evento
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class VistaAnual : AppCompatActivity(), YearViewClickListeners {
    var crudInterface : crudInterface?= null
    internal var listaEventos : List<Evento> = ArrayList<Evento>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_anual)

        crudInterface = CRUD_Evento(this)
        var calendario = findViewById(R.id.year_view) as YearView
        calendario.registerYearViewClickListener(this)

        listaEventos = crudInterface!!.getAllItems()
        var listaDate = ArrayList<Date>()
        val formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        var date = Date()

        for (evento: Evento in listaEventos) {
            date = formato.parse(evento.inicio)
            date.hours = 0
            date.minutes = 0
            date.seconds = 0
            listaDate.add(date)
        }

        calendario.setEventList(listaDate)




    }


    override fun dateClicked(year: Int, month: Int, day: Int) {
        //Toast.makeText(this, "year: "+year+";\nmonth: "+month+";\nday: "+day, Toast.LENGTH_LONG).show()
    }
}
