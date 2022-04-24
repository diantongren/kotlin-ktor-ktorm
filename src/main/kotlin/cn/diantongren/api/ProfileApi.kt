package cn.diantongren.api

import cn.diantongren.service.ProfileService
import cn.diantongren.util.param
import cn.diantongren.util.userId
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.profile(profileService: ProfileService) {

    authenticate(optional = true) {

        /**
         *  Get Profile
         *  GET /profiles/:username
         */
        get("/profiles/{username}") {
            val username = call.param("username")
            val currentUserId = call.userId()
            val profile = profileService.getProfile(username, currentUserId)
            call.respond(profile)
        }

    }

    authenticate {

        /**
         *  Follow user
         *  POST /profiles/:username/follow
         */
        post("/profiles/{username}/follow") {
            val username = call.param("username")
            val currentUserId = call.userId()
            val profile = profileService.changeFollowStatus(username, currentUserId, true)
            call.respond(profile)
        }

        /**
         *  Unfollow user
         *  DELETE /profiles/:username/follow
         */
        delete("/profiles/{username}/follow") {
            val username = call.param("username")
            val currentUserId = call.userId()
            val profile = profileService.changeFollowStatus(username, currentUserId, false)
            call.respond(profile)
        }

    }
}