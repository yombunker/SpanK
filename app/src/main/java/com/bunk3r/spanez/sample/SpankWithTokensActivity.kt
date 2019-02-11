package com.bunk3r.spanez.sample

import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bunk3r.spank.*
import kotlinx.android.synthetic.main.activity_main.*

class SpankWithTokensActivity : AppCompatActivity() {

    var spanIsSet = false
    private lateinit var spannedContent: Spanned

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sample_text.movementMethod = LinkMovementMethod()

        toggle_button.setOnClickListener {
            toggleSpans()
        }

        spannedContent =
            styleStringWithTokens(R.string.medium_post_with_tokens, "{\\%s}", "{%s/}") {
                all {
                    foregroundColor(color(R.color.colorPostContent))
                }

                firstParagraph {
                    absoluteSizeDP(32)
                    bold()
                    foregroundColor(color(R.color.colorPostTitle))
                    link("https://medium.com/@jorgecool/spannables-made-easy-e7ce634723c5")
                }

                paragraphRange(1 to 2) {
                    absoluteSizeDP(10)
                    drawableWithMargin(
                        ContextCompat.getDrawable(
                            context, R.drawable.ic_android_green_24dp
                        )
                    )
                }

                nThParagraph(2) {
                    foregroundColor(color(R.color.colorPostContentWithAlpha))
                }

                token("yes") {
                    foregroundColor(color(R.color.colorAccent))
                    italic()
                }

                token("problem") {
                    underline()
                    clickable {
                        toast("you clicked: $it")
                    }
                }

                token("reveal") {
                    backgroundColor(color(R.color.colorPrimaryDark))
                    foregroundColor(color(R.color.colorAccent))
                }
            }
    }

    private fun toggleSpans() {
        if (spanIsSet) {
            sample_text.setText(R.string.medium_post)
        } else {
            sample_text.text = spannedContent
        }

        spanIsSet = !spanIsSet
    }

}
