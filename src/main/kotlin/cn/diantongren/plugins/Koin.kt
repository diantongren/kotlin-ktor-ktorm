package cn.diantongren.plugins

import cn.diantongren.service.*
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.ktorm.database.Database

val databaseKoinModule = module {
    //single<DatabaseFactory> { DatabaseFactory() }
    single<Database> { DatabaseFactory().database }
}

val serviceKoinModule = module {
    single<AuthService> { AuthServiceImpl(get()) }
    single<ProfileService> { ProfileServiceImpl(get()) }
}

fun Application.koin() {
    install(Koin) {
        modules(databaseKoinModule)
        modules(serviceKoinModule)
    }
}