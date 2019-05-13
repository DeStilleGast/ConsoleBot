package com.consolebot.database

import com.consolebot.Main
import com.consolebot.database.schema.Guilds
import com.consolebot.extensions.asyncTransaction
import net.dv8tion.jda.core.entities.Guild
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

data class DBGuild(
    val id: Long,
    val name: String
)

object DatabaseWrapper{
    private val pool: ExecutorService = Main.pool

    fun getGuild(guild: Guild) = getGuild(guild.idLong)

    fun getGuild(id: Long) = asyncTransaction(pool){
        val guild = Guilds.select{ Guilds.id.eq(id)}.firstOrNull()

        if(guild == null){
            throw Exception("Guild not found")
        } else {
            return@asyncTransaction DBGuild(
                guild[Guilds.id],
                guild[Guilds.name]
            )
        }
    }.execute()

    fun newGuild(guild: Guild) = asyncTransaction(pool){
        val selection = Guilds.select{
            Guilds.id.eq(guild.idLong)
        }

        if(selection.empty()){
            Guilds.insert {
                it[id] = guild.idLong
                it[name] = guild.name
            }
        }
    }.execute().exceptionally {
        Main.LOGGER.error("Error while trying to insert guild with ID ${guild.id}", it)
    }

    fun getGuildSafe(guild: Guild): CompletableFuture<DBGuild> = asyncTransaction(pool){
        val stored = Guilds.select { Guilds.id.eq(guild.idLong)}.firstOrNull()

        if(stored == null){
            Guilds.insert {
                it[id] = guild.idLong
                it[name] = guild.name
            }

            DBGuild(
                guild.idLong,
                guild.name
            )
        } else{
            DBGuild(
                stored[Guilds.id],
                stored[Guilds.name]
            )
        }
    }.execute()
}