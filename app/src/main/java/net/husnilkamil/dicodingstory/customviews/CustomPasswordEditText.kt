package net.husnilkamil.dicodingstory.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import net.husnilkamil.dicodingstory.R

class CustomPasswordEditText : AppCompatEditText {

    private var errorIcon: Drawable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun showErrorIcon() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, errorIcon, null)
    }

    private fun hideErrorIcon() {
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Password Anda?"
        textAlignment = TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {
        errorIcon = ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!charSequence.toString().isEmpty()) {
                    if (charSequence.toString().length <= 6) {
                        showErrorIcon()
                    } else {
                        hideErrorIcon()
                    }
                } else {
                    hideErrorIcon()
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }
}