package me.tuple.lily.utils

import android.graphics.Typeface
import me.tuple.lily.core.Contexter
import me.tuple.lily.core.safeExecute
import java.util.*


/**
 * Created by LazyLoop.
 */

object FontCache {
    private val fontCache = HashMap<String, Typeface>()

    fun getFont(font: String?, defaultFont: Typeface = Typeface.DEFAULT): Typeface {
        if (font.isNullOrBlank()) {
            return Typeface.DEFAULT
        }
        return if (fontCache.containsKey(font)) {
            fontCache[font]!!
        } else {
            var typeface: Typeface? = null
            safeExecute { typeface = Typeface.createFromAsset(Contexter.getAssets(), "fonts/" + font) }
            fontCache.put(font!!, typeface ?: defaultFont)
            typeface ?: defaultFont
        }
    }
}
