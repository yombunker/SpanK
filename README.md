[ ![Download](https://api.bintray.com/packages/yombunker/maven/SpanK/images/download.svg) ](https://bintray.com/yombunker/maven/SpanK/_latestVersion)

# SpanK
Spans for Kotlin made easier, plain and simple.

# How to use
There are 3 steps to start using this library:
1. you need to call the styleString method
   * which will return the styled string that you can set on your TextView.
2. decide which locator(s) fits your needs
   * (optional) you can pass down a flag to override the default spanFlag for all styles on this locator
3. pass down styles to each locator
   * If using link or clickable you need to set movement method on your view `your_text_view.movementMethod = LinkMovementMethod()`


# Step 1 - Get the ball rolling
```kotlin
spannedContent = styleString(R.string.your_string_resource) {}
```

or, if you don't have a String resource just pass the string

```kotlin
spannedContent = styleString("Your string") {}
```

# Step 2 - Locate pieces you want to style
```kotlin
spannedContent = styleString(R.string.your_string_resource) {
    all {
        // Anything inside here applies to the whole content
    }
    
    firstAppearanceOf(R.string.what_ever_text_you_want_to_style) {
        // Anything inside here applies to the first time the string appears inside the content
    }
}
```

# Step 3 - Style styling now
```kotlin
spannedContent = styleString(R.string.your_string_resource) {
    all {
        bold()
        italic()
    }
    
    firstAppearanceOf(R.string.what_ever_text_you_want_to_style) {
        absoluteSizeDP(24)
        clickable {
            // Do something when this is clicked
        }
    }
}
```

# EzFlags (ONLY relevant if using with EditTextView)
* EXCLUSIVE_EXCLUSIVE - (Default) anything added at the beginning or end of the section will NOT be modified by the styles on that section
* INCLUSIVE_INCLUSIVE - anything added at the beginning or end of the section will be modified by the styles on that section
* INCLUSIVE_EXCLUSIVE - anything added at the beginning will be modified by the styles on that section
* EXCLUSIVE_INCLUSIVE- anything added at the end of the section will be modified by the styles on that section
```kotlin
spannedContent = styleString(R.string.your_string_resource) {
    all {
        bold()
        italic()
    }
    
    firstAppearanceOf(R.string.what_ever_text_you_want_to_style, spanFlag = EzFlags.INCLUSIVE_EXCLUSIVE) {
        absoluteSizeDP(24)
        clickable {
            // Do something when this is clicked
        }
    }
}
```