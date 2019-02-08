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

/**
 * intermediate construct used by [styleString] to restrict the auto-complete
 */
open class SpankSection(
    val content: Spannable,
    val start: Int,
    val end: Int,
    @EzFlags val spanFlag: Int
)

/**
 * intermediate construct used by [styleString] to restrict the auto-complete
 */
class SpankParagraph(
    content: Spannable,
    paragraph: Paragraph,
    @EzFlags spanFlag: Int
) : SpankSection(content, paragraph.startIndex, paragraph.endIndex, spanFlag)

/**
 * applies a BOLD style to the specified [SpankSection]
 */
fun SpankSection.bold() = applySpan(
    StyleSpan(Typeface.BOLD)
)


/**
 * applies a ITALIC style to the specified [SpankSection]
 */
fun SpankSection.italic() = applySpan(
    StyleSpan(Typeface.ITALIC)
)

/**
 * applies an UNDERLINE style to the specified [SpankSection]
 */
fun SpankSection.underline() = applySpan(
    UnderlineSpan()
)

/**
 * applies a STRIKE THROUGH style to the specified [SpankSection]
 */
fun SpankSection.strikethrough() = applySpan(
    StrikethroughSpan()
)

/**
 * applies a SUBSCRIPT style to the specified [SpankSection]
 */
fun SpankSection.subscript() = applySpan(
    SubscriptSpan()
)

/**
 * applies a SUPERSCRIPT style to the specified [SpankSection]
 */
fun SpankSection.superscript() = applySpan(
    SuperscriptSpan()
)

/**
 * applies a [color] FOREGROUND style to the specified [SpankSection]
 */
fun SpankSection.foregroundColor(@ColorInt color: Int) = applySpan(
    ForegroundColorSpan(color)
)

/**
 * applies a [color] BACKGROUND style to the specified [SpankSection]
 */
fun SpankSection.backgroundColor(@ColorInt color: Int) = applySpan(
    BackgroundColorSpan(color)
)

/**
 * applies a LINK style to the specified [SpankSection] which points to [url] and is clickable
 */
fun SpankSection.link(url: String) = applySpan(
    URLSpan(url)
)

/**
 * applies the [fontFamily] FONT style to the specified [SpankSection]
 */
fun SpankSection.font(@EzFontFamily fontFamily: String) = applySpan(
    TypefaceSpan(fontFamily)
)

/**
 * applies all the styles in [appearanceRes] to the specified [SpankSection]
 */
fun SpankSection.appearance(context: Context, appearanceRes: Int) = applySpan(
    TextAppearanceSpan(context, appearanceRes)
)

/**
 * changes the LOCALE on the specified [SpankSection]
 */
@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
fun SpankSection.locale(locale: Locale) = applySpan(
    LocaleSpan(locale)
)

/**
 * applies a SCALE on the X axis of [proportion] to the specified [SpankSection]
 */
fun SpankSection.scaleX(proportion: Float) = applySpan(
    ScaleXSpan(proportion)
)

/**
 * applies a SCALING with [proportion] on both axis to the specified [SpankSection]
 */
fun SpankSection.relativeSize(proportion: Float) = applySpan(
    RelativeSizeSpan(proportion)
)

/**
 * applies the SCALING necessary to change the size to [pixels] to the specified [SpankSection]
 */
fun SpankSection.absoluteSize(pixels: Int) = applySpan(
    AbsoluteSizeSpan(pixels)
)

/**
 * applies the SCALING necessary to change the size to [dips] to the specified [SpankSection]
 */
fun SpankSection.absoluteSizeDP(dips: Int) = applySpan(
    AbsoluteSizeSpan(dips, true)
)

/**
 * applies a CLICK LISTENER to the specified [SpankSection], when clicked executes the [block] with
 * the part of the [SpankSection.content] from [SpankSection.start] to [SpankSection.end]
 */
fun SpankSection.clickable(block: (CharSequence) -> Unit) = applySpan(
    ClickableSpank(
        content.subSequence(start, end + 1),
        block
    )
)

/**
 * internal construct used as a intermediate between the Android framework and the library
 */
private class ClickableSpank(
    val spanContent: CharSequence,
    val codeBlock: (CharSequence) -> Unit
) : ClickableSpan() {
    override fun onClick(widget: View) = codeBlock(spanContent)
}

/**
 * adds an ANNOTATION to the specified [SpankSection] (No visual change, is just metadata)
 */
fun SpankSection.customAnnotation(key: String, value: String) = applySpan(
    Annotation(key, value)
)

