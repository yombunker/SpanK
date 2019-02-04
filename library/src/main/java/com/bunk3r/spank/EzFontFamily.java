package com.bunk3r.spank;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;

import static com.bunk3r.spank.EzFontFamily.MONOSPACE;
import static com.bunk3r.spank.EzFontFamily.SANS_SERIF;
import static com.bunk3r.spank.EzFontFamily.SANS_SERIF_CONDENSED;
import static com.bunk3r.spank.EzFontFamily.SANS_SERIF_LIGHT;
import static com.bunk3r.spank.EzFontFamily.SERIF;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Subset of supported font families
 */
@Retention(SOURCE)
@StringDef(value = {MONOSPACE,
        SERIF,
        SANS_SERIF,
        SANS_SERIF_LIGHT,
        SANS_SERIF_CONDENSED})
public @interface EzFontFamily {
    String MONOSPACE = "monospace";
    String SERIF = "serif";
    String SANS_SERIF = "sans-serif";
    String SANS_SERIF_LIGHT = "sans-serif-light";
    String SANS_SERIF_CONDENSED = "sans-serif-condensed";
}
