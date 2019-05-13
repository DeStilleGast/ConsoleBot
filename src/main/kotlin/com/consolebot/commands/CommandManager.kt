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

    fun registerCommand(cmd: BaseApplication){
        var actualCommand = combinePathAndFile(cmd.getPath(), cmd.filename)
        commandMap.put(actualCommand, cmd)
        println("'$actualCommand' was registered")
    }

    private fun combinePathAndFile(path: String, file: String): String{
        if(path.endsWith("/"))
            return path + file

        return "$path/$file"
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        var commandLine = event.message?.contentRaw

        if (commandLine is String) {
            if (commandLine.startsWith("run>")) {
                commandLine = commandLine.substring(4).trim()                                              // Strip "run>" and trim the rest (removes the space at the start if needed)
                if(!commandLine.startsWith('/')) commandLine = "/$commandLine"                                 // Every command should start with /, this will fix it for people that don't know that

                val hasSpaceInMessage = commandLine.contains(" ")                                             // check if the commandline has a space
                val filename = if (hasSpaceInMessage) commandLine.split(" ")[0] else commandLine          // retrive acual command (file)
                val arguments: List<String> =
                    if(hasSpaceInMessage)
                        commandLine.substring(commandLine.indexOf(' ') + 1).split(" ")      // Get list of arguments
                    else
                        Arrays.asList("")

                if (commandMap.containsKey(filename)) { // check if application/command exist, if yes, run command
                    commandMap[filename]!!.execute(Context(event.textChannel, event.author, event.message, arguments))
                }
            }
        }

        if(event.guild != null){
            DatabaseWrapper.getGuildSafe(event.guild).thenAccept { stored
             ->
                if (event.author.isBot){
                    return@thenAccept

                }

            }
        }
    }
}