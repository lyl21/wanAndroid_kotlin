package github.lyl21.wanandroid.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import github.lyl21.wanandroid.http.datamanager.DownloadListener
import okhttp3.internal.and
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder

object StreamUtil {
    private const val TAG = "StreamUtil"

    /**
     * 把流对象转换成字符串
     *
     * @param stream 流对象
     * @return 字符串
     */
    fun convertStreamToString(stream: InputStream?): String {
        if (stream != null) {
            try {
                val bos = ByteArrayOutputStream()
                var len = 0
                val buf = ByteArray(1024)
                while (stream.read(buf).also { len = it } != -1) {
                    bos.write(buf, 0, len)
                }
                bos.flush()
                stream.close()
                val stringInfo = bos.toByteArray()
                return String(stringInfo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return ""
    }

    /**
     * Bitmap转换成字节
     *
     * @param bitmap      bitmap对象
     * @param needRecycle bitmap 是否需要回收
     * @return
     */
    fun convertBitmapToByteArray(bitmap: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bitmap.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 获取Uri对应的InputStream
     *
     * @param context 上下文对象
     * @param uri     uri路径
     * @return 获取输入流失败返回null
     */
    fun inputStreamFromUri(context: Context, uri: Uri?): InputStream? {
        val resolver = context.contentResolver
        try {
            return resolver.openInputStream(uri!!)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 从Assets文件夹中获取一个文件的输入流
     *
     * @param context  上下文对象
     * @param fileName 文件的名称
     * @return 如果文件不存在或者其他原因获取失败返回null
     */
    fun inputStreamFromAssetsFile(context: Context, fileName: String?): InputStream? {
        val manager = context.assets
        var inputStream: InputStream? = null
        try {
            inputStream = manager.open(fileName!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return inputStream
    }

    fun toByteArray(hexString: String): ByteArray? {
        var hexString = hexString
        require(!TextUtils.isEmpty(hexString)) { "this hexString must not be empty" }
        hexString = hexString.lowercase()
        val byteArray = ByteArray(hexString.length / 2)
        var k = 0
        for (i in byteArray.indices) { //因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            val high = Character.digit(hexString[k], 16) and 0xff
            val low = Character.digit(hexString[k + 1], 16) and 0xff
            byteArray[i] = (high shl 4 or low).toByte()
            k += 2
        }
        return byteArray
    }

    /**
     * 字节数组转成16进制表示格式的字符串
     *
     * @param byteArray 需要转换的字节数组
     * @return 16进制表示格式的字符串
     */
    fun toHexString(byteArray: ByteArray?): String {
        require(!(byteArray == null || byteArray.isEmpty())) { "this byteArray must not be null or empty" }
        val hexString = StringBuilder()
        for (i in byteArray.indices) {
            if (byteArray[i] and 0xff < 0x10) //0~F前面不零
                hexString.append("0")
            hexString.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
        }
        return hexString.toString().lowercase()
    }

    fun bitmap2Bytes(bitmap: Bitmap, maxkb: Int): ByteArray {
        val output = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        var options = 100
        while (output.toByteArray().size > maxkb && options != 10) {
            output.reset() //清空output
            bitmap.compress(
                Bitmap.CompressFormat.JPEG,
                options,
                output
            ) //这里压缩options%，把压缩后的数据存放到output中
            options -= 10
        }
        Log.i(TAG, "bitmap2Bytes==" + output.toByteArray())
        return output.toByteArray()
    }

    /**
     * 获取bitmap字节大小
     *
     * @param bitmap
     * @return
     */
    fun getBitmapByteSize(bitmap: Bitmap): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.byteCount.toLong()
        }
        // Pre HC-MR1
        Log.i(TAG, "getBitmapByteSize==" + bitmap.rowBytes)
        return (bitmap.rowBytes * bitmap.height).toLong()
    }

    /**
     * 字节流保存到文件
     *
     * @param stream 输入流
     * @param savePath 保存路径
     * @param totalSize 字节长度
     * @param listener 进度监听
     * @return
     * @throws Exception
     */
    fun writeStreamToFileWithListener(
        stream: InputStream?, savePath: String?,
        totalSize: Long, listener: DownloadListener?
    ): Boolean {
        if (stream == null || TextUtils.isEmpty(savePath)) {
            return false
        }
        val file = File(savePath)
        try {
            val fos = FileOutputStream(file)
            var len = 0
            var downloadSize: Long = 0
            val buf = ByteArray(1024)
            while (stream.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                if (listener != null && totalSize > 0) {
                    downloadSize += len.toLong()
                    listener.onProgress((downloadSize / totalSize * 100).toInt())
                }
            }
            fos.flush()
            fos.close()
            stream.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i(TAG, "writeStreamToFileWithListener==" + Log.getStackTraceString(e))
        }
        return false
    }
}


