package com.bunk3r.spank;

import android.text.Spanned;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bunk3r.spank.EzFlags.EXCLUSIVE_EXCLUSIVE;
import static com.bunk3r.spank.EzFlags.EXCLUSIVE_INCLUSIVE;
import static com.bunk3r.spank.EzFlags.INCLUSIVE_EXCLUSIVE;
import static com.bunk3r.spank.EzFlags.INCLUSIVE_INCLUSIVE;


/**
 * Supported span flags to be used with when adding styles with {@link SpanK}
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef(
        value = {INCLUSIVE_INCLUSIVE,
                INCLUSIVE_EXCLUSIVE,
                EXCLUSIVE_INCLUSIVE,
                EXCLUSIVE_EXCLUSIVE}
)
public @interface EzFlags {
    int INCLUSIVE_INCLUSIVE = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
    int INCLUSIVE_EXCLUSIVE = Spanned.SPAN_INCLUSIVE_EXCLUSIVE;
    int EXCLUSIVE_INCLUSIVE = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
    int EXCLUSIVE_EXCLUSIVE = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
}
