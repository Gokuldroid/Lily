package me.tuple.lily.example

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import me.tuple.lily.core.bind
import me.tuple.lily.toast.toasty

class MainActivity : AppCompatActivity() {

    val button by bind<Button>(R.id.click)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            toasty("Gokul")
        }
    }
}
