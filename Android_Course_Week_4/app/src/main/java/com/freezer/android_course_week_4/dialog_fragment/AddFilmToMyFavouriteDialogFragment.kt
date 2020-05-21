package com.freezer.android_course_week_4.dialog_fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.freezer.android_course_week_4.film.Film
import com.freezer.android_course_week_4.R

class AddFilmToMyFavouriteDialogFragment(
    val dialogTitle: Int,
    val listener: AddFilmToMyFavouriteDialogListener,
    val film: Film
) : DialogFragment() {
    interface AddFilmToMyFavouriteDialogListener{
        fun onPositiveButton(film: Film)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)

            builder.setTitle(dialogTitle)
                .setPositiveButton(
                    R.string.dialog_add_to_favourite_OK,
                    DialogInterface.OnClickListener { dialog, id ->
                        // Send the positive button event back to the host activity
                        listener.onPositiveButton(film)
                    })
                .setNegativeButton(
                    R.string.dialog_add_to_favourite_cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        return@OnClickListener
                    })
            builder.create()
        } ?: throw IllegalStateException()
    }
}