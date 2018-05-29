package com.uagrm.rodrigoab.agendakotlin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.uagrm.rodrigoab.agendakotlin.models.Evento
import com.uagrm.rodrigoab.agendakotlin.R
import com.uagrm.rodrigoab.agendakotlin.activities.Formulario
import kotlinx.android.synthetic.main.row_layout.view.*

class AdaptadorEvento(internal var activity: Activity, internal var listaDeEventos: List<Evento>) :BaseAdapter() {

    internal var inflater : LayoutInflater

    init {
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val evento = listaDeEventos[p0]
        val rowView : View

        rowView = inflater.inflate(R.layout.row_layout, null)
        rowView.txt_titulo.setText(evento.nombre)
        rowView.txt_direccion.setText(evento.lugar)

        rowView.setOnClickListener {
            val intent = Intent(activity, Formulario::class.java)
            intent.putExtra("id", evento.id)
            intent.putExtra("color", evento.color)
            intent.putExtra("nombre", evento.nombre)
            intent.putExtra("lugar", evento.lugar)
            intent.putExtra("inicio", evento.inicio)
            intent.putExtra("fin", evento.fin)
            intent.putExtra("alarma", evento.alarma)
            intent.putExtra("descripcion", evento.descripcion)
            activity!!.startActivity(intent)
        }



        return rowView
    }

    override fun getItem(p0: Int): Any {
        return listaDeEventos[p0]
    }

    override fun getItemId(p0: Int): Long {
        return listaDeEventos[p0].id!!.toLong()
    }

    override fun getCount(): Int {
        return listaDeEventos.size
    }










}