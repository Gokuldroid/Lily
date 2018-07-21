package me.tuple.lily.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * Created by LazyLoop.
 */
fun Activity.sendMail(chooserTitle: String, email: String, subject: String, body: String?) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    body?.also { intent.putExtra(Intent.EXTRA_TEXT, body) }
    this.startActivity(Intent.createChooser(intent, chooserTitle))
}

inline fun AppCompatActivity.supportFragmentTransaction(receiver: FragmentTransaction.() -> Unit) {
    val manager = supportFragmentManager.beginTransaction()
    receiver.invoke(manager)
    manager.commit()
}

inline  fun AppCompatActivity.fragmentTransaction(receiver: android.app.FragmentTransaction.() -> Unit) {
    val manager = fragmentManager.beginTransaction()
    receiver.invoke(manager)
    manager.commit()
}