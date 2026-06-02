package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.data.ThemePalette
import com.example.ui.*
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KathaKoshAppShell()
        }
    }
}

// Defining simple direct drawer navigation items
data class DrawerItem(
    val titleNp: String,
    val titleEn: String,
    val route: String,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector,
    val tag: String
)

@Composable
fun KathaKoshAppShell() {
    val viewModel: KathaKoshViewModel = viewModel()
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings.languageCode

    MyApplicationTheme(
        selectedPalette = settings.themePalette,
        isDarkMode = settings.isDarkMode
    ) {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        val drawerItems = listOf(
            DrawerItem("🏠 गृह फिड", "🏠 Home Feed", "feed", Icons.Filled.Home, Icons.Outlined.Home, "drawer_home"),
            DrawerItem("🔖 बचत गरिएका कथाहरू", "🔖 Saved Stories", "saved", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "drawer_saved"),
            DrawerItem("✍️ नयाँ कथा दर्ता", "✍️ Submit Your Story", "submit", Icons.Filled.Edit, Icons.Outlined.Edit, "drawer_submit"),
            DrawerItem("🔥 फायरबेस नियन्त्रण", "🔥 Firebase Hub", "firebase", Icons.Filled.Cloud, Icons.Outlined.Cloud, "drawer_firebase"),
            DrawerItem("⚙️ अनुप्रयोग सेटिङ", "⚙️ Settings", "settings", Icons.Filled.Settings, Icons.Outlined.Settings, "drawer_settings"),
            DrawerItem("ℹ️ हाम्रो बारेमा", "ℹ️ About", "about", Icons.Filled.Info, Icons.Outlined.Info, "drawer_about")
        )

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route ?: "feed"

        ModalNavigationDrawer(
            drawerState = drawerState,
            modifier = Modifier.testTag("app_navigation_drawer"),
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier
                        .width(310.dp)
                        .fillMaxHeight(),
                    drawerContainerColor = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 32.dp, horizontal = 20.dp)
                    ) {
                        // Title/Logo Area containing tiro devanagari logo
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📖",
                                    fontSize = 44.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Column {
                                    Text(
                                        text = L10n.translate("app_title", lang),
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = if (lang == "np") "ग्रामीण लोककथा सङ्गालो" else "Spectral Lore Reader",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.alpha(0.7f),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = L10n.translate("app_tagline", lang),
                                style = MaterialTheme.typography.bodySmall,
                                fontStyle = FontStyle.Italic,
                                modifier = Modifier.alpha(0.6f),
                                lineHeight = 16.sp
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))

                        // Navigation list items
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            drawerItems.forEach { item ->
                                val isSelected = currentRoute == item.route
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) item.iconFilled else item.iconOutlined,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = if (lang == "np") item.titleNp else item.titleEn,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 14.sp
                                        )
                                    },
                                    selected = isSelected,
                                    onClick = {
                                        coroutineScope.launch { drawerState.close() }
                                        if (currentRoute != item.route) {
                                            navController.navigate(item.route) {
                                                popUpTo("feed") { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .testTag(item.tag)
                                        .height(48.dp)
                                )
                            }
                        }

                        // Bottom developer credit as requested in the PRD (Section 3.4)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = L10n.translate("dev_credit", lang),
                                style = MaterialTheme.typography.labelSmall,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = "feed",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("feed") {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToStory = { storyId ->
                            navController.navigate("reader/$storyId")
                        },
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onNavigateToSubmit = {
                            navController.navigate("submit")
                        }
                    )
                }

                composable(
                    route = "reader/{storyId}",
                    arguments = listOf(navArgument("storyId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("storyId") ?: ""
                    StoryDetailScreen(
                        viewModel = viewModel,
                        storyId = id,
                        onNavigateBack = {
                            navController.popBackStack()
                        }
                    )
                }

                composable("saved") {
                    SavedStoriesScreen(
                        viewModel = viewModel,
                        onNavigateToStory = { storyId ->
                            navController.navigate("reader/$storyId")
                        },
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                composable("submit") {
                    SubmitStoryScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                composable("firebase") {
                    FirebaseHubScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                composable("settings") {
                    SettingsScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }

                composable("about") {
                    AboutScreen(
                        viewModel = viewModel,
                        onOpenDrawer = {
                            coroutineScope.launch { drawerState.open() }
                        }
                    )
                }
            }
        }
    }
}
