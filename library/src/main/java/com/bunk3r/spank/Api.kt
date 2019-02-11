@file:JvmName("SpanK")

package com.bunk3r.spank

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.annotation.IntRange
import androidx.annotation.StringRes

/**
 * Intermediate construct used by [styleString] to reduce the scope of the auto-complete
 */
sealed class SpankBuilderBase(
    val context: Context,
    val content: SpannableStringBuilder
) {
    val paragraphs: List<Paragraph> by lazy {
        content.toParagraphs()
    }

    fun build(): Spanned {
        return content
    }
}

class SpankBuilder(
    context: Context,
    content: SpannableStringBuilder
) : SpankBuilderBase(context, content)

class SpankBuilderWithTokenSupport(
    context: Context,
    content: SpannableStringBuilder,
    val openingTokenFormat: String,
    val closingTokenFormat: String
) : SpankBuilderBase(context, content)

/**
 * Creates a [Spanned] version of the [content] with all styles inside [block]
 */
inline fun Context.styleString(
    content: String,
    block: SpankBuilder.() -> Unit
): Spanned {
    val spannableBuilder = SpannableStringBuilder(content)
    val builder = SpankBuilder(this, spannableBuilder)
    builder.block()
    return builder.build()
}

/**
 * Creates a [Spanned] version of the value of the [resId] with all styles inside [block]
 */
inline fun Context.styleString(
    @StringRes resId: Int,
    block: SpankBuilder.() -> Unit
) = styleString(getString(resId), block)

/**
 * Creates a [Spanned] version of the [content] with all styles inside [block]
 * This adds support for "tokenizing" your content by passing the [openingTokenFormat] and
 * [closingTokenFormat] that will be used with [token]
 */
inline fun Context.styleStringWithTokens(
    content: String,
    openingTokenFormat: String,
    closingTokenFormat: String = openingTokenFormat,
    block: SpankBuilderWithTokenSupport.() -> Unit
): Spanned {
    val builder = SpankBuilderWithTokenSupport(
        this,
        SpannableStringBuilder(content),
        openingTokenFormat,
        closingTokenFormat
    )
    builder.block()
    return builder.build()
}

/**
 * Creates a [Spanned] version of the value of the [resId] with all styles inside [block]
 * This adds support for "tokenizing" your content by passing the [openingTokenFormat] and
 * [closingTokenFormat] that will be used with [token]
 */
inline fun Context.styleStringWithTokens(
    @StringRes resId: Int,
    openingTokenFormat: String,
    closingTokenFormat: String = openingTokenFormat,
    block: SpankBuilderWithTokenSupport.() -> Unit
) = styleStringWithTokens(getString(resId), openingTokenFormat, closingTokenFormat, block)

/**
 * Deletes the opening and closing [token]s from the content and applies the styles on [block] to
 * the content that was inside the opening and closing [token]s
 */
inline fun SpankBuilderWithTokenSupport.token(
    token: String,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val openingToken = openingTokenFormat.format(token)
    val closingToken = closingTokenFormat.format(token)

    val openingTokenIndex = content.indexOf(openingToken)
    val closingTokenIndex = content.indexOf(closingToken)

    if ((openingTokenIndex == NOT_FOUND) or (closingTokenIndex == NOT_FOUND)) {
        throw IllegalStateException("$openingToken or $closingToken not found in the content")
    }

    content.delete(closingTokenIndex, closingTokenIndex + closingToken.length)
    content.delete(openingTokenIndex, openingTokenIndex + openingToken.length)

    SpankSection(
        content,
        openingTokenIndex,
        closingTokenIndex - openingToken.length - 1,
        spanFlag
    ).block()
}

/**
 * Applies the styles on [block] to the specified range([start], [end]) inclusive in both sides
 */
inline fun SpankBuilderBase.range(
    @IntRange(from = 0) start: Int,
    @IntRange(from = 1) end: Int,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    if ((start >= content.length) or (end < start) or (end >= content.length)) {
        throw IllegalStateException(
            "Range($start, $end) is outside the boundaries of the content (0, ${content.length})"
        )
    }

    SpankSection(content, start, end, spanFlag).block()
}

/**
 * Applies the styles on [block] to the specified [range] inclusive in both sides
 */
inline fun SpankBuilderBase.range(
    range: Pair<Int, Int>,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) = range(range.first, range.second, spanFlag, block)

/**
 * Applies the styles on [block] to the whole content
 */
inline fun SpankBuilderBase.all(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = Paragraph(0, content.length - 1)
    SpankParagraph(content, paragraph, spanFlag).block()
}

