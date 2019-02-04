package com.bunk3r.spanez.sample

import android.os.Bundle
import android.text.Spanned
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bunk3r.spank.*
import kotlinx.android.synthetic.main.activity_main.*

class KotlinMainActivity : AppCompatActivity() {

    var spanIsSet = false
    private lateinit var spannedContent: Spanned

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sample_text.movementMethod = LinkMovementMethod()

        toggle_button.setOnClickListener {
            toggleSpans()
        }

        spannedContent = styleString(R.string.medium_post) {
            all {
                foregroundColor(color(R.color.colorPostContent))
            }

            firstParagraph {
                absoluteSizeDP(32)
                bold()
                foregroundColor(color(R.color.colorPostTitle))
                link("https://medium.com/@jorgecool/spannables-made-easy-e7ce634723c5")
            }

            paragraphRange(1, 2) {
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

            firstAppearanceOf(R.string.yes) {
                foregroundColor(color(R.color.colorAccent))
                italic()
            }

            firstAppearanceOf(R.string.then_you_know) {
                underline()
                clickable {
                    toast("you clicked: $it")
                }
            }

            firstAppearanceOf(R.string.working_on_library) {
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
