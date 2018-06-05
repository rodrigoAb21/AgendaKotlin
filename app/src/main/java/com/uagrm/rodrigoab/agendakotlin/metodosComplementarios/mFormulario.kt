package com.uagrm.rodrigoab.agendakotlin.metodosComplementarios

import java.text.SimpleDateFormat
import java.util.*

class mFormulario {
    constructor(){}
    fun getAlarma(inicio : String, alarma : String) : String{
        var fecha = Calendar.getInstance(TimeZone.getDefault() , Locale.getDefault())
        var formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        fecha.time = formato!!.parse(inicio)
        when(alarma){
            "10 min antes" -> {
                fecha.add(Calendar.MINUTE, -10)
            }
            "30 min antes" -> {
                fecha.add(Calendar.MINUTE, -30)
            }
            "1 hora antes" -> {
                fecha.add(Calendar.HOUR, -1)
            }
            "1 dia antes" -> {
                fecha.add(Calendar.DAY_OF_MONTH, -1)
            }
            "Sin alarma" -> {
                return "Sin alarma"
            }
        }

        return formato!!.format(fecha.time).toString()

    }

    fun getPosicion(color : String): Int{
        when(color) {
            "Azul"-> return 0
            "Rojo"-> return 1
            "Verde"-> return 2
            "Amarillo"-> return 3
        }
        return 4
    }

    fun getPosicionAlarma(alarma : String): Int{
        when(alarma) {
            "Sin alarma"-> return 0
            "10 min antes"-> return 1
            "30 min antes"-> return 2
            "1 hora antes"-> return 3
        }
        return 4
    }

}