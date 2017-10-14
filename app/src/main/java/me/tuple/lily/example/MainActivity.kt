package me.tuple.lily.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import me.tuple.lily.core.async
import me.tuple.lily.core.bind
import me.tuple.lily.core.runOnUI
import me.tuple.lily.utils.intPreference

class MainActivity : AppCompatActivity() {


    val button by bind<Button>(R.id.click)
    var deom by intPreference("Gokul")
    var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var inn by intPreference("Hello")
        async {
            runOnUI {

            }
        }
    }
}
