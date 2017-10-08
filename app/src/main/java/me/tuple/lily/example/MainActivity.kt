package me.tuple.lily.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import me.tuple.lily.core.bind
import me.tuple.lily.core.onClick
import me.tuple.lily.toast.Snacky
import me.tuple.lily.toast.toasty
import me.tuple.lily.utils.intPreference

class MainActivity : AppCompatActivity() {


    val button by bind<Button>(R.id.click)
    var deom by intPreference("Gokul")
    var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var inn by intPreference("Hello")
        onClick(R.id.click) {
            Snacky(this).apply {
                title("Title")
                message("Gokul")
                action("Dismiss")
                num = num.inc()
                if (num.rem(2) == 0) {
                    toasty("Top")
                    onTop()
                } else {
                    toasty("Bottom")
                }
            }.show()
        }
    }
}
