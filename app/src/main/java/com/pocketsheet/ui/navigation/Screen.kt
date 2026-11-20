package com.pocketsheet.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Overview : Screen("overview/{characterId}") {
        fun createRoute(characterId: Long) = "overview/$characterId"
    }
    object Stats : Screen("stats/{characterId}") {
        fun createRoute(characterId: Long) = "stats/$characterId"
    }
    object Combat : Screen("combat/{characterId}") {
        fun createRoute(characterId: Long) = "combat/$characterId"
    }
    object Lore : Screen("lore/{characterId}") {
        fun createRoute(characterId: Long) = "lore/$characterId"
    }
    object Equip : Screen("equip/{characterId}") {
        fun createRoute(characterId: Long) = "equip/$characterId"
    }
}
