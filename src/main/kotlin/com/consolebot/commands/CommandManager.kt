package com.consolebot.commands

import com.consolebot.commands.validations.ValidationResult
import com.consolebot.database.DatabaseWrapper
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.streams.toList


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
//            val arguments: List<String> =
//                if (hasSpaceInMessage)
//                    commandLine.substring(commandLine.indexOf(' ') + 1).split(" ")      // Get list of arguments
//                else
//                    Arrays.asList("")

            if (commandMap.containsKey(filename)) { // check if application/command exist, if yes, run command
                val app = commandMap[filename]!!
                val context = Context(
                    event.channel,
                    event.author,
                    event.message,
                    parseArguments(commandLine.substring(commandLine.indexOf(' ') + 1))
                )

                val errors = app.runValidationCheck(context)

                if(errors.isNotEmpty()){
                   context.reply("There were some errors while attempting to start this application:\n- " + errors.stream().map(ValidationResult::reason).toList().joinToString("\n- "))
                }else {
                    app.execute(context)
                }
            }
        }
    }

    // https://stackoverflow.com/questions/10695143/split-a-quoted-string-with-a-delimiter
    private fun parseArguments(command: String): List<String> {
        val p = Pattern.compile("((?<=(\"))[\\w ]*(?=(\"(\\s|$))))|((?<!\")\\w+(?!\"))")
        val m = p.matcher(command)

        var toReturn: MutableList<String> = ArrayList()

        while(m.find()){
            toReturn.add(m.group())
        }

        return toReturn
    }
}