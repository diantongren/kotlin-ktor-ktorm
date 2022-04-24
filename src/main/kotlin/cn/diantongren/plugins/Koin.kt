package cn.diantongren.plugins

import cn.diantongren.service.AuthService
import cn.diantongren.service.impl.AuthServiceImpl
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

//val databaseKoinModule = module {
//    single<Database> { DatabaseFactory().database }
//}

val serviceKoinModule = module {
    single<AuthService> { AuthServiceImpl() }
//    single<ProfileService> { ProfileServiceImpl(get()) }
}

fun Application.koin() {
    install(Koin) {
//        modules(databaseKoinModule)
        modules(serviceKoinModule)
    }
}