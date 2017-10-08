package me.tuple.lily.example

import android.app.Application
import me.tuple.lily.core.Contexter
import me.tuple.lily.toast.Toasty

/**
 * Created by LazyLoop.
 */

public class App:Application(){
    override fun onCreate() {
        super.onCreate()
        Contexter.init(applicationContext)
    }
}