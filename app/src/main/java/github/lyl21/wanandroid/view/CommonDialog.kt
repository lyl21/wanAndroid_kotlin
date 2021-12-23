package github.lyl21.wanandroid.view

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import github.lyl21.wanandroid.R

class CommonDialog(context: Context) : Dialog(context, R.style.SimpleDialog) {
    private lateinit var tvTip: TextView
    private lateinit var tvMsg: TextView
    private lateinit var tvNegative: TextView
    private lateinit var tvPositive: TextView
    var title: String? = null
        private set
    var message: String? = null
        private set
    var negative: String? = null
        private set
    var positive: String? = null
        private set
    private lateinit var positiveButtonListener: DialogInterface.OnClickListener
    private lateinit var negativeButtonListener: DialogInterface.OnClickListener


    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(context, R.layout.dialog_tip, null)
        setContentView(
            view, ViewGroup.LayoutParams(
                ScreenUtils.getScreenWidth() - SizeUtils.dp2px(90f),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        setCanceledOnTouchOutside(false)
        initView()
        refreshView()
    }

    override fun show() {
        super.show()
        refreshView()
    }

    /**
     * 初始化界面控件的显示数据
     */
    private fun refreshView() {
        //如果用户自定了title和message
        if (!TextUtils.isEmpty(title)) {
            tvTip.setText(R.string.tip)
            //            tvTip.setVisibility(View.VISIBLE);
        } /* else {
            tvTip.setVisibility(View.GONE);
        }*/
        if (!TextUtils.isEmpty(message)) {
            tvMsg.text = message
        }
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(positive)) {
            tvPositive.text = positive
        } else {
            tvPositive.setText(R.string.sure)
        }
        if (!TextUtils.isEmpty(negative)) {
            tvNegative.text = negative
        } else {
            tvNegative.setText(R.string.cancel)
        }
    }

    private fun initView() {
        tvTip = findViewById(R.id.tv_tip_title)
        tvMsg = findViewById(R.id.tv_tip_content)
        tvNegative = findViewById(R.id.tv_tip_cancel)
        tvPositive = findViewById(R.id.tv_tip_sure)
        tvNegative.setOnClickListener {
            setNegativeButton(negativeButtonListener)
        }
        tvPositive.setOnClickListener{
            setPositiveButton(positiveButtonListener)
        }
    }

    fun setMessage(message: String?): CommonDialog {
        this.message = message
        return this
    }

    fun setTitle(title: String?): CommonDialog {
        this.title = title
        return this
    }

    fun setPositive(positive: String?): CommonDialog {
        this.positive = positive
        return this
    }

    fun setPositiveButton(listener: DialogInterface.OnClickListener): CommonDialog {
        this.positiveButtonListener = listener
        return this
    }

    fun setNegative(negative: String?): CommonDialog {
        this.negative = negative
        return this
    }

    fun setNegativeButton(listener: DialogInterface.OnClickListener): CommonDialog {
        this.negativeButtonListener = listener
        return this
    }

}
