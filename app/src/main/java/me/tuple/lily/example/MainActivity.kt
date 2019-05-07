package me.tuple.lily.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import me.tuple.lily.core.async
import me.tuple.lily.core.bind
import me.tuple.lily.core.runOnUI
import me.tuple.lily.core.safeExecute
import me.tuple.lily.toast.toasty
import me.tuple.lily.utils.booleanPreference
import me.tuple.lily.utils.intPreference

class MainActivity : AppCompatActivity() {


    val button by bind<Button>(R.id.click)
    var deom by booleanPreference("Gokul")
    var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            toasty("Hello")
        }
    }
}
