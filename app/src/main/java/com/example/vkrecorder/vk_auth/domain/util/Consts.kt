package com.example.vkrecorder.vk_auth.domain.util

import com.vk.api.sdk.auth.VKScope

class Consts {

    companion object {
        val SCOPES: Collection<VKScope> = arrayListOf(VKScope.DOCS)
    }
}
