package cn.diantongren.plugins

import cn.diantongren.service.AuthService
import cn.diantongren.service.AuthServiceImpl
import cn.diantongren.service.DatabaseFactory
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

val databaseKoinModule = module {
    single { DatabaseFactory().database }
}

val serviceKoinModule = module {
    single<AuthService> { AuthServiceImpl(get()) }
}

fun Application.koin() {
    install(Koin) {
        modules(databaseKoinModule)
        modules(serviceKoinModule)
    }
}