package com.bunk3r.spank;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.bunk3r.spank.EzAlign.CENTER;
import static com.bunk3r.spank.EzAlign.END;
import static com.bunk3r.spank.EzAlign.START;

@Retention(RetentionPolicy.SOURCE)
@IntDef(value = {START, CENTER, END})
public @interface EzAlign {
    int START = 0;
    int CENTER = 1;
    int END = 2;
}
