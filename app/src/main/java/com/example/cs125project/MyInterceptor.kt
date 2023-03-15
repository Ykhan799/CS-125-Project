package com.example.cs125project

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key","w7B4saU7cFfnd07VxcMLTw==4rKIFfFCGK21aA7n")
            .build()
        return chain.proceed(request)
    }
}