package github.lyl21.wanandroid.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import github.lyl21.wanandroid.R
import splitties.init.appCtx

/**
 * @param:     dialog.show();   dialog.dismiss();
 * @className: LoadingUtil
 * @description:  加载dialog
 * @author: lyl
 * @date:  2021/11/1
 **/
class LoadingDialogUtil(context: Context): Dialog(context, R.style.SimpleDialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        setCancelable(true)
    }
}