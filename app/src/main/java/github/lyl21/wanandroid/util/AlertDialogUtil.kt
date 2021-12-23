package github.lyl21.wanandroid.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ActivityUtils.startActivity
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.moudle.login.LoginActivity
import splitties.init.appCtx

object AlertDialogUtil {

    fun show(context: Context){
        AlertDialog.Builder(context).apply {
            setTitle(R.string.tip)
            setMessage(R.string.toLogin)
            setPositiveButton(R.string.sure) { _, _ ->
                startActivity(Intent(context, LoginActivity::class.java))
            }
            setNegativeButton(R.string.cancel, null)
        }.create().show()
    }
}