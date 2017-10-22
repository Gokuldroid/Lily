package me.tuple.lily.core

import android.app.Activity
import android.content.Intent
import android.net.Uri

/**
 * Created by LazyLoop.
 */
fun Activity.sendMail(activity: Activity, chooserTitle: String, email: String, subject: String, body: String?) {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    body?.also { intent.putExtra(Intent.EXTRA_TEXT, body) }
    this.startActivity(Intent.createChooser(intent, chooserTitle))
}