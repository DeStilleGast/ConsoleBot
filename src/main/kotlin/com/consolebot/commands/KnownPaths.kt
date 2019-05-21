package com.consolebot.commands

/**
 * Created by DeStilleGast 13-5-2019
 */
enum class KnownPaths(val path: String) {
    ROOT(""),
    SYSTEM("/core"),
    GUILD("/guild"),
    GUILDOTHER("/guild/<guildid>"),
    FUN("/fun"),
    AUDIO("/music"),
    USER("/user/<userid>");

//    var path: String

//        private set
//    var symlinks: List<String>? = null

//    constructor(path: String) {
//        this.path = path
//    }

//    private constructor(path: String, vararg aliases: String) {
//        this.path = path
//        this.symlinks = Arrays.asList(*aliases)
//    }


}
