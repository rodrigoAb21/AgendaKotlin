package com.uagrm.rodrigoab.agendakotlin.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.uagrm.rodrigoab.agendakotlin.R
import com.uagrm.rodrigoab.agendakotlin.helpers.AlarmNotificationReceiver
import com.uagrm.rodrigoab.agendakotlin.helpers.DBHelper
import kotlinx.android.synthetic.main.activity_formulario.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Formulario : AppCompatActivity() {

    var bundle : Bundle ?= null
    var formato : SimpleDateFormat ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)
        val selectorAdapter = ArrayAdapter.createFromResource(this, R.array.items_colores, android.R.layout.simple_spinner_dropdown_item)
        val selectorAlarmaAdapter = ArrayAdapter.createFromResource(this, R.array.items_alarma, android.R.layout.simple_spinner_dropdown_item)
        selector_color.adapter = selectorAdapter
        selector_alarma.adapter = selectorAlarmaAdapter
        formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        bundle = intent.extras
        if ( bundle!!.getInt("id") > 0){

            edt_nombre.setText(bundle!!.getString("nombre"))
            selector_color.setSelection(getPosicion(bundle!!.getString("color")))
            edt_lugar.setText(bundle!!.getString("lugar"))
            edt_inicio.setText(bundle!!.getString("inicio"))
            edt_fin.setText(bundle!!.getString("fin"))
            selector_alarma.setSelection(getPosicionAlarma(bundle!!.getString("alarma")))
            edt_descripcion.setText(bundle!!.getString("descripcion"))

        } else {
            edt_inicio.setText(bundle!!.getString("inicio"))
            edt_fin.setText(bundle!!.getString("inicio"))
        }

        edt_inicio.setOnClickListener {
            selectorFecha(edt_inicio)
        }

        edt_fin.setOnClickListener {
            selectorFecha(edt_fin)
        }


    }





    private fun selectorFecha(textView: TextView){

            var alertView = AlertDialog.Builder(this)
            var vistaAlertDialog = View.inflate(this, R.layout.date_time_picker, null)

            alertView.setView(vistaAlertDialog)
            val dialogo = alertView.show()


            val datePicker2 = vistaAlertDialog.findViewById<DatePicker>(R.id.datepicker)
            val timePicker2 = vistaAlertDialog.findViewById<TimePicker>(R.id.timepicker)

            datePicker2.firstDayOfWeek = Calendar.MONDAY
            timePicker2.setIs24HourView(true)


            vistaAlertDialog.findViewById<Button>(R.id.btn_dateTime).setOnClickListener {
                var fecha = GregorianCalendar(datePicker2.year, datePicker2.month, datePicker2.dayOfMonth, timePicker2.hour, timePicker2.minute, 0)
                var cadena = formato!!.format(fecha.time).toString()

                textView.text = cadena

                if(formato!!.parse(edt_fin.text.toString()).time < formato!!.parse(edt_inicio.text.toString()).time){
                    edt_fin.setText(edt_inicio.text.toString())
                }

                dialogo.dismiss()

            }

    }


    private fun getAlarma(inicio : String, alarma : String) : String{
        var fecha = Calendar.getInstance(TimeZone.getDefault() ,Locale.getDefault())
        fecha.time = formato!!.parse(inicio)
        when(alarma){
            "10 min antes" -> {
                fecha.add(Calendar.MINUTE, -10)
                return formato!!.format(fecha.time).toString()
            }
            "30 min antes" -> {
                fecha.add(Calendar.MINUTE, -30)
                return formato!!.format(fecha.time).toString()
            }
            "1 hora antes" -> {
                fecha.add(Calendar.HOUR, -1)
                return formato!!.format(fecha.time).toString()
            }
            "1 dia antes" -> {
                fecha.add(Calendar.DAY_OF_MONTH, -1)
                return formato!!.format(fecha.time).toString()
            }
        }

        return "Sin alarma"

    }


    private fun getPosicion(color : String): Int{
        when(color) {
            "Azul"-> return 0
            "Rojo"-> return 1
            "Verde"-> return 2
            "Amarillo"-> return 3
        }
        return 4
    }

    private fun getPosicionAlarma(alarma : String): Int{
        when(alarma) {
            "Sin alarma"-> return 0
            "10 min antes"-> return 1
            "30 min antes"-> return 2
            "1 hora antes"-> return 3
        }
        return 4
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if ( bundle!!.getInt("id") > 0){
            menuInflater.inflate(R.menu.menu_edt, menu)
        } else {
            menuInflater.inflate(R.menu.menu_add, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val dbHelper = DBHelper(this)

        when (item!!.itemId) {
            R.id.menu_btn_add -> {

                if(edt_nombre.text.toString().trim() != ""){
                    val valores = ContentValues()
                    valores.put("nombre", edt_nombre.text.toString().trim())
                    valores.put("color", selector_color.selectedItem.toString())
                    valores.put("lugar", edt_lugar.text.toString().trim())
                    valores.put("inicio", edt_inicio.text.toString())
                    valores.put("fin", edt_fin.text.toString())
                    valores.put("alarma", getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))
                    valores.put("descripcion", edt_descripcion.text.toString().trim())

                    val id = dbHelper!!.agregarEvento(valores)
                    if (id > 0) {

                        var myIntent = Intent(this, AlarmNotificationReceiver().javaClass)

                        myIntent.putExtra("id",id)
                        myIntent.setData(Uri.parse("myalarms://"+ id))
                        myIntent.putExtra("titulo",edt_nombre.text.toString().trim())
                        myIntent.putExtra("lugar", edt_lugar.text.toString().trim())
                        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0)
                        var manager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val df : DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                        var fechaHora = df.parse(getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))
                        manager.set(AlarmManager.RTC_WAKEUP,fechaHora.time,pendingIntent)


                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo agregar el evento", Toast.LENGTH_LONG).show()
                    }
                } else {
                    edt_nombre.setText("")
                    Toast.makeText(this, "El campo nombre es obligatorio.", Toast.LENGTH_SHORT).show()
                }


            }

            R.id.menu_btn_update -> {
                if (edt_nombre.text.toString().trim() != ""){
                    val id = bundle!!.getInt("id").toString()
                    val valores = ContentValues()
                    valores.put("id", id)
                    valores.put("nombre", edt_nombre.text.toString().trim())
                    valores.put("color", selector_color.selectedItem.toString())
                    valores.put("lugar", edt_lugar.text.toString().trim())
                    valores.put("inicio", edt_inicio.text.toString())
                    valores.put("fin", edt_fin.text.toString())
                    valores.put("alarma", getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))
                    valores.put("descripcion", edt_descripcion.text.toString().trim())

                    if (dbHelper!!.actualizarEvento(valores) > 0) {

                        var myIntent = Intent(this, AlarmNotificationReceiver().javaClass)
                        myIntent.putExtra("id",id)
                        myIntent.setData(Uri.parse("myalarms://"+ id))
                        myIntent.putExtra("titulo",edt_nombre.text.toString().trim())
                        myIntent.putExtra("lugar", edt_lugar.text.toString().trim())

                        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0)
                        var manager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        manager.cancel(pendingIntent)

                        myIntent.putExtra("id",id)
                        myIntent.setData(Uri.parse("myalarms://"+ id))
                        myIntent.putExtra("titulo",edt_nombre.text.toString().trim())
                        myIntent.putExtra("lugar", edt_lugar.text.toString().trim())


                        val df : DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
                        var fechaHora = df.parse(getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))

                        manager.set(AlarmManager.RTC_WAKEUP,fechaHora.time,pendingIntent)


                        finish()
                    } else {
                        Toast.makeText(this, "No se pudo editar el evento", Toast.LENGTH_LONG).show()
                    }
                } else {
                    edt_nombre.setText("")
                    Toast.makeText(this, "El campo nombre es obligatorio.", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.menu_btn_delete -> {
                val id = bundle!!.getInt("id")
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Quieres eliminar el evento?")
                alertDialog.setPositiveButton("Eliminar", { dialogInterface: DialogInterface, i: Int ->

                    var myIntent = Intent(this, AlarmNotificationReceiver().javaClass)
                    myIntent.putExtra("id",id)
                    myIntent.setData(Uri.parse("myalarms://"+ id))
                    myIntent.putExtra("titulo",edt_nombre.text.toString().trim())
                    myIntent.putExtra("lugar", edt_lugar.text.toString().trim())

                    var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0)
                    var manager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    manager.cancel(pendingIntent)


                    dbHelper!!.eliminarEvento(id.toString())
                    finish()
                })
                alertDialog.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int -> })
                alertDialog.show()
            }

        }

        return super.onOptionsItemSelected(item)
    }




}
