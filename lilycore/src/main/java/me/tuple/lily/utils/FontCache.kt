package me.tuple.lily.utils

import android.graphics.Typeface
import android.support.annotation.Nullable
import me.tuple.lily.core.Contexter
import me.tuple.lily.core.safeExecute
import java.util.*


/**
 * Created by LazyLoop.
 */

object FontCache {
    private val fontCache = HashMap<String, Typeface>()

    fun getFont(font: String): Typeface {
        if (fontCache.containsKey(font)) {
            return fontCache[font]!!
        } else {
            val typeface: Typeface = safeExecute<Typeface> { return Typeface.createFromAsset(Contexter.getAssets(), "fonts/" + font) } ?: Typeface.DEFAULT
            fontCache.put(font, typeface)
            return typeface
        }
    }
}
