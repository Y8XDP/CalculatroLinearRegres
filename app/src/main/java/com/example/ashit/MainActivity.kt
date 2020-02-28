package com.example.ashit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_add_xy.view.*
import kotlinx.android.synthetic.main.item_xy.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun solve(view: View){
        if (Data.dataList.size > 1){
            startActivity(Intent(this, ResultActivity::class.java))
        }else{
            Toast.makeText(applicationContext, "Нужно хотя бы 2 значения", Toast.LENGTH_SHORT).show()
        }
    }

    fun add(view: View){
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_xy, null)

        val builder = AlertDialog.Builder(this)
            .setTitle("Новая строка")
            .setView(dialogView)
            .setPositiveButton("Добавить")  { dialog, _ ->
                if (dialogView.xEdit.text.isNotEmpty() && dialogView.yEdit.text.isNotEmpty()){
                    val x = dialogView.xEdit.text.toString()
                    val y = dialogView.yEdit.text.toString()

                    val item = ItemXY(x.toFloat(), y.toFloat())

                    Data.dataList.add(item)
                    addToVarList(item)
                    dialog.dismiss()
                }
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun addToVarList(item: ItemXY){
        val view = layoutInflater.inflate(R.layout.item_xy, null)

        view.xText.text = item.x.toString()
        view.yText.text = item.y.toString()

        container.addView(view)
    }

    override fun onDestroy() {
        Data.dataList.clear()
        super.onDestroy()
    }
}
