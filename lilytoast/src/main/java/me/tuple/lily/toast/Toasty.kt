package me.tuple.lily.toast

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.annotation.*
import android.support.v7.widget.CardView
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import me.tuple.lily.R
import me.tuple.lily.core.*
import me.tuple.lily.utils.FontCache

/**
 * Created by LazyLoop.
 */

@Suppress("Unused")
class Toasty(private val context: Context) {

    private lateinit var message: String

    private var undoMessage: String? = null

    @ColorInt
    private var background: Int? = null

    private var duration: Int = Toast.LENGTH_SHORT

    private var icon: Int? = null

    private var typeFace: Typeface? = null

    private var fontSize: Int? = null

    private var undoCallback: ((Boolean) -> Unit)? = null

    private var fontColor: Int? = null

    constructor(context: Context, receiver: Toasty.() -> Unit) : this(context) {
        receiver(this)
    }

    constructor(receiver: Toasty.() -> Unit) : this(Contexter.context) {
        receiver(this)
    }

    fun message(@StringRes message: Int): Toasty {
        message(context.getString(message))
        return this
    }

    fun message(message: String): Toasty {
        this.message = message
        return this
    }

    fun duration(duration: Int): Toasty {
        this.duration = duration
        return this
    }

    fun backgroundRes(@ColorRes bg: Int): Toasty {
        this.background = Contexter.getColor(bg)
        return this
    }

    fun background(@ColorInt bg: Int): Toasty {
        this.background = bg
        return this
    }

    fun icon(@DrawableRes icon: Int): Toasty {
        this.icon = icon
        return this
    }

    fun fontSizeRes(@DimenRes fontSize: Int): Toasty {
        this.fontSize = Contexter.getDimensionPixelSize(fontSize)
        return this
    }

    fun fontSizeDp(fontSize: Int): Toasty {
        this.fontSize = fontSize.dpToPx()
        return this
    }

    fun font(fontName: String): Toasty {
        this.typeFace = FontCache.getFont(fontName)
        return this
    }

    fun font(typeface: Typeface): Toasty {
        this.typeFace = typeFace
        return this
    }

    fun fontColorRes(@ColorRes color: Int): Toasty {
        this.fontColor = Contexter.getColor(color)
        return this
    }

    fun fontColor(@ColorInt fontColor: Int): Toasty {
        this.fontColor = fontColor
        return this
    }

    fun undo(message: String, undoCallback: (Boolean) -> Unit): Toasty {
        this.undoMessage = message
        this.undoCallback = undoCallback
        return this
    }

    fun show() {
        setGlobals()
        val toast = Toast(context)
        toast.duration = duration

        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.toasty_layout, null)
        toast.view = view

        val bgView = view.findById<CardView>(R.id.toast_bg)
        val messageTextView = view.findById<TextView>(R.id.toasty_message)
        val iconView = view.findById<ImageView>(R.id.toasty_icon)
        if (icon == null) {
            iconView.gone()
        } else {
            iconView.setImageResource(icon!!)
        }
        bgView.setCardBackgroundColor(background!!)
        messageTextView.apply {
            text = message
            typeface = typeFace
            setTextColor(fontColor!!)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize!!.toFloat())
        }
        toast.show()
    }

    private fun setGlobals() {
        background = background ?: Contexter.resolveColor(R.attr.toasty_bg, Color.DKGRAY)
        typeFace = typeFace ?: FontCache.getFont(Contexter.resolveString(R.attr.toasty_font))
        fontSize = fontSize ?: Contexter.resolveDimension(R.attr.toasty_font_size, 16.dpToPx())
        fontColor = fontColor ?: Contexter.resolveColor(R.attr.toasty_font_color, Color.WHITE)
    }
}

fun toasty(message: Int) {
    Toasty {
        message(message)
    }.show()
}

fun toasty(message: String) {
    Toasty {
        message(message)
    }.show()
}