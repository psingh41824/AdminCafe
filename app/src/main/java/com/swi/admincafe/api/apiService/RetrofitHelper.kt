package com.swi.admincafe.api.apiService

import android.content.Context
import com.google.gson.Gson
import com.makeramen.roundedimageview.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object RetrofitHelper {

    var BASE_URL1 = "https://apicafe.cyclic.app"
    fun getInstance(context: Context): APIService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .retryOnConnectionFailure(false)
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(Interceptor addInterceptor@{ chain: Interceptor.Chain ->
                var response: Response? = null
                try {
                    val request = chain.request()
                    response = chain.proceed(request)
                    if (response.code == 200) {
                        if (
                            !request.url.toString().contains("/api/category") &&
                            !request.url.toString().contains("/api/banner") &&
                            !request.url.toString().contains("/api/product")
                        ) {
                            try {
                                val jsonObject = JSONObject()
                                jsonObject.put("message", JSONObject(response.body!!.string()))
                                val data =
                                    jsonObject.getJSONObject("message").getString("Data")
                                val destr = Aes256.decrypt(
                                    data,
                                    SimpleDateFormat(
                                        "yyyyMMdd",
                                        Locale.ENGLISH
                                    ).format(Date()) + "1201"
                                )
                                if (BuildConfig.DEBUG) {
                                    printMsg(destr)
                                }
                                val contentType = response.body!!.contentType()
                                val responseBody = destr.toResponseBody(contentType)
                                return@addInterceptor response.newBuilder().body(responseBody)
                                    .build()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    if (response.code == 401) {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return@addInterceptor response ?: Response.Builder()
                    .code(200).request(chain.request())
                    .protocol(Protocol.HTTP_1_0).message("asd")
                    .body("asd".toResponseBody())
                    .build()
            })
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                    .build()
                return@Interceptor chain.proceed(request)
            })
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(APIService::class.java)

    }

    private fun printMsg(msg: String) {
        val chunkCount = msg.length / 4050 // integer division
        for (i in 0..chunkCount) {
            val max = 4050 * (i + 1)
            if (max >= msg.length) {
                println(msg.substring(4050 * i))
            } else {
                println(msg.substring(4050 * i, max))
            }
        }
    }
}