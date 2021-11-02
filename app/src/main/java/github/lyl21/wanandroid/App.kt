package github.lyl21.wanandroid

import android.app.Application
import com.tencent.mmkv.MMKV


/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 21:56
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //mmkv
        MMKV.initialize(this)
    }


}