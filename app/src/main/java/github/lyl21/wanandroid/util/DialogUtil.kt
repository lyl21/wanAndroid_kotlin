package github.lyl21.wanandroid.util

import android.content.Context
import android.content.DialogInterface
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.view.CommonDialog
import splitties.alertdialog.appcompat.positiveButton

object DialogUtil {
    fun show(
        context: Context,
        tip: String,
        msg: String,
        negative: String,
        positive: String,
        listener: DialogInterface.OnClickListener
    ) {
        CommonDialog(context)
            .setTitle(tip)
            .setMessage(msg)
            .setNegative(negative)
            .setPositive(positive)
            .setNegativeButton(listener)
            .setPositiveButton(listener)
            .show()
    }


    fun showTip(
        context: Context,
        msg: String,
        listener: DialogInterface.OnClickListener
    ) {
        CommonDialog(context)
            .setTitle(context.getString(R.string.tip))
            .setMessage(msg)
            .setNegative(context.getString(R.string.cancel))
            .setPositive(context.getString(R.string.sure))
            .setPositiveButton(listener)
            .show()
    }


}