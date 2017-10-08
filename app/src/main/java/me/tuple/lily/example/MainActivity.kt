package me.tuple.lily.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import me.tuple.lily.core.bind
import me.tuple.lily.core.findById
import me.tuple.lily.core.onClick
import me.tuple.lily.toast.Toasty
import me.tuple.lily.utils.intPreference

class MainActivity : AppCompatActivity() {


    val button by bind<Button>(R.id.click)
    var deom by intPreference("Gokul")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var inn by intPreference("Hello")
        onClick(R.id.click) {
            Toasty(this) {
                message("Hello")
                icon(R.mipmap.ic_launcher)
            }.show()
        }
        findById<Button>(R.id.click).apply {
            setOnClickListener{

            }
        }
    }
}
