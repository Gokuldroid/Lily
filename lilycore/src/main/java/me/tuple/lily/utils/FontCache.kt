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
        if (fontCache.containsKey(font)) {
            return fontCache[font]!!
        } else {
            val typeface: Typeface = safeExecute<Typeface> { return Typeface.createFromAsset(Contexter.getAssets(), "fonts/" + font) } ?: defaultFont
            fontCache.put(font!!, typeface)
            return typeface
        }
    }
}
