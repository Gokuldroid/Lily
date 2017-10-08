package me.tuple.lily.toast

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.*
import android.widget.Toast
import me.tuple.lily.core.Contexter
import me.tuple.lily.core.dpToPx
import me.tuple.lily.utils.FontCache

/**
 * Created by LazyLoop.
 */
@Suppress("Unused")
class Toasty(private val context: Context) {

    private var message: String? = null

    private var undoMessage: String? = null

    @ColorRes
    private var background: Int = Color.DKGRAY

    private var duration: Int = Toast.LENGTH_SHORT

    private var icon: Int? = null

    private var typeFace: Typeface? = null

    private var fontSize: Int? = null

    private var undoCallback: ((Boolean) -> Unit)? = null

    constructor(context: Context, receiver: Toasty.() -> Unit) : this(context) {
        receiver(this)
    }

    fun message(@StringRes message: Int) {
        message(context.getString(message))
    }

    fun message(message: String) {
        this.message = message
    }

    fun duration(duration: Int) {
        this.duration = duration
    }

    fun backgroundRes(@ColorRes bg: Int) {
        this.background = Contexter.getColor(bg)
    }

    fun background(@ColorInt bg: Int) {
        this.background = bg
    }

    fun icon(@DrawableRes icon: Int) {
        this.icon = icon
    }

    fun fontSizeRes(@DimenRes fontSize: Int) {
        this.fontSize = Contexter.getDimension(fontSize)
    }

    fun fontSizeDp(fontSize: Int) {
        this.fontSize = fontSize.dpToPx()
    }

    fun font(fontName: String) {
        this.typeFace = FontCache.getFont(fontName)
    }

    fun font(typeface: Typeface) {
        this.typeFace = typeFace
    }

    fun undo(message: String, undoCallback: (Boolean) -> Unit) {
        this.undoMessage = message
        this.undoCallback = undoCallback
    }
}