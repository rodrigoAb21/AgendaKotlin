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
import com.uagrm.rodrigoab.agendakotlin.helpers.CRUD_Evento
import kotlinx.android.synthetic.main.activity_formulario.*
import java.text.SimpleDateFormat
import java.util.*

class Formulario : AppCompatActivity() {

    var bundle : Bundle ?= null
    var formato : SimpleDateFormat ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        setupFormulario()

        edt_inicio.setOnClickListener {
            selectorFecha(edt_inicio)
        }

        edt_fin.setOnClickListener {
            selectorFecha(edt_fin)
        }


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
        var crudInterface = CRUD_Evento(this)
        val df = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        when (item!!.itemId) {
            R.id.menu_btn_add -> {

                if(edt_nombre.text.toString().trim() != ""){
                    val valores = ContentValues()
                    cargarContent(valores)

                    val id = crudInterface!!.agregar(valores)
                    if (id > 0) {
                        if (selector_alarma.selectedItem.toString() != "Sin alarma") {
                            var fechaHora = df.parse(getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))
                            if(Date().time < fechaHora.time){
                                metodoX(id.toInt(), fechaHora,"crear")
                            }
                        }

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
                    val id = bundle!!.getInt("id")
                    val valores = ContentValues()
                    valores.put("id", id)
                    cargarContent(valores)

                    if (crudInterface!!.actualizar(valores) > 0) {
                        if (selector_alarma.selectedItem.toString() == "Sin alarma") {
                            metodoX(id, null, "eliminar")
                        }else{
                            var fechaHora = df.parse(getAlarma(edt_inicio.text.toString(), selector_alarma.selectedItem.toString()))
                            if(Date().time < fechaHora.time){
                                metodoX(id, fechaHora, "actualizar")
                            }
                        }
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
                    metodoX(id, null, "eliminar")
                    crudInterface!!.eliminar(id.toString())
                    finish()
                })
                alertDialog.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int -> })

                alertDialog.show()
            }

        }

        return super.onOptionsItemSelected(item)
    }

    // algunos metodos ;)

    private fun setupFormulario(){
        val selectorAdapter = ArrayAdapter.createFromResource(this,
                R.array.items_colores, android.R.layout.simple_spinner_dropdown_item)
        val selectorAlarmaAdapter = ArrayAdapter.createFromResource(this,
                R.array.items_alarma, android.R.layout.simple_spinner_dropdown_item)
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
    }


    private fun selectorFecha(textView: TextView){

        var alertView = AlertDialog.Builder(this)
        var vistaAlertDialog = View.inflate(this, R.layout.date_time_picker, null)

        alertView.setView(vistaAlertDialog)

        var datePicker2 = vistaAlertDialog.findViewById<DatePicker>(R.id.datepicker)
        var timePicker2 = vistaAlertDialog.findViewById<TimePicker>(R.id.timepicker)

        datePicker2.firstDayOfWeek = Calendar.MONDAY


        var fecha = Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault())
        fecha.time = formato!!.parse(textView.text.toString())
        datePicker2.updateDate(fecha.get(Calendar.YEAR), fecha.get(Calendar.MONTH),
                fecha.get(Calendar.DAY_OF_MONTH))

        timePicker2.setIs24HourView(true)
        timePicker2.currentHour = fecha.get(Calendar.HOUR_OF_DAY)
        timePicker2.currentMinute = fecha.get(Calendar.MINUTE)

        val dialogo = alertView.show()

        vistaAlertDialog.findViewById<Button>(R.id.btn_dateTime).setOnClickListener {
            var fecha = GregorianCalendar(datePicker2.year, datePicker2.month,
                    datePicker2.dayOfMonth, timePicker2.hour, timePicker2.minute, 0)
            var cadena = formato!!.format(fecha.time).toString()

            textView.text = cadena

            if(formato!!.parse(edt_fin.text.toString()).time <
                    formato!!.parse(edt_inicio.text.toString()).time){
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

    private fun metodoX(id : Int, fechaHora : Date?, accion : String){
        var myIntent = Intent(this, AlarmNotificationReceiver().javaClass)

        myIntent.putExtra("id",id)
        myIntent.setData(Uri.parse("myalarms://"+ id))
        myIntent.putExtra("color", selector_color.selectedItem.toString())
        myIntent.putExtra("nombre",edt_nombre.text.toString().trim())
        myIntent.putExtra("lugar", edt_lugar.text.toString().trim())
        myIntent.putExtra("inicio", edt_inicio.text.toString())
        myIntent.putExtra("fin", edt_fin.text.toString())
        myIntent.putExtra("alarma", selector_alarma.selectedItem.toString())
        myIntent.putExtra("descripcion", edt_descripcion.text.toString().trim())

        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)
        var manager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        when(accion){
            "crear" -> {
                manager.set(AlarmManager.RTC_WAKEUP, fechaHora!!.time, pendingIntent)
            }
            "actualizar" -> {
                manager.cancel(pendingIntent)
                manager.set(AlarmManager.RTC_WAKEUP, fechaHora!!.time, pendingIntent)
            }
            "eliminar" -> {
                manager.cancel(pendingIntent)
            }
        }


    }

    private fun cargarContent(valores : ContentValues){
        valores.put("nombre", edt_nombre.text.toString().trim())
        valores.put("color", selector_color.selectedItem.toString())
        valores.put("lugar", edt_lugar.text.toString().trim())
        valores.put("inicio", edt_inicio.text.toString())
        valores.put("fin", edt_fin.text.toString())
        valores.put("alarma", selector_alarma.selectedItem.toString())
        valores.put("descripcion", edt_descripcion.text.toString().trim())
    }

}
