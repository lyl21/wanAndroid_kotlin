package github.lyl21.wanandroid

import android.app.Application
import com.tencent.mmkv.MMKV
import android.content.Context
import android.content.SharedPreferences
import github.lyl21.wanandroid.common.ConstantParam
import github.lyl21.wanandroid.moudle.live.Constants
import github.lyl21.wanandroid.moudle.live.rtc.AgoraEventHandler
import github.lyl21.wanandroid.moudle.live.rtc.EngineConfig
import github.lyl21.wanandroid.moudle.live.rtc.EventHandler
import github.lyl21.wanandroid.moudle.live.stats.StatsManager
import github.lyl21.wanandroid.moudle.live.utils.FileUtil
import github.lyl21.wanandroid.moudle.live.utils.PrefManager
import github.lyl21.wanandroid.util.DarkModeUtil
import io.agora.rtc.RtcEngine
import java.lang.Exception



/**
 *
 *
 * @author    popcomimico
 * @date    2021/9/28 21:56
 */
class App : Application() {
    private lateinit var application: App
    private lateinit var mRtcEngine: RtcEngine
    private val mGlobalConfig = EngineConfig()
    private val mHandler = AgoraEventHandler()
    private val mStatsManager = StatsManager()



    override fun onCreate() {
        super.onCreate()
        application=this
        //mmkv
        MMKV.initialize(this)
        //暗黑模式
        DarkModeUtil.init(this)
        //声网
        try {
            mRtcEngine = RtcEngine.create(applicationContext, ConstantParam.Agora_APPID, mHandler)
            mRtcEngine.setLogFile(FileUtil.initializeLogFile(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        initLiveConfig()
//        initLoadSir()
    }

    private fun initLiveConfig() {
        val pref: SharedPreferences = PrefManager.getPreferences(applicationContext)
        mGlobalConfig.videoDimenIndex = pref.getInt(
            Constants.PREF_RESOLUTION_IDX, Constants.DEFAULT_PROFILE_IDX
        )
        val showStats = pref.getBoolean(Constants.PREF_ENABLE_STATS, false)
        mGlobalConfig.setIfShowVideoStats(showStats)
        mStatsManager.enableStats(showStats)
        mGlobalConfig.mirrorLocalIndex = pref.getInt(Constants.PREF_MIRROR_LOCAL, 0)
        mGlobalConfig.mirrorRemoteIndex = pref.getInt(Constants.PREF_MIRROR_REMOTE, 0)
        mGlobalConfig.mirrorEncodeIndex = pref.getInt(Constants.PREF_MIRROR_ENCODE, 0)
    }

    fun engineConfig(): EngineConfig {
        return mGlobalConfig
    }
    fun rtcEngine(): RtcEngine {
        return mRtcEngine
    }
    fun statsManager(): StatsManager {
        return mStatsManager
    }
    fun registerEventHandler(handler: EventHandler?) {
        mHandler.addHandler(handler)
    }
    fun removeEventHandler(handler: EventHandler?) {
        mHandler.removeHandler(handler)
    }

    override fun onTerminate() {
        super.onTerminate()
        RtcEngine.destroy()
    }


    fun getMyApplicationContext(): Context? {
        return application.applicationContext
    }


//    fun initLoadSir(){
//        LoadSir.beginBuilder()
//            .addCallback(ErrorCallback()) //添加各种状态页
//            .addCallback(EmptyCallback())
//            .addCallback(LoadingCallback())
////            .addCallback(TimeoutCallback())
////            .addCallback(CustomCallback())
//            .setDefaultCallback(LoadingCallback::class.java) //设置默认状态页
//            .commit()
//    }


}