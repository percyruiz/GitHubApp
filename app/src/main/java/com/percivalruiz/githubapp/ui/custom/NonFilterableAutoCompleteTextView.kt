package com.percivalruiz.githubapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.percivalruiz.githubapp.R

class NonFilterableAutoCompleteTextView @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyleAttr: Int = R.attr.autoCompleteTextViewStyle
) : MaterialAutoCompleteTextView(context, attributeSet, defStyleAttr) {
  private var isCallingSetText = false

  override fun setText(text: CharSequence?, type: BufferType?) {
    if (isCallingSetText || inputType != EditorInfo.TYPE_NULL) {
      super.setText(text, type)
    } else {
      isCallingSetText = true
      setText(text, false)
      isCallingSetText = false
    }
  }
}
