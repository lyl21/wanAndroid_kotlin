package github.lyl21.wanandroid.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.ActivityManager.RunningTaskInfo
import android.content.*
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ToastUtils
import github.lyl21.wanandroid.App
import github.lyl21.wanandroid.R
import github.lyl21.wanandroid.common.ConstantParam
import java.io.File

object SystemUtil {
    const val GET_CROP_IMAGE = 1002

    /**
     * 获取资源的ID
     *
     * @param context 上下文对象
     * @param name    资源的名称（图片的话，不用带后缀名）
     * @param type    资源的类型
     * @return 0：表示的是没有找到相应的资源文件
     */
    fun getResourceID(
        context: Context,
        name: String,
        type: String
    ): Int {
        return context.resources.getIdentifier(
            context.packageName + ":" + type + "/" + name,
            null,
            null
        )
    }

    /*是否在后台*/
    fun isInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo: List<*>?
        if (Build.VERSION.SDK_INT > 20) {
            taskInfo = am.runningAppProcesses
            if (taskInfo == null) {
                return false
            }
            val componentInfo = taskInfo.iterator()
            while (true) {
                var processInfo: RunningAppProcessInfo
                do {
                    if (!componentInfo.hasNext()) {
                        return isInBackground
                    }
                    processInfo = componentInfo.next() as RunningAppProcessInfo
                } while (processInfo.importance != 100)
                val var6 = processInfo.pkgList
                val var7 = var6.size
                for (var8 in 0 until var7) {
                    val activeProcess = var6[var8]
                    if (activeProcess == context.packageName) {
                        return false
                    }
                }
            }
        } else {
            taskInfo = am.getRunningTasks(1)
            val var10 = (taskInfo[0] as RunningTaskInfo).topActivity
            if (var10!!.packageName == context.packageName) {
                isInBackground = false
            }
        }
        return isInBackground
    }

    fun fullScreenWithStatusBarColor(
        activity: Activity,
        colorResID: Int,
        isWhite: Boolean
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            val window = activity.window
            window.addFlags(-2147483648)
            window.clearFlags(67108864)
            window.statusBarColor = ContextCompat.getColor(activity, colorResID)
            val decorView = window.decorView
            val option: Short
            if (isWhite) {
                option = 1280
                decorView.systemUiVisibility = option.toInt()
            } else {
                option = 9472
                decorView.systemUiVisibility = option.toInt()
            }
            true
        } else {
            false
        }
    }

    /**
     * 显示软键盘
     *
     * @param context
     * @param v
     */
    fun showSystemKeyBoard(context: Context, v: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文对象
     * @param v       获取焦点的View
     */
    fun hideSystemKeyBoard(context: Context, v: View?) {
        if (v == null) {
            return
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 裁剪图片
     *
     * @param activity  启动裁剪图片的Activity
     * @param imagePath 图片的路径
     * @param savePath  裁剪后图片的保存路径，如果不想保存的话传null或者“”
     * @param aspectX   裁剪时X轴的比例
     * @param aspectY   裁剪时Y轴的比例
     * @param outX      裁剪图片输出的X轴的长度
     * 如果savePath为null或者“”，在onActivityResult中接收系统默认返回的是裁剪的缩略图，使用data.getExtras().get("data");接收。
     * 该方法以startActivityForResult的方式启动系统的裁剪工具，requestCode为GET_CROP_IMAGE，值为1002
     */
    fun cropImage(
        activity: Activity,
        imagePath: String?,
        savePath: String?,
        aspectX: Int,
        aspectY: Int,
        outX: Int,
        GET_CROP_IMAGE: Int
    ) {
        if (!TextUtils.isEmpty(imagePath)) {
            val intent = Intent("com.android.camera.action.CROP")
            intent.setDataAndType(getImageContentUri(activity, imagePath), "image/*")
            //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true")
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", aspectX)
            intent.putExtra("aspectY", aspectY)
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", outX)
            intent.putExtra("outputY", outX * aspectY / aspectX)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra("return-data", false)
            if (!TextUtils.isEmpty(savePath)) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(File(savePath)))
            }
            activity.startActivityForResult(intent, GET_CROP_IMAGE)
        }
    }

    /**
     * 根据输入源文件路径获取uri
     *
     * @param filePath
     * @return
     */
    fun getImageContentUri(filePath: String?): Uri {
        val context: Context = App().applicationContext
        val file = File(filePath)
        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                context,
                context.packageName + ConstantParam.FILE_PROVIDER,
                file
            )
        } else {
            Uri.fromFile(file)
        }
        return uri
    }

    /**
     * 根据输入源文件路径获取uri
     *
     * @param path
     * @return
     */
    @SuppressLint("Range")
    private fun getImageContentUri(context: Context, path: String?): Uri? {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Images.Media._ID),
            MediaStore.Images.Media.DATA + "=? ", arrayOf(path), null
        )
        return if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            val baseUri = Uri.parse("content://media/external/images/media")
            Uri.withAppendedPath(baseUri, "" + id)
        } else {
            val contentValues = ContentValues(1)
            contentValues.put(MediaStore.Images.Media.DATA, path)
            context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
        }
    }

    /**
     * 复制内容到剪切板
     *
     * @param context
     * @param copyString
     */
    fun copyToClipboard(context: Context, copyString: String?) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        val clipData = ClipData.newPlainText(null, copyString)
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData)
       ToastUtils.showLong( R.string.copy_yes)
    }

    /**
     * 通知相册更新
     *
     * @param context
     * @param imagePath
     */
    fun updateImageForAlbum(context: Context?, imagePath: String?) {
        if (!TextUtils.isEmpty(imagePath)) {
            /*Uri imageUri;
            File file = new File(imagePath);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ConstantParam.FILE_PROVIDER, file);
            } else {
                imageUri = Uri.fromFile(file);
            }
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri);
            context.sendBroadcast(intent);*/
            val file = File(imagePath)
            MediaScannerConnection.scanFile(context, arrayOf(file.toString()),
                arrayOf(file.name), null)
//            val imageUri = Uri.fromFile(file)
//            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri)
//            context?.sendBroadcast(intent)
        }
    }

//    fun advertJump(context: Context, advertInfo: AdvertInfo?) {
//        //广告类型（ 0：无动作，1：图文广告，2：外部链接 ）
//        if (advertInfo == null) {
//            return
//        }
//        val intent: Intent
//        when (advertInfo.getAdvertType()) {
//            "1", "2" -> {
//                intent = Intent(context, WebViewHelperActivity::class.java)
//                intent.putExtra("title", advertInfo.getAdvertTitle())
//                intent.putExtra("url", advertInfo.getLinkUrl())
//                context.startActivity(intent)
//            }
//            else -> {}
//        }
//    }
}