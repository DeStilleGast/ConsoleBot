package com.consolebot.commands

import com.consolebot.Main
import com.consolebot.commands.validations.ValidationResult
import com.consolebot.database.DatabaseWrapper
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.streams.toList


/**
 * Created by DeStilleGast 12-5-2019
 */

object CommandManager : ListenerAdapter() {

    private val commandMap: MutableList<Pair<String, BaseApplication>> = ArrayList()

    fun registerCommand(cmd: BaseApplication) {
        val actualCommand = combinePathAndFile(cmd.getPath(), cmd.filename)
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
            var filename = if (hasSpaceInMessage) commandLine.split(" ")[0] else commandLine          // retrive acual command (file)
//            val arguments: List<String> =
//                if (hasSpaceInMessage)
//                    commandLine.substring(commandLine.indexOf(' ') + 1).split(" ")      // Get list of arguments
//                else
//                    Arrays.asList("")

            val pathArguments = resolvePath(event.jda, filename)
            filename = pathArguments.first ?: filename

            if (commandMap.map { it.first }.contains(filename)) { // check if application/command exist, if yes, run command
                val app = commandMap.first { it.first == filename }.second
                val context = Context(
                    event.channel,
                    event.author,
                    event.message,
                    parseArguments(commandLine.substring(commandLine.indexOf(' ') + 1)),
                    pathArguments.second
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

        val toReturn: MutableList<String> = ArrayList()

        while(m.find()){
            toReturn.add(m.group())
        }

        return toReturn
    }


    val patternMatcher = Regex("<.*>")
    private fun resolvePath(bot: JDA, input: String): Pair<String?, List<Pair<String, Any?>>>{
        val split = input.split("/")
        var thisCommand: BaseApplication? = null
        val pathArguments: MutableList<Pair<String, Any?>> = ArrayList()

        commandMap.forEach {
            val path = it.first

            if(path.startsWith(split[0]) && path.endsWith(split[split.size-1])) {
                thisCommand = it.second

                for(i in 0 until split.size){
                    val pathSplit = path.split("/")[i]
                    if(patternMatcher.matches(pathSplit)){
                        pathArguments.add(Pair(pathSplit, resolveObject(bot, pathSplit, split[i])))
                    }
                }
            }
        }

        return if(thisCommand != null) {
            Pair(thisCommand!!.getPath().path + "/${split[split.size-1]}", pathArguments.toList())
        }else{
            Pair(input, pathArguments.toList())
        }
    }

    private fun resolveObject(bot: JDA, pattern: String, extraInfo: String): Any?{
        var toReturn: Any? = null
        if(pattern in listOf("<userid>", "<user_id>")){
            try {
                toReturn = bot.getUserById(extraInfo)
            }catch (ex: NumberFormatException){
                toReturn = bot.getUsersByName(extraInfo, true)
            }
        }

        return toReturn
    }

}