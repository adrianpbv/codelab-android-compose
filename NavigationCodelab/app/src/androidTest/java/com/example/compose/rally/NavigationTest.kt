import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController

    @Test
    fun rallyNavHost() {
        composeTestRule.setContent {
            // Creates a TestNavHostController
            navController =
                TestNavHostController(LocalContext.current)
            // Sets a ComposeNavigator to the navController so it can navigate through composables
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )
            RallyNavHost(navController = navController)
        }
        fail() // serves as a reminder to finish implementing the test.
    }

    @Before
    fun setupRallyNavHost() {
        composeTestRule.setContent {
            navController =
                TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator()
            )
            RallyNavHost(navController = navController)
        }
    }

    @Test
    fun rallyNavHost_verifyOverviewStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("Overview Screen")
            .assertIsDisplayed()
    }

    @Test
    fun rallyNavHost_clickAllAccount_navigatesToAccounts() {
        composeTestRule
            .onNodeWithContentDescription("All Accounts") // button with contentDescription All Accounts
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("Accounts Screen")
            .assertIsDisplayed()
        // You can follow this pattern to test all of the remaining click navigation actions in the app.
    }

    @Test
    fun rallyNavHost_clickAllBills_navigateToBills() {
        composeTestRule.onNodeWithContentDescription("All Bills")
            .performScrollTo() // Scroll down to perform a click
            .performClick()

        val route = navController.currentBackStackEntry?.destination?.route
        assertEquals(route, "bills")
    }

}

// Note: To check that your RallyNavHost is working correctly, its hierarchy will have
// to be composed first before anything else can be tested.
// This means that your test assertions and verifications will have to be written outside of
// and after the setContent function.

// You also can use the navController to check your assertions by comparing the current string routes to the expected one
// Use navController.currentBackStackEntry?.destination?.route.