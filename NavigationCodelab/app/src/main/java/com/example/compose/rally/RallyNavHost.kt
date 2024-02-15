package com.example.compose.rally

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.compose.rally.ui.accounts.AccountsScreen
import com.example.compose.rally.ui.accounts.SingleAccountScreen
import com.example.compose.rally.ui.bills.BillsScreen
import com.example.compose.rally.ui.overview.OverviewScreen

// TODO 3 Create the NavHost and link it to the NavController
@Composable
fun RallyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier // accept the outer Scaffold padding and apply it to the NavHost
) {
    NavHost(navController = navController,
            startDestination = Overview.route,
            modifier = modifier)
    {
        // TODO 4 Build the NavGraph by defining the destinations
        // builder parameter is responsible for defining and building the navigation graph
        composable(route = Overview.route){
            // TODO 8 add the composable directly in the NavHost navigation graph
            OverviewScreen(
                onClickSeeAllAccounts = {
                    navController.navigateSingleTopTo(Accounts.route)
                },
                onClickSeeAllBills = {
                    navController.navigateSingleTopTo(Bills.route)
                },
                // TODO 12 pass the navigationAction callback
                onAccountClick = {  accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
            // Overview.screen()
        }
        composable(route = Accounts.route) {
            AccountsScreen(
                onAccountClick = {  accountType ->
                    navController.navigateToSingleAccount(accountType)
                }
            )
        }
        composable(route = Bills.route) {
            BillsScreen()
        }
        // TODO 9 passing Arguments to a route inside curly braces
        composable(route = SingleAccount.routeWithArgs,
            arguments = SingleAccount.arguments, // This also ensures we keep the NavHost as clean and readable as possible:
            // TODO 16 add list of deepLink
            deepLinks = SingleAccount.deepLinks
        ){ navBackStackEntry ->
            // TODO 11 request accountTypeArg(argument) from the navBackStackEntry
            val accountType = navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)
            // Pass accountType to SingleAccountScreen
            SingleAccountScreen(accountType)
        }
    }
}

// TODO 4 Create extension function to navigate and give behaviour to the app while navigating
fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // there will be at most one copy of a given destination on the top of the back stack
        launchSingleTop = true
        // pop up to the start destination of the graph to avoid building up a large stack of destinations on the back stack as you select tabs
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id){
            saveState = true
        }
        // re-tapping the same tab would keep the previous data and user state on the screen without reloading it again
        restoreState = true
    }

// TODO 13 extract the navigation action into a private helper, extension function
private fun NavHostController.navigateToSingleAccount(accountType: String) {
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")
}