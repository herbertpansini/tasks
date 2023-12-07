package com.ztec.tasks.service.constants

class TaskConstants private constructor() {
    object USER {
        const val ID = "id"
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val DEVICE = "device"
        const val ROLE = "role"
    }

    object HEADER {
        const val TOKEN_KEY = "Authorization"
        const val TOKEN_VALUE = "tokenvalue"
    }

    object HTTP {
        val SUCCESS = arrayOf(200, 201, 204)
    }

    object BUNDLE {
        const val TASKID = "taskid"
    }
}