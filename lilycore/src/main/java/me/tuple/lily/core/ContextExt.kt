package me.tuple.lily.core

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.support.annotation.*
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.WindowManager
import kotlin.reflect.KClass


/**
 * Created by LazyLoop.
 */

val Context.layoutInflater: LayoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

val Context.clipboardManager: ClipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

fun Context.hasPermission(permission: String): Boolean =
        isApiBelow(AndroidVersion.M) || ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

fun Context.hasStoragePermission(): Boolean =
        hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)

fun Context.hasSettingsPermission(): Boolean = hasPermission(Manifest.permission.WRITE_SETTINGS)

fun Context.filterNotGrantedPermission(permissions: Array<String>): Array<String> =
        permissions.filter { !hasPermission(it) }.toTypedArray()

fun Context.openAppDetails() {
    val i = Intent()
    i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    i.addCategory(Intent.CATEGORY_DEFAULT)
    i.data = Uri.parse("package:" + packageName)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    startActivity(i)
}

fun Context.openInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW,
            Uri.parse(url))
    intent.apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent);
}

fun Context.getVersionName(): String {
    safeExecute {
        val info = this.packageManager.getPackageInfo(this.packageName, 0)
        return info.versionName
    }
    return ""
}

fun Context.openInPlayStore(packageName: String) {
    val uri = Uri.parse("market://details?id=" + packageName)
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        this.startActivity(goToMarket)
    } catch (e: ActivityNotFoundException) {
        openInBrowser("http://play.google.com/store/apps/details?id=" + this.packageName)
    }
}

fun Context.startActivity(activity: KClass<out Activity>) {
    val intent = Intent(this, activity.java)
    intent.action = Intent.ACTION_MAIN
    startActivity(intent)
}

@RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
fun Context.hasInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected
}


fun Context.isGPSEnabled(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}


fun Context.getWindowWidth(): Int {
    val metrics = getDisplayMetrics()
    return metrics.widthPixels
}

fun Context.getWindowHeight(): Int {
    val metrics = getDisplayMetrics()
    return metrics.heightPixels
}


fun Context.getWindowWidthInDp(): Float {
    val metrics = getDisplayMetrics()
    return metrics.widthPixels / metrics.density
}

fun Context.getWindowHeightInDp(): Float {
    val metrics = getDisplayMetrics()
    return metrics.heightPixels / metrics.density
}

fun Context.getDisplayMetrics(): DisplayMetrics {
    val display = windowManager.defaultDisplay
    val metrics = DisplayMetrics()
    display.getMetrics(metrics)
    return metrics
}

@SuppressLint("StaticFieldLeak")
object Contexter {
    lateinit var context: Context
    val resources by lazy {
        context.resources as Resources
    }

    fun init(context: Context) {
        this.context = context
    }

    fun openAppDetailsActivity() {
        context.openAppDetails()
    }

    fun dpToPixels(dp: Int): Int {
        val metrics = resources.displayMetrics
        return Math.round(dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun pixelsToDp(pixels: Int): Int {
        val metrics = resources.displayMetrics
        return Math.round(pixels * (DisplayMetrics.DENSITY_DEFAULT.toFloat() / metrics.densityDpi))
    }

    fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(context, colorId)

    fun getBoolean(@BoolRes boolId: Int): Boolean = resources.getBoolean(boolId)

    fun getString(@StringRes stringId: Int): String = resources.getString(stringId)

    fun getString(@StringRes stringId: Int, vararg params: Any): String =
            resources.getString(stringId, params)

    fun getInt(@IntegerRes intId: Int): Int = resources.getInteger(intId)

    fun getDrawable(@DrawableRes icDrawable: Int): Drawable =
            ContextCompat.getDrawable(context, icDrawable)!!

    fun getDimensionPixelSize(@DimenRes dimensionId: Int) =
            resources.getDimensionPixelSize(dimensionId)

    fun getColorDrawable(@ColorRes color: Int): ColorDrawable = ColorDrawable(getColor(color))

    fun getAssets(): AssetManager = context.assets

    @ColorInt
    fun resolveColor(@AttrRes attr: Int, fallback: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getColor(0, fallback)
        } finally {
            a.recycle()
        }
    }

    fun resolveBoolean(@AttrRes attr: Int, fallback: Boolean = false): Boolean {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getBoolean(0, fallback)
        } finally {
            a.recycle()
        }
    }

    fun resolveString(@AttrRes attr: Int): String? {
        val v = TypedValue()
        if (context.theme.resolveAttribute(attr, v, true)) {
            return v.string as String
        }
        return null
    }

    fun resolveDimension(@AttrRes attr: Int, fallback: Int): Int {
        val a = context.theme.obtainStyledAttributes(intArrayOf(attr))
        try {
            return a.getDimensionPixelSize(0, fallback)
        } finally {
            a.recycle()
        }
    }
}

fun Int.dpToPx(): Int = Contexter.dpToPixels(this)

fun Int.pxToDp(): Int = Contexter.pixelsToDp(this)
