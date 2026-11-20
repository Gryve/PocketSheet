package com.pocketsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pocketsheet.data.local.PocketSheetDatabase
import com.pocketsheet.data.repository.CharacterRepositoryImpl
import com.pocketsheet.domain.repository.CharacterRepository
import com.pocketsheet.ui.combat.CombatScreen
import com.pocketsheet.ui.equip.EquipScreen
import com.pocketsheet.ui.home.HomeScreen
import com.pocketsheet.ui.home.HomeViewModel
import com.pocketsheet.ui.lore.LoreScreen
import com.pocketsheet.ui.navigation.Screen
import com.pocketsheet.ui.overview.CharacterOverviewScreen
import com.pocketsheet.ui.overview.OverviewViewModel
import com.pocketsheet.ui.stats.StatsScreen
import com.pocketsheet.ui.theme.PocketSheetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = PocketSheetDatabase.getInstance(applicationContext)
        val repository: CharacterRepository = CharacterRepositoryImpl(database.characterDao())

        setContent {
            PocketSheetTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PocketSheetApp(repository = repository)
                }
            }
        }
    }
}

@Composable
fun PocketSheetApp(repository: CharacterRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val homeViewModel = remember { HomeViewModel(repository) }
            HomeScreen(
                viewModel = homeViewModel,
                onCharacterClick = { charId ->
                    navController.navigate(Screen.Overview.createRoute(charId))
                }
            )
        }

        composable(
            route = Screen.Overview.route,
            arguments = listOf(navArgument("characterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val charId = backStackEntry.arguments?.getLong("characterId") ?: return@composable
            val overviewViewModel = remember(charId) { OverviewViewModel(charId, repository) }

            CharacterOverviewScreen(
                viewModel = overviewViewModel,
                onBackClick = { navController.popBackStack() },
                onNavigateStats = { navController.navigate(Screen.Stats.createRoute(it)) },
                onNavigateCombat = { navController.navigate(Screen.Combat.createRoute(it)) },
                onNavigateLore = { navController.navigate(Screen.Lore.createRoute(it)) },
                onNavigateEquip = { navController.navigate(Screen.Equip.createRoute(it)) }
            )
        }

        composable(
            route = Screen.Stats.route,
            arguments = listOf(navArgument("characterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val charId = backStackEntry.arguments?.getLong("characterId") ?: return@composable
            val overviewViewModel = remember(charId) { OverviewViewModel(charId, repository) }

            StatsScreen(
                viewModel = overviewViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Combat.route,
            arguments = listOf(navArgument("characterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val charId = backStackEntry.arguments?.getLong("characterId") ?: return@composable
            val overviewViewModel = remember(charId) { OverviewViewModel(charId, repository) }

            CombatScreen(
                viewModel = overviewViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Lore.route,
            arguments = listOf(navArgument("characterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val charId = backStackEntry.arguments?.getLong("characterId") ?: return@composable
            val overviewViewModel = remember(charId) { OverviewViewModel(charId, repository) }

            LoreScreen(
                viewModel = overviewViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Equip.route,
            arguments = listOf(navArgument("characterId") { type = NavType.LongType })
        ) { backStackEntry ->
            val charId = backStackEntry.arguments?.getLong("characterId") ?: return@composable
            val overviewViewModel = remember(charId) { OverviewViewModel(charId, repository) }

            EquipScreen(
                viewModel = overviewViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
