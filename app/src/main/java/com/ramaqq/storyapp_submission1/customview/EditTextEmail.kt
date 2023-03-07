package com.ramaqq.storyapp_submission1.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.ramaqq.storyapp_submission1.R

class EditTextEmail: AppCompatEditText{

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_email)
    }

/*    override fun onFocusChange(p0: View?, p1: Boolean) {
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")

        if (!p1 && !text.isNullOrEmpty() && !emailRegex.matches(text.toString()))
            error = "masukkan format email yang valid"
    }*/

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (text.isNullOrEmpty())
                    error = context.getString(R.string.hint_email)
                if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches() && !text.isNullOrEmpty())
                    error = context.getString(R.string.error_email)
            }

            override fun afterTextChanged(p0: Editable) {
                // do nothing
            }
        })
    }


}