@file:JvmName("SpankStyles")

package com.bunk3r.spank

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Annotation
import android.text.Layout
import android.text.Spannable
import android.text.style.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.annotation.RequiresApi
import java.util.*

open class SpankSection(
    val content: Spannable,
    val start: Int,
    val end: Int,
    @EzFlags val spanFlag: Int
)

class SpankParagraph(
    content: Spannable,
    paragraph: Paragraph,
    @EzFlags spanFlag: Int
) : SpankSection(content, paragraph.startIndex, paragraph.endIndex, spanFlag)

fun SpankSection.bold() {
    applySpan(
        StyleSpan(
            Typeface.BOLD
        )
    )
}

fun SpankSection.italic() {
    applySpan(
        StyleSpan(
            Typeface.ITALIC
        )
    )
}

fun SpankSection.underline() {
    applySpan(
        UnderlineSpan()
    )
}

fun SpankSection.strikethrough() {
    applySpan(
        StrikethroughSpan()
    )
}

fun SpankSection.subscript() {
    applySpan(
        SubscriptSpan()
    )
}

fun SpankSection.superscript() {
    applySpan(
        SuperscriptSpan()
    )
}

fun SpankSection.foregroundColor(@ColorInt foregroundColor: Int) {
    applySpan(
        ForegroundColorSpan(
            foregroundColor
        )
    )
}

fun SpankSection.backgroundColor(@ColorInt backgroundColor: Int) {
    applySpan(
        BackgroundColorSpan(
            backgroundColor
        )
    )
}

fun SpankSection.link(url: String) {
    applySpan(
        URLSpan(
            url
        )
    )
}

fun SpankSection.font(@EzFontFamily fontFamily: String) {
    applySpan(
        TypefaceSpan(
            fontFamily
        )
    )
}

fun SpankSection.appearance(context: Context, appearanceRes: Int) {
    applySpan(
        TextAppearanceSpan(
            context, appearanceRes
        )
    )
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun SpankSection.locale(locale: Locale) {
    applySpan(
        LocaleSpan(
            locale
        )
    )
}

fun SpankSection.scaleX(proportion: Float) {
    applySpan(
        ScaleXSpan(
            proportion
        )
    )
}

fun SpankSection.relativeSize(proportion: Float) {
    applySpan(
        RelativeSizeSpan(
            proportion
        )
    )
}

fun SpankSection.absoluteSize(pixels: Int) {
    applySpan(
        AbsoluteSizeSpan(
            pixels
        )
    )
}

fun SpankSection.absoluteSizeDP(dips: Int) {
    applySpan(
        AbsoluteSizeSpan(
            dips, true
        )
    )
}

fun SpankSection.clickable(block: (CharSequence) -> Unit) {
    applySpan(
        ClickableSpank(
            content.subSequence(start, end + 1), block
        )
    )
}

private class ClickableSpank(
    val spanContent: CharSequence,
    val codeBlock: (CharSequence) -> Unit
) : ClickableSpan() {
    override fun onClick(widget: View) {
        codeBlock(spanContent)
    }
}

fun SpankSection.customAnnotation(key: String, value: String) {
    applySpan(
        Annotation(
            key, value
        )
    )
}

fun SpankSection.image(
    drawable: Drawable,
    sourceUri: String? = null,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) {
    val span =
        if (sourceUri != null) ImageSpan(drawable, sourceUri, verticalAlignment)
        else ImageSpan(drawable, verticalAlignment)

    applySpan(
        span
    )
}

fun SpankSection.imageResource(
    context: Context,
    @DrawableRes resId: Int,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) {
    applySpan(
        ImageSpan(
            context, resId, verticalAlignment
        )
    )
}

//*********************************************
//*********************************************
//**                Paragraph                **
//**                  Styles                 **
//*********************************************
//*********************************************

@RequiresApi(Build.VERSION_CODES.P)
fun SpankParagraph.customQuote(
    @ColorInt quoteColor: Int = -0xffff01,
    @IntRange(from = 0) stripeWidth: Int = 2,
    @IntRange(from = 0) gapWidth: Int = 2
) {
    applySpan(
        QuoteSpan(quoteColor, stripeWidth, gapWidth)
    )
}

fun SpankParagraph.quote(@ColorInt quoteColor: Int = -0xffff01) {
    applySpan(
        QuoteSpan(
            quoteColor
        )
    )
}

fun SpankParagraph.alignTo(@EzAlign gravity: Int) {
    applySpan(
        AlignmentSpan.Standard(
            when (gravity) {
                EzAlign.START -> Layout.Alignment.ALIGN_NORMAL
                EzAlign.CENTER -> Layout.Alignment.ALIGN_CENTER
                EzAlign.END -> Layout.Alignment.ALIGN_OPPOSITE
                else -> throw IllegalArgumentException("Unknown gravity $gravity, make sure to pass a valid value")
            }
        )
    )
}

fun SpankParagraph.customAlignTo(alignment: AlignmentSpan) {
    applySpan(
        alignment
    )
}

fun SpankParagraph.bullet(
    gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
    @ColorInt bulletColor: Int = Color.TRANSPARENT
) {
    applySpan(
        BulletSpan(
            gapWidth, bulletColor
        )
    )
}

fun SpankParagraph.drawableWithMargin(
    drawable: Drawable?,
    padding: Int = 0
) {
    if (drawable != null) {
        applySpan(
            DrawableMarginSpan(
                drawable, padding
            )
        )
    }
}

fun SpankParagraph.bitmapWithMargin(
    bitmap: Bitmap,
    padding: Int = 0
) {
    applySpan(
        IconMarginSpan(
            bitmap, padding
        )
    )
}

fun SpankParagraph.leadingMarginModifier(
    padding: Int,
    numberOfLines: Int
) {
    applySpan(
        CustomLeadingMarginSpan(
            padding, numberOfLines
        )
    )
}

private class CustomLeadingMarginSpan(
    private val padding: Int,
    private val numberOfLines: Int
) : LeadingMarginSpan.LeadingMarginSpan2 {
    override fun drawLeadingMargin(
        c: Canvas?,
        p: Paint?,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence?,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
    }

    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) padding
        else 0
    }

    override fun getLeadingMarginLineCount(): Int {
        return numberOfLines
    }
}

fun SpankParagraph.tabStop(offset: Int) {
    applySpan(
        TabStopSpan.Standard(
            offset
        )
    )
}

private fun SpankSection.applySpan(span: Any) {
    content.setSpan(span, start, end + 1, spanFlag)
}
