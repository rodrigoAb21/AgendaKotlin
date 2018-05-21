package com.uagrm.rodrigoab.agendakotlin

import android.content.ContentValues
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.uagrm.rodrigoab.agendakotlin.DBHelper.DBHelper
import kotlinx.android.synthetic.main.activity_formulario.*

class formulario : AppCompatActivity() {

    var bundle : Bundle ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formulario)

        bundle = intent.extras
        if ( bundle!!.getInt("id") > 0){
            edt_nombre.setText(bundle!!.getString("nombre"))
            edt_direccion.setText(bundle!!.getString("direccion"))
            edt_descripcion.setText(bundle!!.getString("descripcion"))
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
        val dbHelper = DBHelper(this)

        when (item!!.itemId) {
            R.id.menu_btn_add -> {
                val valores = ContentValues()
                valores.put("nombre", edt_nombre.text.toString())
                valores.put("direccion", edt_direccion.text.toString())
                valores.put("descripcion", edt_descripcion.text.toString())

                if (dbHelper.agregarEvento(valores) > 0) {
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo agregar el evento", Toast.LENGTH_LONG).show()
                }

            }

            R.id.menu_btn_update -> {
                val valores = ContentValues()
                valores.put("id", bundle!!.getInt("id"))
                valores.put("nombre", edt_nombre.text.toString())
                valores.put("direccion", edt_direccion.text.toString())
                valores.put("descripcion", edt_descripcion.text.toString())

                if (dbHelper.actualizarEvento(valores) > 0) {
                    finish()
                } else {
                    Toast.makeText(this, "No se pudo editar el evento", Toast.LENGTH_LONG).show()
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
