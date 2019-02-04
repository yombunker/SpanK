@file:JvmName("SpanK")

package com.bunk3r.spank

import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.annotation.IntRange
import androidx.annotation.StringRes

class SpankBuilder(val context: Context, val content: SpannableStringBuilder) {
    val paragraphs: List<Paragraph> by lazy {
        content.toParagraphs()
    }

    fun build(): Spanned {
        return content
    }
}

inline fun Context.styleString(content: String, block: SpankBuilder.() -> Unit): Spanned {
    val spannableBuilder = SpannableStringBuilder(content)
    val builder = SpankBuilder(this, spannableBuilder)
    builder.block()
    return builder.build()
}

inline fun Context.styleString(@StringRes resId: Int, block: SpankBuilder.() -> Unit) =
    styleString(getString(resId), block)

inline fun SpankBuilder.range(
    @IntRange(from = 0) start: Int,
    @IntRange(from = 1) end: Int,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    if ((start >= content.length) or (end < start) or (end >= content.length)) {
        throw IllegalStateException("the range($start, $end) is outside the boundaries of the content (0, ${content.length})")
    }

    SpankSection(content, start, end, spanFlag).block()
}

inline fun SpankBuilder.all(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = Paragraph(0, content.length - 1)
    SpankParagraph(content, paragraph, spanFlag).block()
}

inline fun SpankBuilder.firstAppearanceOf(
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

inline fun SpankBuilder.firstAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val excerpt = context.getString(resId)
    firstAppearanceOf(excerpt, ignoreCase, spanFlag, block)
}

inline fun SpankBuilder.lastAppearanceOf(
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

inline fun SpankBuilder.lastAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val excerpt = context.getString(resId)
    lastAppearanceOf(excerpt, ignoreCase, spanFlag, block)
}

inline fun SpankBuilder.allAppearanceOf(
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

inline fun SpankBuilder.allAppearanceOf(
    @StringRes resId: Int,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val excerpt = context.getString(resId)
    allAppearanceOf(excerpt, ignoreCase, spanFlag, block)
}

inline fun SpankBuilder.nThAppearanceOf(
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

inline fun SpankBuilder.nThAppearanceOf(
    @StringRes resId: Int,
    indexes: List<Int>,
    ignoreCase: Boolean = false,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankSection.() -> Unit
) {
    val excerpt = context.getString(resId)
    nThAppearanceOf(excerpt, indexes, ignoreCase, spanFlag, block)
}

inline fun SpankBuilder.firstParagraph(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = paragraphs.firstOrNull()
        ?: throw IllegalStateException("Couldn't find any paragraph on '$content'")
    SpankParagraph(content, paragraph, spanFlag).block()
}

inline fun SpankBuilder.lastParagraph(
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    val paragraph = paragraphs.lastOrNull()
        ?: throw IllegalStateException("Couldn't find any paragraph on '$content'")
    SpankParagraph(content, paragraph, spanFlag).block()
}

inline fun SpankBuilder.nThParagraph(
    index: Int,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    nThParagraph(listOf(index), spanFlag, block)
}

inline fun SpankBuilder.nThParagraph(
    indexes: List<Int>,
    @EzFlags spanFlag: Int = EzFlags.EXCLUSIVE_EXCLUSIVE,
    block: SpankParagraph.() -> Unit
) {
    indexes.forEach {
        if ((it < 0) or (it >= paragraphs.size)) {
            throw IllegalStateException("One or more indexes passed are invalid $indexes, indexes are zero based (0-${paragraphs.size - 1})")
        }
        SpankParagraph(content, paragraphs[it], spanFlag).block()
    }
}

inline fun SpankBuilder.paragraphRange(
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
