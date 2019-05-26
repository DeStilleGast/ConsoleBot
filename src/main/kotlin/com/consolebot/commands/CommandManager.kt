package com.consolebot.commands

import com.consolebot.Main
import com.consolebot.commands.validations.ValidationResult
import com.consolebot.database.DatabaseWrapper
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList


/**
 * Created by DeStilleGast 12-5-2019
 */

object CommandManager : ListenerAdapter() {

    private val commandMap: MutableList<Pair<String, BaseApplication>> = ArrayList()

    fun registerCommand(cmd: BaseApplication) {
        val actualCommand = combinePathAndFile(cmd.getPath(), cmd.filename)

        if (cmd.filename.contains(" ")) {
            Main.LOGGER.error("Command '${cmd.filename}' has a space in its filename and is not registered !")
        }

        if (commandMap.map { it.first }.contains(actualCommand)) {
            Main.LOGGER.error("Command '$actualCommand' is already registered, change filename or path !")
        }

        commandMap.add(Pair(actualCommand, cmd))
        Main.LOGGER.info("Command '$actualCommand' was registered")
    }

    private fun combinePathAndFile(path: KnownPaths, file: String): String {
        if (path.path.endsWith("/"))
            return path.path + file

        return "${path.path}/$file"
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val commandLine = event.message?.contentRaw
        val allowedPrefixes = Arrays.asList("run>", ">")

        // TODO: for later, unknown command message (only for 'run>' and not for shortcut)
        if (commandLine is String && !event.author.isBot) {
            allowedPrefixes.forEach { tryAndRunCommand(it, event) }
        }

        // TODO: Uitleggen hoe dit nou precies in elkaar zit en hoe dit de overige db requests tegenhoud
        if (event.guild != null) {
            DatabaseWrapper.getGuildSafe(event.guild).thenAccept { stored
                ->
                if (event.author.isBot) {
                    return@thenAccept
                }
            }
        }
    }

    private fun tryAndRunCommand(prefix: String, event: MessageReceivedEvent) {
        var commandLine = event.message.contentRaw

        if (commandLine.startsWith(prefix)) {
            commandLine = commandLine.substring(prefix.length)
                .trim()                                              // Strip "run>" and trim the rest (removes the space at the start if needed)
            if (!commandLine.startsWith('/')) commandLine =
                "/$commandLine"                                 // Every command should start with /, this will fix it for people that don't know that

            var filename = ""
            val pathArguments = resolvePath(event.jda, commandLine)
            filename = pathArguments.first ?: filename

            if (commandMap.map { it.first }.contains(filename)) { // check if application/command exist, if yes, run command
                val app = commandMap.first { it.first == filename }.second
                val context = Context(
                    event.channel,
                    event.author,
                    event.message,
                    pathArguments.second,
                    pathArguments.third ?: ""
                )

                val errors = app.runValidationCheck(context)

                if (errors.isNotEmpty()) {
                    context.reply(
                        "There were some errors while attempting to start this application:\n- " + errors.stream().map(
                            ValidationResult::notPassedReason
                        ).toList().joinToString("\n- ")
                    )
                } else {
                    app.execute(context)
                }
            }
        }
    }


    val patternMatcher = Regex("<.*>")
    // Pair < actual command path, resolved users/guilds in path, rest of input>
    private fun resolvePath(bot: JDA, input: String): Triple<String?, List<Pair<String, Any?>>, String?> {
        var thisCommand: BaseApplication? = null
        var thisPath: String? = null
        val pathArguments: MutableList<Pair<String, Any?>> = ArrayList()

        var appArguments: String? = null

        for (it in commandMap) {
            thisPath = it.first
            val pathRegex = it.second.getRegexPath()

            if (input.matches(pathRegex)) {
                thisCommand = it.second
                appArguments = thisCommand.getRegexAppPath().replaceFirst(input, "").trim()

                // Splitting the path to resolve items
                val split = thisPath.split("/")

                for (i in 0 until split.size) {
                    val pathSplit = thisPath.split("/")[i]
                    if (patternMatcher.matches(pathSplit)) {
                        pathArguments.add(Pair(pathSplit, resolveObject(bot, pathSplit, input.split("/")[i])))
                    }
                }
                break
            }
        }


        // both are filled if thiscommand is not null
        return if (thisCommand != null && appArguments != null) {
            Triple(thisPath, pathArguments.toList(), appArguments)
        } else {
            val thinkcmd = input.split(" ")[0]
            Triple(thinkcmd, pathArguments.toList(), input.substring(thinkcmd.length))
        }
    }

    private fun resolveObject(bot: JDA, pattern: String, extraInfo: String): Any? {
        var toReturn: Any? = null
        if (pattern in listOf("<userid>", "<user_id>")) {
            try {
                toReturn = bot.getUserById(extraInfo)
            } catch (ignored: NumberFormatException) {
                toReturn = bot.getUsersByName(extraInfo.replace(" ", ""), true)
            }
        } else if (pattern in listOf("<guildid>", "<guild_id>")) {
            try {
                toReturn = bot.asBot().shardManager.getGuildById(extraInfo)
            } catch (ignored: NumberFormatException) {
                toReturn = bot.asBot().shardManager.getGuildsByName(extraInfo, true)
            }
        }

        return toReturn
    }

}