package com.example.islam_shanasi

import android.widget.EditText

class Common {

    companion object {

        fun fieldsRequired(vararg fields: EditText): Boolean {
            var fieldsRequired = false
            fields.forEach {
                if (it.text.isEmpty()) {
                    it.error = "Field Required"
                    it.requestFocus()
                    fieldsRequired = true
                }
            }
            return fieldsRequired;
        }
    }

}