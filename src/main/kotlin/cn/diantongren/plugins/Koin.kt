package cn.diantongren.plugins

import cn.diantongren.service.impl.ArticleServiceImpl
import cn.diantongren.service.impl.AuthServiceImpl
import cn.diantongren.service.impl.CommentServiceImpl
import cn.diantongren.service.impl.ProfileServiceImpl
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

val serviceKoinModule = module {
    single { AuthServiceImpl() }
    single { ProfileServiceImpl() }
    single { ArticleServiceImpl() }
    single { CommentServiceImpl() }
}

fun Application.koin() {
    install(Koin) {
        modules(serviceKoinModule)
    }
}