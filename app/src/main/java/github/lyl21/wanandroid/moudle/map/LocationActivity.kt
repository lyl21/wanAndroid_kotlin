package github.lyl21.wanandroid.moudle.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode
import com.amap.api.location.AMapLocationClientOption.AMapLocationProtocol
import com.amap.api.location.AMapLocationListener
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.R

class LocationActivity : CheckPermissionsActivity() ,View.OnClickListener {

    //声明AMapLocationClient类对象
    private lateinit var mLocationClient: AMapLocationClient
    //声明AMapLocationClientOption对象
    private lateinit var mLocationOption: AMapLocationClientOption;

    /**
     * 定位监听
     */
    private var locationListener =
        AMapLocationListener { location ->
            if (location != null) {
                if (location.errorCode == 0) {
//                可在其中解析amapLocation获取相应内容。
                    location.city;//城市信息
                    location.district;//城区信息
                    location.street;//街道信息
                    location.streetNum;//街道门牌号信息
                    Log.e("lll", "位置：" + location.city + location.district + location.street + location.streetNum)
                    ToastUtils.showLong("位置：" + location.city + location.district + location.street + location.streetNum)

                    locationInfoTextView.text=location.city+location.district+location.street+location.streetNum

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    locationInfoTextView.text= ""+location.errorCode + "--"+location.errorInfo
                    Log.e(
                        "lll", "location Error, ErrCode:"
                                + location.errorCode + ", errInfo:"
                                + location.errorInfo
                    );
                }
            }

        }

    private lateinit var locationInfoTextView: TextView
    private lateinit var button: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        locationInfoTextView=findViewById(R.id.tv_location_info)
        button=findViewById(R.id.tv_location_info_get)

        locationInfoTextView.text = "初始化"

        initLocation()
        initListener();

    }

    private fun initListener(){
        button.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        if (v?.id == R.id.tv_location_info_get) {
                startLocation()
        }
    }


    private fun initLocation() {
        //初始化定位
        mLocationClient = AMapLocationClient(baseContext)
        mLocationOption = getDefaultOption()
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        //设置定位回调监听
        mLocationClient.setLocationListener(locationListener);
//        mLocationOption.locationPurpose = AMapLocationClientOption.AMapLocationPurpose.Transport
//        if (null != mLocationClient) {
//            mLocationClient.setLocationOption(mLocationOption);
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            mLocationClient.stopLocation();
//            mLocationClient.startLocation();
//        }
    }
    private fun startLocation(){
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy;
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    override fun onStop() {
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        super.onStop()
    }

    override fun onDestroy() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy()
        }
        super.onDestroy()
    }


    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     */
    private fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode =
            AMapLocationMode.Hight_Accuracy //可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false //可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000 //可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000 //可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true //可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = false //可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest =
            false //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationProtocol.HTTP) //可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false //可选，设置是否使用传感器。默认是false
        mOption.isWifiScan =
            true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        mOption.geoLanguage =
            AMapLocationClientOption.GeoLanguage.DEFAULT //可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption
    }

}