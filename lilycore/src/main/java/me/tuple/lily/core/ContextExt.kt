package me.tuple.lily.core

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.LayoutInflater


/**
 * Created by LazyLoop.
 */

public val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

public val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

public fun Context.hasPermission(permission: String): Boolean {
    return isApiBelow(Build.VERSION_CODES.M) || ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasStoragePermission(): Boolean {
    return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Context.hasSettingsPermission(): Boolean {
    return hasPermission(Manifest.permission.WRITE_SETTINGS)
}

fun Context.filterNotGrantedPermission(permissions: Array<String>): Array<String> {
    return permissions.filter { !hasPermission(it) }.toTypedArray()
}

fun Context.openAppDetailsActivity() {
    val i = Intent()
    i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    i.addCategory(Intent.CATEGORY_DEFAULT)
    i.data = Uri.parse("package:" + packageName)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivity(i)
}

object Contexter {
    lateinit var context: Context
    val resources by lazy {
        context.resources as Resources
    }

    fun init(context: Context) {
        this.context = context
    }

    fun openAppDetailsActivity() {
        context.openAppDetailsActivity()
    }

    fun dpToPixels(dp: Int): Int {
        val metrics = resources.displayMetrics
        return Math.round(dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun pixelsToDp(pixels: Int): Int {
        val metrics = resources.displayMetrics
        return Math.round(pixels * (DisplayMetrics.DENSITY_DEFAULT.toFloat() / metrics.densityDpi))
    }

    fun getColor(@ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    fun getBoolean(@BoolRes boolId: Int): Boolean {
        return resources.getBoolean(boolId)
    }

    fun getString(@StringRes stringId: Int): String {
        return resources.getString(stringId)
    }

    fun getString(@StringRes stringId: Int,vararg params:Any): String {
        return resources.getString(stringId,params)
    }

    fun getInt(@IntegerRes intId: Int): Int {
        return resources.getInteger(intId)
    }

    fun getDrawable(@DrawableRes icDrawable: Int): Drawable {
        return ContextCompat.getDrawable(context, icDrawable)
    }

    fun getColorDrawable(@ColorRes color: Int): ColorDrawable {
        return ColorDrawable(getColor(color))
    }

    fun getAssets(): AssetManager {
        return context.assets
    }
}

fun Int.dpToPx(): Int = Contexter.dpToPixels(this)

fun Int.pxToDp(): Int = Contexter.pixelsToDp(this)
