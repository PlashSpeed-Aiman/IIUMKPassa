package org.forthify.passxplat.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import org.forthify.passxplat.logic.ImaalumService
import org.forthify.passxplat.logic.LoginService
import org.forthify.passxplat.logic.SettingsService
import org.koin.dsl.module

val commonModule = module {
    single {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 20000
            }
            install(HttpCookies){
                storage = AcceptAllCookiesStorage()
        }


    }
}
    single {
        SettingsService(get())
    }

    single{
        ImaalumService(get(), get(),get())
    }

    single{
        LoginService(get(),get())
    }



}