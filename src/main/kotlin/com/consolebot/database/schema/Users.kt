package com.consolebot.database.schema

import com.consolebot.database.EnumTerminalStyle
import org.jetbrains.exposed.sql.Table

/**
 * Created by DeStilleGast 15-5-2019
 */
object Users : Table() {
    val id = long("id")
        .uniqueIndex()
        .primaryKey()
        .autoIncrement()

    val userId = long("userId").uniqueIndex()

    val terminalStyle = enumeration("terminalStyle", EnumTerminalStyle.WINDOWS.javaClass)
        .default(EnumTerminalStyle.WINDOWS)
}