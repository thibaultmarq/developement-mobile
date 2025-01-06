package com.tibomrq.todotibomrq.data

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdate(
    val commands: List<Commands>,
) {
    companion object {
        fun userUpdate(uuid: String, fullName: String): UserUpdate {
            return UserUpdate(commands = listOf(Commands.createCommands(uuid, fullName)))
        }
    }

}

@Serializable
data class Commands(
    val type: String,
    val uuid: String,
    val args: Map<String, String>
) {
    companion object {
        fun createCommands(uuid: String, fullName: String): Commands {
            return Commands(
                type = "user_update",
                uuid = uuid,
                args = mapOf("full_name" to fullName)
            )
        }
    }
}
