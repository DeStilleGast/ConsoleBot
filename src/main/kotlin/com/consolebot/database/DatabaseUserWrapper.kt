package com.consolebot.database

import com.consolebot.Main
import com.consolebot.database.schema.Users
import com.consolebot.extensions.asyncTransaction
import net.dv8tion.jda.core.entities.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

/**
 * Created by DeStilleGast 15-5-2019
 */

data class DBUser(
    val id: Long,
    val userId: Long,
    val terminalStyle: EnumTerminalStyle
)

object DatabaseUserWrapper{
    private val pool = Main.pool

    fun getUser(user: User) = getUser(user.idLong)

    fun getUser(userId: Long) = asyncTransaction<DBUser>(pool){
        val user = Users.select{ Users.userId.eq(userId)}.firstOrNull()

        if(user == null){
            val newEntity = Users.insert { it[Users.userId] = userId }
            return@asyncTransaction DBUser(
                newEntity[Users.id],
                newEntity[Users.userId],
                EnumTerminalStyle.WINDOWS
            )
        }else{
            return@asyncTransaction DBUser(
                user[Users.id],
                user[Users.userId],
                user[Users.terminalStyle]
            )
        }
    }.execute().exceptionally {
        Main.LOGGER.error("Failed to retrive or create new DBUser ${userId}", it)
        null
    }
}