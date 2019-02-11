package com.bunk3r.spanez.sample

import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import com.bunk3r.spank.*
import kotlinx.android.synthetic.main.activity_main.*

class SpankTosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_service)

        sample_text.movementMethod = LinkMovementMethod()
        sample_text.text =
            styleStringWithTokens(R.string.google_music_terms_of_service, "{\\%s}", "{%s/}") {
                all {
                    foregroundColor(color(R.color.colorTosForeground))
                    alignTo(EzAlign.CENTER)
                }

                firstParagraph {
                    absoluteSizeDP(32)
                }

                nThParagraph(1) {
                    absoluteSizeDP(18)
                }

                nThParagraph(2) {
                    absoluteSizeDP(24)
                    foregroundColor(Color.BLACK)
                    backgroundColor(color(R.color.colorTosForeground))
                    clickable(showUnderline = false) {
                        toast("Imagine a purchase popup appearing")
                    }
                }

                nThParagraph(3) {
                    absoluteSizeDP(24)
                }

                lastParagraph {
                    absoluteSizeDP(12)
                }

                lastAppearanceOf(R.string.tos_link) {
                    clickable {
                        toast("Imagine this is an in-app popup")
                    }
                }

                token("copy") {
                    superscript()
                }
            }
    }

}
