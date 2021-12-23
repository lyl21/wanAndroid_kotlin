package github.lyl21.wanandroid.http.datamanager

import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface DataManagerApiService {
    @Streaming
    @GET
    fun downloadFile(@Url url: String?): Observable<ResponseBody>

    @Multipart
    @POST
    fun callPostRequestMultipartURL(
        @Url url: String?,
        @Part paramMap: RequestBody?,
        @Part fileMap: List<Part?>?
    ): Call<String?>
}
