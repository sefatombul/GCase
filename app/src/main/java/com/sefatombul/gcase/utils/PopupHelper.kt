package com.sefatombul.gcase.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


object PopupHelper {

    fun Context.showAlertDialog(
        title: String,
        message: String,
        positiveButtonString: String? = null,
        positiveButtonClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        negativeButtonString: String? = null,
        negativeButtonClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
        neutralButtonString: String? = null,
        neutralButtonClickListener: ((dialog: DialogInterface, which: Int) -> Unit)? = null,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        positiveButtonClickListener?.let { listener ->
            builder.setPositiveButton(positiveButtonString) { dialog, which ->
                listener(dialog, which)
            }
        }
        negativeButtonClickListener?.let { listener ->
            builder.setNegativeButton(negativeButtonString) { dialog, which ->
                listener(dialog, which)
            }
        }
        neutralButtonClickListener?.let { listener ->
            builder.setNeutralButton(neutralButtonString) { dialog, which ->
                listener(dialog, which)
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}