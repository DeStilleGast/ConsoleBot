package com.consolebot.commands

import java.util.*

/**
 * Created by DeStilleGast 13-5-2019
 */
enum class KnownPaths {
    ROOT("/"),
    SYSTEM("/core"),
    GUILD("/guild"),
    FUN("/fun"),
    AUDIO("/music"),
    USER("/user/<userid>");

    var path: String

        private set
    var symlinks: List<String>? = null

    private constructor(path: String) {
        this.path = path
    }

    private constructor(path: String, vararg aliases: String) {
        this.path = path
        this.symlinks = Arrays.asList(*aliases)
    }
}
