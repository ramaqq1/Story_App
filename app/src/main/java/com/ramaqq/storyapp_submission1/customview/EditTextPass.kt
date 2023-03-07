package com.ramaqq.storyapp_submission1.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.ramaqq.storyapp_submission1.R

class EditTextPass: AppCompatEditText{
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
        hint = context.getString(R.string.hint_password)
    }

/*    override fun onFocusChange(p0: View?, p1: Boolean) {
        val length = 6
        if (!p1 && !text.isNullOrEmpty() && text?.length!! < length)
            error = "Password minimal terdiri dari 6 karakter"
    }*/

    private fun init(){
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                // do nothing
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (text.isNullOrEmpty())
                    error = context.getString(R.string.hint_password)
                if (p0.count() in 1..7 && !text.isNullOrEmpty())
                    error = context.getString(R.string.min_pass)
            }

            override fun afterTextChanged(p0: Editable) {
                // do nothing
            }
        })
    }


}