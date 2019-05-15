package com.consolebot.commands.impl

import com.consolebot.commands.BaseApplication
import com.consolebot.commands.Context
import com.consolebot.commands.validations.NSFWValidation
import net.dv8tion.jda.core.Permission
import java.util.*

/**
 * Created by DeStilleGast 14-5-2019
 */
class test : BaseApplication("test") {

    init {
        registerValidation(NSFWValidation())
    }

    override fun execute(context: Context) {
        context.reply("Test completed")
    }

    override fun requireBotPermission(): List<Permission> {
        return Arrays.asList(Permission.ADMINISTRATOR, Permission.MANAGE_CHANNEL, Permission.MESSAGE_MANAGE)
    }
}