package com.consolebot.commands

import com.consolebot.database.DatabaseWrapper
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*


/**
 * Created by DeStilleGast 12-5-2019
 */

class CommandManager : ListenerAdapter() {

    var commandMap: HashMap<String, BaseApplication> = HashMap()

    fun registerCommand(cmd: BaseApplication) {
        val actualCommand = combinePathAndFile(cmd.getPath(), cmd.filename)
        commandMap.put(actualCommand, cmd)
        println("'$actualCommand' was registered")
    }

    private fun combinePathAndFile(path: String, file: String): String {
        if (path.endsWith("/"))
            return path + file

        return "$path/$file"
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        val commandLine = event.message?.contentRaw
        val allowedPrefixes = Arrays.asList("run>", ">")

        // TODO: for later, unknown command message (only for 'run>' and not for shortcut)
        if (commandLine is String && !event.author.isBot) {
            allowedPrefixes.forEach { tryAndRunCommand(it, event) }
        }

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

            val hasSpaceInMessage =
                commandLine.contains(" ")                                             // check if the commandline has a space
            val filename =
                if (hasSpaceInMessage) commandLine.split(" ")[0] else commandLine          // retrive acual command (file)
            val arguments: List<String> =
                if (hasSpaceInMessage)
                    commandLine.substring(commandLine.indexOf(' ') + 1).split(" ")      // Get list of arguments
                else
                    Arrays.asList("")

            if (commandMap.containsKey(filename)) { // check if application/command exist, if yes, run command
                val app = commandMap[filename]!!
                val context = Context(
                    event.channel,
                    event.author,
                    event.message,
                    arguments
                )

                val errors = app.extraValidationChecks(context)

                if(errors.isNotEmpty()){
                   context.reply("There were some errors while attemping to start this application:\n- " + errors.joinToString("\n- "))
                }else {
                    app.execute(context)
                }
            }
        }
    }
}