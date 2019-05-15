package com.consolebot

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.CommandManager
import com.consolebot.database.DatabaseWrapper
import com.consolebot.database.schema.Guilds
import com.consolebot.database.schema.Users
import com.consolebot.extensions.asyncTransaction
import com.consolebot.processlist.ActiveApplications
import com.consolebot.settings.Settings
import mu.KotlinLogging
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.bot.sharding.ShardManager
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.events.Event
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.ShutdownEvent
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.hooks.EventListener
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.reflections8.Reflections
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Main(botSettings: Settings) : EventListener {

    companion object {
        @JvmField
        val LOGGER = KotlinLogging.logger(Main::class.java.name)

        val pool: ExecutorService by lazy {
            Executors.newCachedThreadPool {
                Thread(it, "Console-Bot-Pool-Thread").apply {
                    isDaemon = true
                }
            }
        }

        var jda: JDA? = null

        lateinit var shardManager: ShardManager
    }

    private val commandManager: CommandManager = CommandManager()

    init {
        println("LOGGER.isErrorEnabled = ${LOGGER.isErrorEnabled}")
        println("LOGGER.isWarnEnabled = ${LOGGER.isWarnEnabled}")
        println("LOGGER.isInfoEnabled = ${LOGGER.isInfoEnabled}")
        println("LOGGER.isDebugEnabled = ${LOGGER.isDebugEnabled}")
        println("LOGGER.isTraceEnabled = ${LOGGER.isTraceEnabled}")

        Database.connect(
            "jdbc:mysql://${botSettings.DBHost}:${botSettings.DBPort}/${botSettings.DBSchema}?autoReconnect=true&maxReconnects=5&serverTimezone=${TimeZone.getDefault().id}",
            "com.mysql.cj.jdbc.Driver",
            botSettings.DBUsername,
            botSettings.DBPassword
        )
        asyncTransaction(pool){
            SchemaUtils.create(
                Guilds,
                Users
            )
        }.execute()


        findAndRegisterCommands()
        build(0,
            botSettings.ShardTotal - 1,
            botSettings.ShardTotal, botSettings.bottoken)

    }

    fun build(token: String){
        jda = JDABuilder(AccountType.BOT)
            .setToken(token)
            .setAudioEnabled(false)
            .setAutoReconnect(true)
            .addEventListener(this)
            .addEventListener(commandManager)
            .build()

        Main.jda = jda
    }

    fun build(firstShard: Int, lastShard: Int, total: Int, token: String){
        val mainInstance = this
        shardManager = DefaultShardManagerBuilder().apply {
            setToken(token)
            addEventListeners(mainInstance)
            addEventListeners(commandManager)
            setAutoReconnect(true)
            setAudioEnabled(false)
            setBulkDeleteSplittingEnabled(false)
            setShardsTotal(total)
            setShards(firstShard, lastShard)
        }.build()
    }

    override fun onEvent(event: Event?) {
        if (event is ReadyEvent) {
            println("Bot is ready !")
            println("Bot is in ${event.guildTotalCount} guilds with ${event.guildAvailableCount} available")
        }else if(event is GuildJoinEvent){
            DatabaseWrapper.newGuild(event.guild)
            LOGGER.info("Joined ${event.guild.name}") // quick logging
        }else if(event is GuildLeaveEvent){
            LOGGER.info("Left ${event.guild.name}") // quick logging
        }else if(event is ShutdownEvent){
            System.exit(0)
        }

        ActiveApplications.onEvent(event)
    }

    /**
     * Attempt and find commands in commands implementations (and subpackages)
     */
    private fun findAndRegisterCommands() {
        val reflections = Reflections("com.consolebot.commands.impl")

        val subTypes = reflections.getSubTypesOf(BaseApplication::class.java)

        subTypes.forEach {
            commandManager.registerCommand(it.newInstance())
        }
    }

}