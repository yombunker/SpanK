package com.bunk3r.spank

import android.text.Spannable
import androidx.annotation.IntRange

const val NOT_FOUND = -1

/**
 * intermediate construct to represent the range of a paragraph, this is used by [styleString]
 */
data class Paragraph(
    @IntRange(from = 0) val startIndex: Int,
    @IntRange(from = 0) val endIndex: Int
)

/**
 * splits the [Spannable] into paragraphs and returns a [List] containing them
 */
fun Spannable.toParagraphs(): List<Paragraph> {
    val paragraphs: MutableList<Paragraph> = mutableListOf()
    var startPosition = 0
    var newLinePosition: Int

    do {
        newLinePosition = indexOf('\n', startPosition)
        val paragraphEnd = if (newLinePosition != NOT_FOUND) newLinePosition else length
        val endPosition = paragraphEnd - 1
        if (endPosition - startPosition > 0) {
            paragraphs.add(
                Paragraph(
                    startPosition,
                    endPosition
                )
            )
        }
        startPosition = newLinePosition + 1
    } while (newLinePosition != NOT_FOUND)

    return paragraphs
}
