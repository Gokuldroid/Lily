package me.tuple.lily.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import me.tuple.lily.core.Contexter
import me.tuple.lily.core.bind
import me.tuple.lily.utils.intPreference
import java.util.*

class MainActivity : AppCompatActivity() {


    var deom by intPreference("Gokul")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button by bind<Button>(R.id.click)
        var inn by intPreference("Hello")
        button.setOnClickListener({
            Log.d("D","Inn ::" +inn)
            inn = Random().nextInt(20)
            Log.d("D","demo ::" +deom)
            deom++
        })
    }

}
