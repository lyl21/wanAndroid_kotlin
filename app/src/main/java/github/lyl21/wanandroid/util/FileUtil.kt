package github.lyl21.wanandroid.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.luck.picture.lib.tools.SdkVersionUtils
import okhttp3.internal.and
import java.io.*
import java.lang.Exception
import java.lang.StringBuilder
import kotlin.experimental.and

/**
 * @类说明 文件操作类
 * @作者 hhsoft
 * @创建日期 2019/8/19 16:47
 */
object FileUtil {
    /**
     * 判断Uri是否是content
     *
     * @return
     */
    fun checkedUriScheme_CONTENT(uri: Uri?): Boolean {
        if (uri == null) {
            return false
        }
        val scheme = uri.scheme
        return !TextUtils.isEmpty(scheme) && "content".equals(scheme, ignoreCase = true)
    }

    /**
     * 判断SD卡是否装载
     *
     * @return true:sd卡已装载；false:sd卡未装载
     */
    val isSDExist: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return state == Environment.MEDIA_MOUNTED
        }

    /**
     * 判断一个文件是否存在
     *
     * @param filePath 文件或者文件夹的路径
     * @return true：文件存在  false:文件不存在
     */
    fun isFileExist(filePath: String?): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    /**
     * 在Andorid Q 中，当我们通过Content Uri拿到路径之后，是无法通过File来判断文件是否存在，即file.exist()会总是为False。所以我们借助于ContentResolver来判断
     * 对应于一个同步 I/O 调用，易造成线程等待
     *
     * @param context
     * @param uri
     * @return
     */
    fun isContentUriExists(context: Context?, uri: Uri?): Boolean {
        if (null == context) {
            return false
        }
        val cr = context.contentResolver
        try {
            val afd = cr.openAssetFileDescriptor(uri!!, "r")
            if (null == afd) {
                return false
            } else {
                try {
                    afd.close()
                } catch (e: IOException) {
                }
            }
        } catch (e: FileNotFoundException) {
            return false
        }
        return true
    }

    /**
     * 创建一个目录
     *
     * @param dirName 目录名（路径）
     * @return File：当前目录文件
     */
    fun createDirectory(dirName: String?): File {
        val file = File(dirName)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }

    /**
     * 删除单个文件
     *
     * @param filePath 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    fun deleteFile(filePath: String?): Boolean {
        if (TextUtils.isEmpty(filePath)) {
            return false
        }
        val file = File(filePath)
        return if (file.exists() && file.isFile) {
            file.delete()
        } else false
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param filePath 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    fun deleteDirectory(filePath: String): Boolean {
        var filePath = filePath
        var isSuccess = true
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath += File.separator
        }
        val dirFile = File(filePath)
        if (dirFile.exists() && dirFile.isDirectory) {
            // 删除文件夹中的所有文件包括子目录
            val files = dirFile.listFiles()
            for (file in files) {
                isSuccess = if (file.isFile) {
                    //删除子文件
                    deleteFile(file.absolutePath) && isSuccess
                } else {
                    //删除子目录
                    deleteDirectory(file.absolutePath) && isSuccess
                }
            }
            //删除当前目录
            isSuccess = dirFile.delete() && isSuccess
        } else {
            isSuccess = false
        }
        return isSuccess
    }

    /**
     * 根据路径删除指定的目录或文件
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false
     */
    fun deleteFolder(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            if (file.isFile) {
                //为文件时调用删除文件方法
                deleteFile(filePath)
            } else {
                //为目录时调用删除目录方法
                deleteDirectory(filePath)
            }
        } else false
    }

    /**
     * 复制单个文件
     * 适配AndroidQ
     * @param context
     * @param srcUri
     * @param targetFilePath
     * @return
     */
    fun copyFile(context: Context, srcUri: Uri?, targetFilePath: String?): Boolean {
        try {
            if (srcUri == null) {
                return false
            }
            if (checkedUriScheme_CONTENT(srcUri)) {
                if (isContentUriExists(context, srcUri)) {
                    val inputStream =
                        context.contentResolver.openInputStream(srcUri) ?: return false
                    val outputStream: OutputStream = FileOutputStream(File(targetFilePath))
                    copyStream(inputStream, outputStream)
                    inputStream.close()
                    outputStream.close()
                    return true
                }
            } else {
                if (isFileExist(srcUri.path)) {
                    val targetFile = File(targetFilePath)
                    val fis = FileInputStream(srcUri.path)
                    val inChannel = fis.channel
                    val fos = FileOutputStream(targetFile)
                    val outChannel = fos.channel
                    inChannel.transferTo(0, inChannel.size(), outChannel)
                    inChannel.close()
                    outChannel.close()
                    fis.close()
                    fos.close()
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 复制单个文件
     * 适配AndroidQ以下
     *
     * @param sourceFilePath 原文件路径
     * @param targetFilePath 目标文件路径
     * @return 复制成功返回true，出现异常返回false
     */
    fun copyFile(sourceFilePath: String?, targetFilePath: String?): Boolean {
        val file = File(sourceFilePath)
        if (file.exists() && file.isFile) {
            try {
                val targetFile = File(targetFilePath)
                val fis = FileInputStream(file)
                val inChannel = fis.channel
                val fos = FileOutputStream(targetFile)
                val outChannel = fos.channel
                inChannel.transferTo(0, inChannel.size(), outChannel)
                inChannel.close()
                outChannel.close()
                fis.close()
                fos.close()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return false
    }

    /**
     * 复制流
     *
     * @param input
     * @param output
     * @return
     * @throws Exception
     * @throws IOException
     */
    @Throws(Exception::class, IOException::class)
    fun copyStream(input: InputStream?, output: OutputStream?): Int {
        val BUFFER_SIZE = 1024 * 2
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
            }
            try {
                `in`.close()
            } catch (e: IOException) {
            }
        }
        return count
    }

    /**
     * 目录及目录下的文件
     *
     * @param sourceFilePath 原文件路径
     * @param targetFilePath 目标文件路径
     * @return 复制成功返回true，出现异常返回false
     */
    fun copyDirectory(sourceFilePath: String?, targetFilePath: String): Boolean {
        var targetFilePath = targetFilePath
        var isSuccess = true
        val dirFile = File(sourceFilePath)
        if (dirFile.exists() && dirFile.isDirectory) {
            // 如果dir不以文件分隔符结尾，自动添加文件分隔符
            if (!targetFilePath.endsWith(File.separator)) {
                targetFilePath += File.separator
            }
            val targetFile = File(targetFilePath)
            if (!targetFile.exists()) {
                targetFile.mkdirs()
            }
            val files = dirFile.listFiles()
            for (file in files) {
                isSuccess = if (file.isFile) {
                    copyFile(file.absolutePath, targetFilePath + file.name) && isSuccess
                } else {
                    copyFolder(file.absolutePath, targetFilePath + file.name) && isSuccess
                }
            }
        } else {
            isSuccess = false
        }
        return isSuccess
    }

    /**
     * 复制指定的目录或文件
     *
     * @param sourceFilePath 原目录或文件路径
     * @param targetFilePath 目标目录或文件路径
     * @return 复制成功返回true，出现异常返回false
     */
    fun copyFolder(sourceFilePath: String?, targetFilePath: String): Boolean {
        val file = File(sourceFilePath)
        return if (file.exists()) {
            if (file.isFile) {
                //为文件时调用复制文件方法
                copyFile(sourceFilePath, targetFilePath)
            } else {
                //为目录时调用复制目录方法
                copyDirectory(sourceFilePath, targetFilePath)
            }
        } else false
    }

    /**
     * 重命名指定的目录或文件
     *
     * @param sourceFilePath 原目录或文件路径
     * @param targetFilePath 重命名目录或文件路径
     */
    fun renameFolder(sourceFilePath: String?, targetFilePath: String?) {
        val oldFile = File(sourceFilePath)
        if (oldFile.exists()) {
            val newFile = File(targetFilePath)
            oldFile.renameTo(newFile)
        }
    }

    /**
     * 保存Bitmap到本地文件
     *
     * @param bitmap   图片的bitmap对象
     * @param filePath 图片保存的文件路径
     * @param quality  图片压缩率，eg:不压缩是100，即压缩率为0
     * @return
     */
    fun saveBitmapToFile(bitmap: Bitmap, filePath: String?, quality: Int): Boolean {
        var isSuccess = false
        try {
            val file = File(filePath)
            var out: FileOutputStream? = FileOutputStream(file)
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                isSuccess = true
                out!!.flush()
                out.close()
                out = null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            LogUtils.e("FileUtils", "saveBitmapToFile==" + Log.getStackTraceString(e))
        }
        return isSuccess
    }

    /**
     * 内部存储：向指定的文件中写入指定的数据：文件默认存储位置：/data/data/包名/files/文件名（fileName）
     *
     * @param fileName 文件名 eg:test
     * @param content  文件内容
     * @return 写入成功返回true，写入失败返回false
     */
    fun writeFileData(fileName: String?, content: String): Boolean {
        try {
            //获得FileOutputStream
            val file = File(fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fout = FileOutputStream(file)
            //将要写入的字符串转换为byte数组
            val bytes = content.toByteArray()
            fout.write(bytes) //将byte数组写入文件
            fout.close() //关闭文件输出流
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 内部存储：读取一个内部存储的私有文件
     *
     * @param fileName 文件名 eg:test
     * @return 读取内容为空，返回空字符串
     */
    fun readFileData(fileName: String?): String {
        try {
            val file = File(fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fis = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(fis))
            return reader.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 向指定的文件中写入指定的数据
     *
     * @param filePath 文件路径
     * @param content  文件内容
     * @return 写入成功返回true，写入失败返回false
     */
    fun writeFileDataToSDCard(filePath: String?, content: String): Boolean {
        try {
            if (isSDExist) {
                val file = File(filePath)
                if (!file.exists()) {
                    //先创建文件夹/目录
                    file.parentFile.mkdirs()
                    //再创建新文件
                    file.createNewFile()
                }
                val fout = FileOutputStream(file)
                //将要写入的字符串转换为byte数组
                val bytes = content.toByteArray()
                fout.write(bytes) //将byte数组写入文件
                fout.close() //关闭文件输出流
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 读取SDCard文件内容
     *
     * @param filePath 文件名路径
     * @return 读取内容为空，返回空字符串
     */
    fun readFileDataFromSDCard(filePath: String?): String {
        try {
            val file = File(filePath)
            if (!file.exists()) {
                //先创建文件夹/目录
                file.parentFile.mkdirs()
                //再创建新文件
                file.createNewFile()
            }
            val fis = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(fis))
            return reader.readLine()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 获取指定目录/文件的大小
     *
     * @param filePath 目录/文件的路径
     * @return 指定目录/文件的大小
     */
    fun fileSize(filePath: String?): Long {
        var size: Long = 0
        if (!TextUtils.isEmpty(filePath)) {
            val file = File(filePath)
            if (file.isDirectory) {
                val files = file.listFiles()
                if (files != null && files.size > 0) {
                    for (f in files) {
                        size += fileSize(f.absolutePath)
                    }
                }
            } else {
                size = file.length()
            }
        }
        return size
    }

    /**
     * 根据图片的文件头判断图片类别
     * 适配Android Q
     *
     * @param context
     * @param filePath 文件路径(Android Q 以上不是文件的绝对路径)
     * @return 返回自定义文件枚举类型FileType
     */
    fun fileTypeForImageData(context: Context, filePath: String?): FileType {
        val b = ByteArray(4)
        var `is`: InputStream? = null
        try {
            `is` = if (SdkVersionUtils.isQ()) {
                if (checkedUriScheme_CONTENT(Uri.parse(filePath))) {
                    context.applicationContext.contentResolver.openInputStream(Uri.parse(filePath))
                } else {
                    FileInputStream(filePath)
                }
            } else {
                FileInputStream(filePath)
            }
            `is`!!.read(b, 0, b.size)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(ContentValues.TAG, "fileTypeForImageData==" + Log.getStackTraceString(e))
        }
        val type = bytesToHexString(b)!!.toUpperCase()
        if (type.contains("FFD8FF")) {
            return FileType.IMAGE_JPEG
        } else if (type.contains("89504E47")) {
            return FileType.IMAGE_PNG
        } else if (type.contains("47494638")) {
            return FileType.IMAGE_GIF
        } else if (type.contains("49492A00")) {
            return FileType.IMAGE_TIFF
        } else if (type.contains("424D")) {
            return FileType.IMAGE_BMP
        } else if (type.contains("52494646")) {
            return FileType.IMAGE_WEBP
        }
        return FileType.IMAGE_JPEG
    }

    private fun bytesToHexString(src: ByteArray?): String? {
        val builder = StringBuilder()
        if (src == null || src.isEmpty()) {
            return null
        }
        var hv: String
        for (i in src.indices) {
            hv = Integer.toHexString(src[i] and  0xFF).uppercase()
            if (hv.length < 2) {
                builder.append(0)
            }
            builder.append(hv)
        }
        return builder.toString()
    }

    /**
     * 获取文件夹名字
     *
     * @param filePath
     * @return
     */
    fun dirNameByFilePath(filePath: String): String {
        return if (TextUtils.isEmpty(filePath)) {
            filePath
        } else {
            val lastSep = filePath.lastIndexOf(File.separator)
            if (lastSep == -1) "" else filePath.substring(0, lastSep + 1)
        }
    }

    /**
     * 保存Bitmap对象到文件
     *
     * @param bitmap         bitmap对象
     * @param targetFilePath 保存文件的路径
     * @return 返回保存文件的路径，保存失败返回空字符串
     */
    fun writeBitmapToFile(bitmap: Bitmap?, targetFilePath: String?): String? {
        if (bitmap == null) {
            return null
        }
        try {
            val file = File(targetFilePath)
            if (!file.exists()) {
                //先创建文件夹/目录
                file.parentFile.mkdirs()
                //再创建新文件
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
            return targetFilePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    /**
     * 根据Uri获取文件路径
     *
     * @param context
     * @param uri     待查找路径的Uri
     * @return 查询失败返回null
     */
    fun filePathByUri(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return filePathByUriForApi19(context, uri)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            // File
            return uri.path
        }
        return null
    }

    /**
     * 获取uri的文件路径，目标版本19
     *
     * @param context
     * @param uri
     * @return
     */
    private fun filePathByUriForApi19(context: Context, uri: Uri): String? {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return (context.getExternalFilesDir(null).toString() + "/"
                            + split[1])
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context, contentUri, selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }

    /**
     * 判断文件路径是否为网络路径
     *
     * @param filePath
     * @return
     */
    fun isHttpUrl(filePath: String): Boolean {
        return if (TextUtils.isEmpty(filePath)) {
            false
        } else filePath.startsWith("http") || filePath.startsWith("https")
    }

    /**
     * 自定义文件类型
     */
    enum class FileType {
        IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF, IMAGE_TIFF, IMAGE_WEBP, IMAGE_BMP
    }
}