/**
 * replaces the specified [SpankSection] with the [drawable] aligned to [verticalAlignment],
 * possible values are [DynamicDrawableSpan.ALIGN_BASELINE] or [DynamicDrawableSpan.ALIGN_BOTTOM]
 */
fun SpankSection.image(
    drawable: Drawable,
    sourceUri: String? = null,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) = applySpan(
    if (sourceUri != null) ImageSpan(drawable, sourceUri, verticalAlignment)
    else ImageSpan(drawable, verticalAlignment)
)

/**
 * replaces the specified [SpankSection] with the image from [resId] aligned to [verticalAlignment],
 * possible values are [DynamicDrawableSpan.ALIGN_BASELINE] or [DynamicDrawableSpan.ALIGN_BOTTOM]
 */
fun SpankSection.imageResource(
    context: Context,
    @DrawableRes resId: Int,
    verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM
) = applySpan(
    ImageSpan(context, resId, verticalAlignment)
)

//*********************************************
//*********************************************
//**                Paragraph                **
//**                  Styles                 **
//*********************************************
//*********************************************

/**
 * applies a QUOTE style to the [SpankParagraph] with [quoteColor], [stripeWidth] and [gapWidth]
 */
@RequiresApi(Build.VERSION_CODES.P)
fun SpankParagraph.customQuote(
    @ColorInt quoteColor: Int = -0xffff01,
    @IntRange(from = 0) stripeWidth: Int = 2,
    @IntRange(from = 0) gapWidth: Int = 2
) = applySpan(
    QuoteSpan(quoteColor, stripeWidth, gapWidth)
)

/**
 * applies a QUOTE style to the specified [SpankParagraph] with [quoteColor]
 */
fun SpankParagraph.quote(@ColorInt quoteColor: Int = -0xffff01) = applySpan(
    QuoteSpan(quoteColor)
)

/**
 * aligns the specified [SpankParagraph] to [gravity]
 */
fun SpankParagraph.alignTo(@EzAlign gravity: Int) = applySpan(
    AlignmentSpan.Standard(
        when (gravity) {
            EzAlign.START -> Layout.Alignment.ALIGN_NORMAL
            EzAlign.CENTER -> Layout.Alignment.ALIGN_CENTER
            EzAlign.END -> Layout.Alignment.ALIGN_OPPOSITE
            else -> throw IllegalArgumentException("Unknown gravity parameter: $gravity")
        }
    )
)

/**
 * aligns the specified [SpankParagraph] to [alignment]
 */
fun SpankParagraph.customAlignTo(alignment: AlignmentSpan) = applySpan(
    alignment
)

/**
 * applies a BULLET style to the specified [SpankParagraph] with [bulletColor] and [gapWidth]
 */
fun SpankParagraph.bullet(
    gapWidth: Int = BulletSpan.STANDARD_GAP_WIDTH,
    @ColorInt bulletColor: Int = Color.BLACK
) = applySpan(
    BulletSpan(gapWidth, bulletColor)
)

/**
 * adds the [drawable] with some [padding] the specified [SpankParagraph]
 */
fun SpankParagraph.drawableWithMargin(drawable: Drawable?, padding: Int = 0) = drawable?.let {
    applySpan(
        DrawableMarginSpan(it, padding)
    )
}

/**
 * adds the [bitmap] with some [padding] the specified [SpankParagraph]
 */
fun SpankParagraph.bitmapWithMargin(bitmap: Bitmap, padding: Int = 0) = applySpan(
    IconMarginSpan(bitmap, padding)
)

/**
 * adds some [padding] to the first [numberOfLines] on the specified [SpankParagraph]
 */
fun SpankParagraph.leadingMarginModifier(padding: Int, numberOfLines: Int) = applySpan(
    CustomLeadingMarginSpan(padding, numberOfLines)
)

/**
 * intermediate construct used by [leadingMarginModifier] to add [padding] on a [numberOfLines]
 */
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
        // STUB
    }

    override fun getLeadingMargin(first: Boolean) = if (first) padding else 0

    override fun getLeadingMarginLineCount() = numberOfLines
}

/**
 * applies a TAB STOP style with an [offset] to the specified [SpankParagraph]
 */
fun SpankParagraph.tabStop(offset: Int) = applySpan(
    TabStopSpan.Standard(offset)
)

/**
 * internal method to apply the [span] to the content, used by all the styles
 */
private fun SpankSection.applySpan(span: Any) = content.setSpan(span, start, end + 1, spanFlag)
