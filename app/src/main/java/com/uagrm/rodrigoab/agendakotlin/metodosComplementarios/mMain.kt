package com.uagrm.rodrigoab.agendakotlin.metodosComplementarios

import android.graphics.Color

class mMain {
    fun getColor(color : String) : Int{
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