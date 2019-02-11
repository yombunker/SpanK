Change Log
==========

Version 1.1.0 *(2019-02-11)*
-----------------------------
 * Added support for Tokenized strings
   - use "styleStringWithTokens" and pass tbe opening/closing tokens
   - closing token is optional if same as opening token
   - token(s) format MUST include %s somewhere in the format, for example:
     - {\\%s} and {%s/} -> {\\title}tokens can be different{title/}
     - {\[%s\]} -> {\[same\]}or they can be the same{\[same\]}
     - @@%s and %s@@ -> @@TALL_TOKEN even uppercase TALL_TOKEN@@
     - abc%s and %scba -> abcbody valid, but, why would you? bodycba
   
 * Added locator:
   - token (only available if you used "WithTokens" method)
     - pass the unique token identifier, style will be applied to everything inside that token

Version 1.0.0 *(2019-02-04)*
-----------------------------

 * Added locators:
    - all
    - range
    - firstAppearanceOf
    - firstAppearanceOf
    - lastAppearanceOf
    - lastAppearanceOf
    - allAppearanceOf
    - allAppearanceOf
    - nThAppearanceOf
    - nThAppearanceOf
    - firstParagraph
    - lastParagraph
    - nThParagraph
    - nThParagraph
    - paragraphRange
 * Added character styles:
    - bold
    - italic
    - underline
    - strikethrough
    - subscript
    - superscript
    - foregroundColor
    - backgroundColor
    - link
    - font
    - appearance
    - locale
    - scaleX
    - relativeSize
    - absoluteSize
    - absoluteSizeDP
    - clickable
    - customAnnotation
    - image
    - imageResource
 * Added paragraph styles:
    - customQuote
    - quote
    - alignTo
    - customAlignTo
    - bullet
    - drawableWithMargin
    - bitmapWithMargin
    - leadingMarginModifier
    - tabStop