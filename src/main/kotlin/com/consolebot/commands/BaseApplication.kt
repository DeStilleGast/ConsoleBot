package com.consolebot.commands

import net.dv8tion.jda.core.Permission
import java.util.*

/**
 * Created by DeStilleGast 12-5-2019
 */

/**
 * @param filename Set filename for command WITHOUT EXTENSION
 */
abstract class BaseApplication(var filename: String) {

    abstract fun execute(context: Context)

    /**
     * Return path for "application"
     */
    fun getPath(): String{
        return KnownPaths.ROOT.path
    }

    /**
     * @return list of permissions that the bot requires to run this command
     */
    fun requireBotPermission() : List<Permission>{
        return Collections.emptyList()
    }

    /**
     * @return list of permissions that the user requires to run this command
     */
    fun requireUserPermission() : List<Permission>{
        return Collections.emptyList()
    }
}