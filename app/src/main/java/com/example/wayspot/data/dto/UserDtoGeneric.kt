package com.example.wayspot.data.dto

import com.example.wayspot.data.model.user.UserInfo

abstract class UserDtoGeneric {

    abstract fun toUserInfo(): UserInfo

}
