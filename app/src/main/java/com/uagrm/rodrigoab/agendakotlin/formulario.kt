package com.uagrm.rodrigoab.agendakotlin

import android.content.ContentValues
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.uagrm.rodrigoab.agendakotlin.DBHelper.DBHelper
import kotlinx.android.synthetic.main.activity_formulario.*
import java.text.SimpleDateFormat
import java.util.*

class formulario : AppCompatActivity() {

    var bundle : Bundle ?= null
    var formato : SimpleDateFormat ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)
        var selectorAdapter = ArrayAdapter.createFromResource(this, R.array.items_colores, android.R.layout.simple_spinner_dropdown_item)
        selector_color.adapter = selectorAdapter
        formato = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())

        bundle = intent.extras
        if ( bundle!!.getInt("id") > 0){

            edt_nombre.setText(bundle!!.getString("nombre"))
            selector_color.setSelection(getPosicion(bundle!!.getString("color")))
            edt_lugar.setText(bundle!!.getString("lugar"))
            edt_inicio.setText(bundle!!.getString("inicio"))
            edt_fin.setText(bundle!!.getString("fin"))
            edt_alarma.setText(bundle!!.getInt("alarma").toString())
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
                dialogo.dismiss()

            }

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
                    valores.put("alarma", edt_alarma.text.toString())
                    valores.put("descripcion", edt_descripcion.text.toString().trim())

                    if (dbHelper!!.agregarEvento(valores) > 0) {
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
                    val valores = ContentValues()
                    valores.put("id", bundle!!.getInt("id").toString())
                    valores.put("nombre", edt_nombre.text.toString().trim())
                    valores.put("color", selector_color.selectedItem.toString())
                    valores.put("lugar", edt_lugar.text.toString().trim())
                    valores.put("inicio", edt_inicio.text.toString())
                    valores.put("fin", edt_fin.text.toString())
                    valores.put("alarma", edt_alarma.text.toString())
                    valores.put("descripcion", edt_descripcion.text.toString().trim())

                    if (dbHelper!!.actualizarEvento(valores) > 0) {
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
                var alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Quieres eliminar el evento?")
                alertDialog.setPositiveButton("Eliminar", { dialogInterface: DialogInterface, i: Int ->
                    dbHelper!!.eliminarEvento(bundle!!.getInt("id").toString())
                    finish()
                })
                alertDialog.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int -> })
                alertDialog.show()
            }

        }

        return super.onOptionsItemSelected(item)
    }




}
