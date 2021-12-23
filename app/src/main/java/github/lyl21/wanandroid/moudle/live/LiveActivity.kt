package github.lyl21.wanandroid.moudle.live

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.moudle.live.stats.LocalStatsData
import github.lyl21.wanandroid.moudle.live.stats.RemoteStatsData
import github.lyl21.wanandroid.moudle.live.ui.VideoGridContainer
import io.agora.rtc.IRtcEngineEventHandler.*
import io.agora.rtc.video.VideoEncoderConfiguration.VideoDimensions
import io.agora.rtc.Constants


class LiveActivity : RtcBaseActivity() {

    private var mVideoGridContainer: VideoGridContainer? = null
    private var mMuteAudioBtn: ImageView? = null
    private var mMuteVideoBtn: ImageView? = null
    private var mVideoDimension: VideoDimensions? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_live)
        config().channelName="test"
        initUI()
        initData()
    }

    private fun initUI() {
        val roomName: TextView = findViewById(R.id.live_room_name)
        roomName.text = config().channelName
        roomName.isSelected = true
        initUserIcon()
        val role = intent.getIntExtra(
            github.lyl21.wanandroid.moudle.live.Constants.KEY_CLIENT_ROLE,
            Constants.CLIENT_ROLE_AUDIENCE
        )
        val isBroadcaster = role == Constants.CLIENT_ROLE_BROADCASTER
        mMuteVideoBtn = findViewById(R.id.live_btn_mute_video)
        mMuteVideoBtn?.isActivated = isBroadcaster
        mMuteAudioBtn = findViewById(R.id.live_btn_mute_audio)
        mMuteAudioBtn?.isActivated = isBroadcaster
        val beautyBtn: ImageView = findViewById(R.id.live_btn_beautification)
        beautyBtn.isActivated = true
        rtcEngine().setBeautyEffectOptions(
            beautyBtn.isActivated,
            github.lyl21.wanandroid.moudle.live.Constants.DEFAULT_BEAUTY_OPTIONS
        )
        mVideoGridContainer = findViewById(R.id.live_video_grid_layout)
        mVideoGridContainer?.setStatsManager(statsManager())
        rtcEngine().setClientRole(role)
        if (isBroadcaster) startBroadcast()
    }

    private fun initUserIcon() {
//        val origin = BitmapFactory.decodeResource(resources, R.drawable.fake_user_icon)
//        val drawable = RoundedBitmapDrawableFactory.create(resources, origin)
//        drawable.isCircular = true
//        val iconView: ImageView = findViewById(R.id.live_name_board_icon)
//        iconView.setImageDrawable(drawable)
    }

    private fun initData() {
        mVideoDimension =
            github.lyl21.wanandroid.moudle.live.Constants.VIDEO_DIMENSIONS[config().videoDimenIndex]
    }

    override fun onGlobalLayoutCompleted() {
        val topLayout: RelativeLayout = findViewById(R.id.live_room_top_layout)
        val params = topLayout.layoutParams as RelativeLayout.LayoutParams
        params.height = mStatusBarHeight + topLayout.measuredHeight
        topLayout.layoutParams = params
        topLayout.setPadding(0, mStatusBarHeight, 0, 0)
    }

    private fun startBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_BROADCASTER)
        val surface = prepareRtcVideo(0, true)
        mVideoGridContainer!!.addUserVideoSurface(0, surface, true)
        mMuteAudioBtn!!.isActivated = true
    }

    private fun stopBroadcast() {
        rtcEngine().setClientRole(Constants.CLIENT_ROLE_AUDIENCE)
        removeRtcVideo(0, true)
        mVideoGridContainer!!.removeUserVideo(0, true)
        mMuteAudioBtn!!.isActivated = false
    }

    override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
        // Do nothing at the moment
    }

    override fun onUserJoined(uid: Int, elapsed: Int) {
        // Do nothing at the moment
    }

    override fun onUserOffline(uid: Int, reason: Int) {
        runOnUiThread { removeRemoteUser(uid) }
    }

    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
        runOnUiThread { renderRemoteUser(uid) }
    }

    private fun renderRemoteUser(uid: Int) {
        val surface = prepareRtcVideo(uid, false)
        mVideoGridContainer!!.addUserVideoSurface(uid, surface, false)
    }

    private fun removeRemoteUser(uid: Int) {
        removeRtcVideo(uid, false)
        mVideoGridContainer!!.removeUserVideo(uid, false)
    }

    override fun onLocalVideoStats(stats: LocalVideoStats) {
        if (!statsManager().isEnabled) return
        val data: LocalStatsData = statsManager().getStatsData(0) as LocalStatsData ?: return
        data.width = mVideoDimension!!.width
        data.height = mVideoDimension!!.height
        data.framerate = stats.sentFrameRate
    }

    override fun onRtcStats(stats: RtcStats) {
        if (!statsManager().isEnabled) return
        val data: LocalStatsData = statsManager().getStatsData(0) as LocalStatsData ?: return
        data.lastMileDelay = stats.lastmileDelay
        data.videoSendBitrate = stats.txVideoKBitRate
        data.videoRecvBitrate = stats.rxVideoKBitRate
        data.audioSendBitrate = stats.txAudioKBitRate
        data.audioRecvBitrate = stats.rxAudioKBitRate
        data.cpuApp = stats.cpuAppUsage
        data.cpuTotal = stats.cpuAppUsage
        data.sendLoss = stats.txPacketLossRate
        data.recvLoss = stats.rxPacketLossRate
    }

    override fun onNetworkQuality(uid: Int, txQuality: Int, rxQuality: Int) {
        if (!statsManager().isEnabled) return
        val data = statsManager().getStatsData(uid) ?: return
        data.sendQuality = statsManager().qualityToString(txQuality)
        data.recvQuality = statsManager().qualityToString(rxQuality)
    }

    override fun onRemoteVideoStats(stats: RemoteVideoStats) {
        if (!statsManager().isEnabled) return
        val data: RemoteStatsData = statsManager().getStatsData(stats.uid) as RemoteStatsData
            ?: return
        data.width = stats.width
        data.height = stats.height
        data.framerate = stats.rendererOutputFrameRate
        data.videoDelay = stats.delay
    }

    override fun onRemoteAudioStats(stats: RemoteAudioStats) {
        if (!statsManager().isEnabled) return
        val data: RemoteStatsData = statsManager().getStatsData(stats.uid) as RemoteStatsData
            ?: return
        data.audioNetDelay = stats.networkTransportDelay
        data.audioNetJitter = stats.jitterBufferDelay
        data.audioLoss = stats.audioLossRate
        data.audioQuality = statsManager().qualityToString(stats.quality)
    }

    override fun finish() {
        super.finish()
        statsManager().clearAllData()
    }

    fun onLeaveClicked(view: View?) {
        finish()
    }

    fun onSwitchCameraClicked(view: View?) {
        rtcEngine().switchCamera()
    }

    fun onBeautyClicked(view: View) {
        view.isActivated = !view.isActivated
        rtcEngine().setBeautyEffectOptions(
            view.isActivated,
            github.lyl21.wanandroid.moudle.live.Constants.DEFAULT_BEAUTY_OPTIONS
        )
    }

    fun onMoreClicked(view: View?) {
        // Do nothing at the moment
    }

    fun onPushStreamClicked(view: View?) {
        // Do nothing at the moment
    }

    fun onMuteAudioClicked(view: View) {
        if (!mMuteVideoBtn!!.isActivated) return
        rtcEngine().muteLocalAudioStream(view.isActivated)
        view.isActivated = !view.isActivated
    }

    fun onMuteVideoClicked(view: View) {
        if (view.isActivated) {
            stopBroadcast()
        } else {
            startBroadcast()
        }
        view.isActivated = !view.isActivated
    }



}