/**
 * Applies the styles on [block] to the first appearance of the [excerpt] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.firstAppearanceOf(
    excerpt: String,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val start = content.indexOf(excerpt, ignoreCase = ignoreCase)
    if (start == NOT_FOUND) {
        throw IllegalStateException("Couldn't found $excerpt in $content")
    }

    val end = start + excerpt.length - 1
    SpankSection(content, start, end, spanFlag).block()
}

/**
 * Applies the styles on [block] to the first appearance of the value of the [resId] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.firstAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) = firstAppearanceOf(context.getString(resId), ignoreCase, spanFlag, block)

/**
 * Applies the styles on [block] to the last appearance of the [excerpt] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.lastAppearanceOf(
    excerpt: String,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val start = content.lastIndexOf(excerpt, ignoreCase = ignoreCase)
    if (start == NOT_FOUND) {
        throw IllegalStateException("Couldn't found $excerpt in $content")
    }

    val end = start + excerpt.length - 1
    SpankSection(content, start, end, spanFlag).block()
}

/**
 * Applies the styles on [block] to the last appearance of the value of the [resId] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.lastAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) = lastAppearanceOf(context.getString(resId), ignoreCase, spanFlag, block)

/**
 * Applies the styles on [block] to all appearances of the [excerpt] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.allAppearanceOf(
    excerpt: String,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    var start = 0
    do {
        val nextAppearance = content.indexOf(excerpt, start, ignoreCase = ignoreCase)
        if (nextAppearance != NOT_FOUND) {
            val end = nextAppearance + excerpt.length - 1
            SpankSection(content, nextAppearance, end, spanFlag).block()
            start = end + 1
        }
    } while (nextAppearance != NOT_FOUND)
}

/**
 * Applies the styles on [block] to all appearances of the value of the [resId] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.allAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) = allAppearanceOf(context.getString(resId), ignoreCase, spanFlag, block)

/**
 * Applies the styles on [block] to the [indexes] appearances of the [excerpt] in the content,
 * if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.nThAppearanceOf(
    excerpt: String,
    indexes: List<Int>,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    var occurrenceNumber = 0
    do {
        val start = content.indexOf(excerpt, ignoreCase = ignoreCase)
        if (start != NOT_FOUND) {
            occurrenceNumber++
            if (indexes.contains(occurrenceNumber)) {
                val end = start + excerpt.length - 1
                SpankSection(content, start, end, spanFlag).block()
            }
        }
    } while (start != NOT_FOUND)
}

/**
 * Applies the styles on [block] to the [indexes] appearances of the value of the [resId] in the
 * content, if it should match even if casing is different, then use [ignoreCase] = true
 */
inline fun SpankBuilderBase.nThAppearanceOf(
    @StringRes resId: Int,
    indexes: List<Int>,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) = nThAppearanceOf(context.getString(resId), indexes, ignoreCase, spanFlag, block)

/**
 * Applies the styles on [block] to the first paragraph of the content
 */
inline fun SpankBuilderBase.firstParagraph(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = paragraphs.firstOrNull()
        ?: throw IllegalStateException("Couldn't find any paragraph on '$content'")
    SpankParagraph(content, paragraph, spanFlag).block()
}

/**
 * Applies the styles on [block] to the last paragraph of the content
 */
inline fun SpankBuilderBase.lastParagraph(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = paragraphs.lastOrNull()
        ?: throw IllegalStateException("Couldn't find any paragraph on '$content'")
    SpankParagraph(content, paragraph, spanFlag).block()
}

/**
 * Applies the styles on [block] to the [indexes] paragraphs of the content
 */
inline fun SpankBuilderBase.nThParagraph(
    indexes: List<Int>,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) = indexes.forEach {
    if ((it < 0) or (it >= paragraphs.size)) {
        throw IllegalStateException("One or more indexes passed are invalid $indexes, indexes are zero based (0-${paragraphs.size - 1})")
    }
    SpankParagraph(content, paragraphs[it], spanFlag).block()
}

/**
 * Applies the styles on [block] to the [index] paragraph of the content
 */
inline fun SpankBuilderBase.nThParagraph(
    index: Int,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) = nThParagraph(listOf(index), spanFlag, block)

/**
 * Applies the styles on [block] to the paragraphs [start] to [end] of the content both inclusive
 */
inline fun SpankBuilderBase.paragraphRange(
    @IntRange(from = 0) start: Int,
    @IntRange(from = 0) end: Int,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    if ((start >= paragraphs.size) or (end < start) or (end >= paragraphs.size)) {
        throw IllegalStateException("range ($start, $end) is invalid indexes are zero based (0-${paragraphs.size - 1})")
    }

    val startIndex = paragraphs[start].startIndex
    val endIndex = paragraphs[end].endIndex
    SpankParagraph(
        content,
        Paragraph(startIndex, endIndex),
        spanFlag
    ).block()
}

inline fun SpankBuilderBase.paragraphRange(
    range: Pair<Int, Int>,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) = paragraphRange(range.first, range.second, spanFlag, block)