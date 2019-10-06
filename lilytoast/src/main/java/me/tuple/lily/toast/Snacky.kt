package me.tuple.lily.toast

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import me.tuple.lily.R
import me.tuple.lily.core.*
import me.tuple.lily.utils.FontCache


/**
 * Created by LazyLoop.
 */

class Snacky : LinearLayout {
    private var activity: Activity

    constructor(activity: Activity) : this(activity, null)

    private constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    private constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.activity = context as Activity
        inflate(context, R.layout.snacky_layout, this)
    }

    private var icon: Int? = null
    private var background: Int? = null
    private var title: String? = null
    private var message: String? = null
    private var action: String? = null
    private var titleColor: Int? = null
    private var messageColor: Int? = null
    private var actionColor: Int? = null
    private var typeFace: Typeface? = null
    private var layoutGravity: Int = Gravity.BOTTOM

    private val iconView by bind<ImageView>(R.id.snacky_icon)
    private val titleView by bind<TextView>(R.id.snacky_title)
    private val messageView by bind<TextView>(R.id.snacky_message)
    private val actionView by bind<TextView>(R.id.snacky_action)
    private val snackyView by bind<LinearLayout>(R.id.snacky)

    fun title(@StringRes title: Int): Snacky {
        title(context.getString(title))
        return this
    }

    fun title(title: String): Snacky {
        this.title = title
        return this
    }

    fun message(@StringRes message: Int): Snacky {
        message(context.getString(message))
        return this
    }

    fun message(message: String): Snacky {
        this.message = message
        return this
    }

    fun action(@StringRes action: Int): Snacky {
        action(context.getString(action))
        return this
    }

    fun action(action: String): Snacky {
        this.action = action
        return this
    }

    fun icon(@DrawableRes icon: Int): Snacky {
        this.icon = icon
        return this
    }

    fun backgroundRes(@ColorRes bg: Int): Snacky {
        this.background = Contexter.getColor(bg)
        return this
    }

    fun background(@ColorInt bg: Int): Snacky {
        this.background = bg
        return this
    }


    fun titleColorRes(@ColorRes bg: Int): Snacky {
        this.titleColor = Contexter.getColor(bg)
        return this
    }

    fun titleColor(@ColorInt bg: Int): Snacky {
        this.titleColor = bg
        return this
    }

    fun messageColorRes(@ColorRes bg: Int): Snacky {
        this.messageColor = Contexter.getColor(bg)
        return this
    }

    fun messageColor(@ColorInt bg: Int): Snacky {
        this.messageColor = bg
        return this
    }

    fun actionColorRes(@ColorRes bg: Int): Snacky {
        this.actionColor = Contexter.getColor(bg)
        return this
    }

    fun actionColor(@ColorInt bg: Int): Snacky {
        this.actionColor = bg
        return this
    }

    fun font(fontName: String): Snacky {
        this.typeFace = FontCache.getFont(fontName)
        return this
    }

    fun font(typeface: Typeface): Snacky {
        this.typeFace = typeFace
        return this
    }

    fun onTop(): Snacky {
        this.layoutGravity = Gravity.TOP
        return this
    }

    fun show() {
        val decorView = activity.window.decorView as ViewGroup
        val content = decorView.findById<ViewGroup>(android.R.id.content)
        if (this.parent == null) {
            val container = if (this.layoutGravity == Gravity.BOTTOM)
                content
            else
                decorView

            container.forEach {
                if (it is Snacky) {
                    return
                }
            }
            setGlobals()
            constructView()
            container.addView(this)
        }
    }

    private fun constructView() {
        if (title.isNullOrBlank().not()) {
            with(titleView) {
                show()
                text = title
                setTypeface(typeFace, Typeface.BOLD)
                setTextColor(titleColor!!)
            }
        }

        if (message.isNullOrBlank().not()) {
            with(messageView) {
                show()
                text = message
                typeface = typeFace
                setTextColor(messageColor!!)
            }
        }

        if (action.isNullOrBlank().not()) {
            with(actionView) {
                show()
                text = action
                typeface = typeFace
                setTextColor(actionColor!!)
            }
        }

        if (icon != null) {
            with(iconView) {
                setImageResource(icon!!)
            }
        }
        with(snackyView) {
            setBackgroundColor(this@Snacky.background!!)
            if (layoutGravity == Gravity.TOP) {
                val params = layoutParams as LayoutParams
                params.gravity = Gravity.TOP
                layoutParams = params
            } else {
                setPadding(16.dpToPx())
            }
        }
        setAnimations()
    }

    private var slideOutAnimation: Animation? = null

    private var slideOutAnimationDuration: Long? = null

    private fun setAnimations() {
        val slideInAnimation = AnimationUtils.loadAnimation(context,
                if (layoutGravity == Gravity.BOTTOM) R.anim.slide_in_from_bottom else R.anim.slide_in_from_top)
        slideInAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                postDelayed({ dismiss() }, 2000)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        animation = slideInAnimation

        slideOutAnimation = AnimationUtils.loadAnimation(context,
                if (layoutGravity == Gravity.BOTTOM) R.anim.slide_out_to_bottom else R.anim.slide_out_to_top)
        slideOutAnimationDuration = slideOutAnimation!!.duration
    }

    private fun dismiss() {
        slideOutAnimation!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                removeFromParent()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        startAnimation(slideOutAnimation)
    }

    private fun removeFromParent() {
        postDelayed({
            val parent = parent
            if (parent != null) {
                this.clearAnimation()
                (parent as ViewGroup).removeView(this)
            }
        }, 200)
    }

    private fun setGlobals() {
        background = background ?: Contexter.resolveColor(R.attr.snacky_bg, Color.DKGRAY)
        val fallbackTextColor: Int = Contexter.resolveColor(R.attr.snacky_text_color, Color.WHITE)
        titleColor = titleColor ?: Contexter.resolveColor(R.attr.snacky_title_text_color, fallbackTextColor)
        messageColor = messageColor ?: Contexter.resolveColor(R.attr.snacky_message_text_color, fallbackTextColor)
        actionColor = actionColor ?: Contexter.resolveColor(R.attr.snacky_action_text_color, fallbackTextColor)
        typeFace = typeFace ?: FontCache.getFont(Contexter.resolveString(R.attr.toasty_font))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (this.layoutGravity == Gravity.TOP) {
            super.onLayout(changed, l, 0, r, measuredHeight)
        } else {
            super.onLayout(changed, l, t, r, b)
        }
    }
}