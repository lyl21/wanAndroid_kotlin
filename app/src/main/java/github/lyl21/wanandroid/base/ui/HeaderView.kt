package github.lyl21.wanandroid.base.ui

import android.content.Context
import android.widget.LinearLayout
import android.util.AttributeSet

import android.view.LayoutInflater
import github.lyl21.wanandroid.R
import android.app.Activity
import android.widget.TextView
import org.w3c.dom.Text


class HeaderView(context: Context?, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {
    private val back: LinearLayout
    private val titleTextView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.activity_title, this)
        titleTextView = findViewById(R.id.tv_title_title)
        back = findViewById(R.id.ll_title_back)
        back.setOnClickListener { (getContext() as Activity).finish() }
    }

    fun setTitle(title: String) =
        titleTextView.setText(title)


    fun setTitleColor(color: Int) =
        titleTextView.setTextColor(color)



}
