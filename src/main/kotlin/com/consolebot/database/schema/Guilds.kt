package com.consolebot.database.schema

import org.jetbrains.exposed.sql.Table

object Guilds : Table() {
    val id = long("id")
        .uniqueIndex()
        .primaryKey()
    val name = varchar("name", 300)
}