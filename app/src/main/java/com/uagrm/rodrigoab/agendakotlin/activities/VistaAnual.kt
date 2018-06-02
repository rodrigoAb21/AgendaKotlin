package com.uagrm.rodrigoab.agendakotlin.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.ik024.calendar_lib.custom.YearView
import com.github.ik024.calendar_lib.listeners.YearViewClickListeners
import com.uagrm.rodrigoab.agendakotlin.R

class VistaAnual : AppCompatActivity(), YearViewClickListeners {
    override fun dateClicked(year: Int, month: Int, day: Int) {
        Toast.makeText(this, "year: "+year+";\nmonth: "+month+";\nday: "+day, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_anual)


        var calendario = findViewById(R.id.year_view) as YearView
        calendario.registerYearViewClickListener(this)

    }
}
