package cn.diantongren.plugins

import cn.diantongren.service.ArticleService
import cn.diantongren.service.AuthService
import cn.diantongren.service.CommentService
import cn.diantongren.service.ProfileService
import cn.diantongren.service.impl.ArticleServiceImpl
import cn.diantongren.service.impl.AuthServiceImpl
import cn.diantongren.service.impl.CommentServiceImpl
import cn.diantongren.service.impl.ProfileServiceImpl
import io.ktor.application.*
import org.koin.dsl.module
import org.koin.ktor.ext.Koin

val serviceKoinModule = module {
    single<AuthService> { AuthServiceImpl() }
    single<ProfileService> { ProfileServiceImpl() }
    single<ArticleService> { ArticleServiceImpl() }
    single<CommentService> { CommentServiceImpl() }
}

fun Application.koin() {
    install(Koin) {
        modules(serviceKoinModule)
    }
}