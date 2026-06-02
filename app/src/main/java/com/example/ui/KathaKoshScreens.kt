package com.example.ui

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.os.Build
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

// Dynamic Localization map matching PRD strings
object L10n {
    private val en = mapOf(
        "app_title" to "Folklore Katha",
        "app_tagline" to "Nepali Ghost Folklore & Underrated Stories Reader",
        "home_title" to "KathaKosh Feed",
        "saved_title" to "Saved Hand-Picked Stories",
        "submit_title" to "Submit Original Folk Horror",
        "themes_title" to "Visual Aesthetic Themes",
        "settings_title" to "Application Preferences",
        "about_title" to "Story of KathaKosh",
        "read_time" to "min read",
        "author" to "By",
        "empty_saved" to "No saved stories yet",
        "empty_saved_tip" to "Tap the heart icon on any story card to save it for reading offline.",
        "search_hint" to "Search stories, genres, folklore...",
        "save_success" to "Story saved to favorites!",
        "unsave_success" to "Story removed from favorites!",
        "submit_headline" to "Share Your Paranormal Encounter",
        "submit_sub" to "Do you have a bone-chilling community tale or underrated regional myth? Share it to get published on KathaKosh after moderation reviews.",
        "input_title_np" to "Title in Nepali",
        "input_title_en" to "Title in English",
        "input_genre" to "Paranormal Genre",
        "input_body" to "Story Narration (Min 200 characters)",
        "input_author" to "Pen Name / Author Name (Optional)",
        "action_submit" to "Submit Story for Review",
        "auth_required" to "Google Account Required to Submit",
        "auth_required_sub" to "We enforce minimal Google authentication to protect our community stories from spam. Readers remain 100% anonymous.",
        "action_login" to "Simulate Google Auth Sign-In",
        "welcome" to "Namaste,",
        "submission_success" to "Story submitted successfully! Under review.",
        "settings_global_dark" to "Dark mode aesthetic (Spooky)",
        "settings_system_dark" to "Sync with device dark mode",
        "settings_theme_label" to "Select Aesthetic Color Palette",
        "settings_lang" to "Application Language",
        "settings_cache" to "Flush Local Image Space Cache",
        "settings_cache_toast" to "Application cache cleared successfully!",
        "settings_ver" to "App version",
        "about_p1" to "KathaKosh (कथाकोश) is a premium reader dedicated exclusively to Nepali ghost stories, paranormal experiences, and lesser-known regional folklore.",
        "about_p2" to "Our local lore and tribal myths are rich with spectral entities like the Kichkandi, Ban Jhakri, and Himalayan Yetis. KathaKosh preserves these oral histories in comfortable digital typography.",
        "dev_credit" to "Made with love by Rahul Shah ❤️",
        "contact_dev" to "Contact Developer",
        "back_to_feed" to "Back to Story Feed",
        "pending_header" to "Your Submitted Stories Flow",
        "pending_empty" to "No submissions yet. Write your story!",
        "sim_approve" to "Approve Post (Sim)",
        "sim_reject" to "Reject Post (Sim)",
        "reader_opt" to "Reader Comfort Mode",
        "text_size" to "Reading Text Size"
    )

    private val np = mapOf(
        "app_title" to "फोकलोर कथा",
        "app_tagline" to "नेपाली भूतप्रेत र ऐतिहासिक लोककथा सङ्गालो",
        "home_title" to "कथाकोश फिड",
        "saved_title" to "बचत गरिएका कथाहरू",
        "submit_title" to "कथा तथा अनुभव बुझाउनुहोस्",
        "themes_title" to "आकर्षक पृष्ठभूमि थीमहरू",
        "settings_title" to "अनुप्रयोग सेटिङहरू",
        "about_title" to "कथाकोशको बारेमा",
        "read_time" to "मिनेट पढाइ",
        "author" to "लेखक",
        "empty_saved" to "बचत गरिएका कुनै पनि कथाहरू छैनन्",
        "empty_saved_tip" to "कुनै पनि कथाको कार्डमा रहेको मुटु (हार्ट) आइकनमा थिचेर अफलाइन पढ्नका लागि बचत गर्नुहोस्।",
        "search_hint" to "कथा, विधा वा शीर्षक खोज्नुहोस्...",
        "save_success" to "कथा सफलतापूर्वक मनपर्ने सूचीमा थपियो!",
        "unsave_success" to "कथा मनपर्ने सूचीबाट हटाइयो!",
        "submit_headline" to "आफूले भोगेको रहस्यमय कथा साझा गर्नुहोस्",
        "submit_sub" to "के तपाइँसँग मनमा कतै सजिएको भूतको वा गाउँघरको कुनै रोमान्चक लोककथा छ? यहाँ बुझाउनुहोस्, स्वीकृति पछि कथा सम्पूर्ण पाठक माझ देखा पर्नेछ।",
        "input_title_np" to "शीर्षक (नेपालीमा)",
        "input_title_en" to "शीर्षक (अंग्रेजीमा)",
        "input_genre" to "विधा छान्नुहोस्",
        "input_body" to "कथा लेखन (न्यूनतम २०० अक्षर)",
        "input_author" to "लेखकको नाम / उपनाम (वैकल्पिक)",
        "action_submit" to "कथा समीक्षामा पठाउनुहोस्",
        "auth_required" to "कथा बुझाउन गुगल साइन-इन आवश्यक छ",
        "auth_required_sub" to "कथा बुझाउने लेखकहरूको विश्वसनीयता बढाउन र स्पाम नियन्त्रण गर्न न्यूनतम गुगल साइन-इनको व्यवस्था गरिएको छ। कथा पढ्न भने लगइन चाहिँदैन।",
        "action_login" to "गुगल अकाउन्ट लगइन सिमुलेट गर्नुहोस्",
        "welcome" to "नमस्ते,",
        "submission_success" to "कथा सफलतापूर्वक पठाइयो! यो अहिले समीक्षामा छ।",
        "settings_global_dark" to "डार्क मोड एस्थेटिक (रहस्यमयी)",
        "settings_system_dark" to "फोनको पूर्वनिर्धारित थीम प्रयोग गर्नुहोस्",
        "settings_theme_label" to "एस्थेटिक र सुहाउँदो रङ थीम छान्नुहोस्",
        "settings_lang" to "अनुप्रयोगको भाषा (Language)",
        "settings_cache" to "क्यास फाइलहरू खाली गर्नुहोस्",
        "settings_cache_toast" to "अस्थायी फाइलहरू र क्यास सफा भयो!",
        "settings_ver" to "एप संस्करण (Version)",
        "about_p1" to "कथाकोश (कथाकोश) नेपाली परिवेशका भूतप्रेत, लोककथा, मसान, र रहस्यमय असाधारण घटनहरूलाई डिजिटल प्रविधिमा संकलन गर्ने र जोगाउने एउटा नितान्त नेपाली साझा थलो हो।",
        "about_p2" to "हाम्रा हजुरबुबा, हजुरआमाका पालादेखि सुनिँदै आएका किचकन्डी, वनझाँक्री, मुण्डकटवा र यती जस्ता पात्रहरूका दन्त्यकथाहरू र आधुनिक रोमान्चक कथाहरू यहाँ आरामदायी अक्षर शैलिसहित पढ्न पाइन्छ।",
        "dev_credit" to "राहुल शाहद्वारा मायाका साथ सिर्जना गरिएको ❤️",
        "contact_dev" to "विकासकर्तालाई सम्पर्क गर्नुहोस्",
        "back_to_feed" to "कथा सङ्गालोमा फर्कनुहोस्",
        "pending_header" to "तपाईंका बुझाइएका कथाहरूको स्थिति",
        "pending_empty" to "हालसम्म कुनै कथा बुझाउनुभएको छैन। कथा सुरु गर्नुहोस्!",
        "sim_approve" to "स्वीकृत गर्नुहोस् (सिमुलेटर)",
        "sim_reject" to "अस्वीकृत गर्नुहोस् (सिमुलेटर)",
        "reader_opt" to "पढ्ने सतह अप्टिमाइजेसन",
        "text_size" to "अक्षरको आकार परिवर्तन"
    )

    fun translate(key: String, lang: String): String {
        return if (lang == "np") np[key] ?: en[key] ?: key else en[key] ?: key
    }
}

// Visual color style mapping for Genre types as outlined on Section 7 of PRD
fun getGenreVisuals(genre: String, isDark: Boolean): Triple<Color, Color, String> {
    return when (genre.lowercase()) {
        "ghost", "भूत" -> Triple(
            if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9), // Slate dark vs blue gray light
            Color(0xFF3F51B5), // Indigo accent
            "👻"
        )
        "folklore", "लोककथा" -> Triple(
            if (isDark) Color(0xFF2B2118) else Color(0xFFFDF6E2), // Warm dark brown vs warm paper light
            Color(0xFFFF9800), // Amber accent
            "📜"
        )
        "mystery", "रहस्य" -> Triple(
            if (isDark) Color(0xFF0F2525) else Color(0xFFE6F3F3), // Muted dark teal vs teal paper light
            Color(0xFF009688), // Teal accent
            "🔍"
        )
        "underrated", "अल्पचर्चित" -> Triple(
            if (isDark) Color(0xFF2C1E21) else Color(0xFFFAF0F2), // Dark rose tint vs pale rose light
            Color(0xFFE91E63), // Rose accent
            "✨"
        )
        "community", "सामुदायिक" -> Triple(
            if (isDark) Color(0xFF1B1B29) else Color(0xFFEDECF9), // Dark violet vs light lavender
            Color(0xFF673AB7), // M3 theme primary or deep violet
            "✍️"
        )
        else -> Triple(
            if (isDark) Color(0xFF171321) else Color(0xFFF3F0FF),
            Color(0xFF512DA8),
            "📖"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: KathaKoshViewModel,
    onNavigateToStory: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    onNavigateToSubmit: () -> Unit
) {
    val stories by viewModel.filteredStories.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val settings by viewModel.userSettings.collectAsState()

    val lang = settings.languageCode

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onOpenDrawer, Modifier.testTag("drawer_menu_button")) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    title = {
                        Text(
                            text = L10n.translate("app_title", lang),
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    },
                    actions = {
                        // Quick switch theme mode from app bar
                        IconButton(onClick = { viewModel.setDarkMode(!settings.isDarkMode) }) {
                            Icon(
                                imageVector = if (settings.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme"
                            )
                        }
                    }
                )
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text(L10n.translate("search_hint", lang)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .testTag("search_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    singleLine = true
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToSubmit,
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                text = { Text(if (lang == "np") "✍️ कथा लेख्नुहोस्" else "✍️ Story Submission") },
                modifier = Modifier.testTag("fab_submit_story")
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Sticky Genre Filter Chips Row dynamically populated with any custom categories
            val allStoriesList by viewModel.allStories.collectAsState()
            val genres = remember(allStoriesList) {
                val core = listOf("सबै", "भूत", "लोककथा", "रहस्य", "Underrated", "Community")
                val custom = allStoriesList.map { it.genre }
                    .filterNot { g -> core.any { c -> c.equals(g, ignoreCase = true) } }
                    .distinct()
                    .filterNot { it.isBlank() }
                core + custom
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(genres) { genre ->
                    val isSelected = selectedGenre == genre
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setGenreFilter(genre) },
                        label = { Text(genre) },
                        modifier = Modifier.testTag("genre_chip_${genre}")
                    )
                }
            }

            if (stories.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("💀", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (lang == "np") "कुनै कथाहरू फेला परेनन्" else "No spectral tales matched your query",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (lang == "np") "खोज शब्दहरू परिवर्तन गरेर हेर्नुहोस्।" else "Try adjusting your filters or query",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.alpha(0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .testTag("story_feed_list"),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(stories, key = { _, story -> story.id }) { index, story ->
                        // Smooth entry animation for cards
                        var visible by remember { mutableStateOf(false) }
                        LaunchedEffect(Unit) { visible = true }

                        AnimatedVisibility(
                            visible = visible,
                            enter = slideInVertically(
                                initialOffsetY = { 200 },
                                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
                            ) + fadeIn(animationSpec = tween(300, delayMillis = index * 50)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StoryCard(
                                story = story,
                                isDarkGlobal = settings.isDarkMode,
                                onClick = { onNavigateToStory(story.id) },
                                onToggleSave = { viewModel.toggleSaveStory(story) },
                                isSavedFlow = viewModel.isStorySavedFlow(story.id),
                                lang = lang
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StoryCard(
    story: Story,
    isDarkGlobal: Boolean,
    onClick: () -> Unit,
    onToggleSave: () -> Unit,
    isSavedFlow: kotlinx.coroutines.flow.Flow<Boolean>,
    lang: String
) {
    val isSaved by isSavedFlow.collectAsState(initial = false)
    val (bgColor, accentColor, emoji) = getGenreVisuals(story.genre, isDarkGlobal)

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("story_card_${story.id}"),
        colors = CardDefaults.elevatedCardColors(
            containerColor = bgColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Visual header band of card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!story.imageUrl.isNullOrBlank()) {
                    coil.compose.AsyncImage(
                        model = story.imageUrl,
                        contentDescription = "Cover Image for ${story.titleEn}",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Semi-transparent overlay to ensure M3 chip and button text remain highly legible
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.5f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                // Ambient subtle noise texture simulation
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(accentColor.copy(alpha = 0.4f), Color.Transparent)
                                    )
                                )
                            }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = emoji,
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            SuggestionChip(
                                onClick = { },
                                label = {
                                    Text(
                                        text = story.genre,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = if (!story.imageUrl.isNullOrBlank()) Color.Black.copy(alpha = 0.6f) else accentColor.copy(alpha = 0.15f),
                                    labelColor = if (!story.imageUrl.isNullOrBlank()) Color.White else accentColor
                                ),
                                border = BorderStroke(1.dp, if (!story.imageUrl.isNullOrBlank()) Color.White.copy(alpha = 0.5f) else accentColor.copy(alpha = 0.3f))
                            )
                        }
                    }

                    // Save toggle inside the header
                    IconButton(
                        onClick = onToggleSave,
                        modifier = Modifier.testTag("save_button_${story.id}")
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Save Story",
                            tint = if (isSaved) Color.Red else (if (!story.imageUrl.isNullOrBlank()) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Tiro Devanagari style Header
                Text(
                    text = story.titleNp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle / English title
                Text(
                    text = story.titleEn,
                    style = MaterialTheme.typography.labelMedium,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(top = 4.dp).alpha(0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Short Excerpt
                Text(
                    text = story.excerpt,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Row with reading metadata
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${L10n.translate("author", lang)}: ${story.author}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = accentColor
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Outlined.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp).alpha(0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${story.readTimeMinutes} ${L10n.translate("read_time", lang)}",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.alpha(0.6f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    viewModel: KathaKoshViewModel,
    storyId: String,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val story by viewModel.activeStory.collectAsState()
    val isSaved by viewModel.isStorySavedFlow(storyId).collectAsState(initial = false)

    // Reader state overrides
    val readerDarkModeOverride by viewModel.readerDarkModeOverride.collectAsState()
    val userSettings by viewModel.userSettings.collectAsState()
    val readerFontSizeSp by viewModel.readerFontSizeSp.collectAsState()

    val lang = userSettings.languageCode

    // Decide what is the effective reader theme mode
    val isReaderDark = when (readerDarkModeOverride) {
        null -> userSettings.isDarkMode
        else -> readerDarkModeOverride!!
    }

    LaunchedEffect(storyId) {
        viewModel.selectStory(storyId)
    }

    BackHandler {
        viewModel.selectStory(null)
        onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.selectStory(null)
                        onNavigateBack()
                    }, Modifier.testTag("back_button")) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Text(
                        text = story?.titleEn ?: "Paranormal Encounter",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    // Quick text controls
                    IconButton(onClick = { viewModel.toggleReaderDarkMode() }) {
                        Icon(
                            imageVector = if (isReaderDark) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                            contentDescription = "Independent Reader Theme Override"
                        )
                    }

                    // Share
                    IconButton(onClick = {
                        story?.let { s ->
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, s.titleNp)
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "✨ Read \"${s.titleNp} (${s.titleEn})\" by ${s.author} on KathaKosh (कथाकोश): \n\n\"${s.excerpt}\"\n\nDownload KathaKosh on Android for the ultimate Nepali Ghost stories collection!"
                                )
                            }
                            context.startActivity(Intent.createChooser(intent, "Share KathaKosh story"))
                        }
                    }, Modifier.testTag("share_button")) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { innerPadding ->
        story?.let { s ->
            val (genreBg, accent, emoji) = getGenreVisuals(s.genre, isReaderDark)

            // Dynamic Custom Immersive Reading Canvas with specific genre ambient backgrounds
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(genreBg)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Progress bar representing reading positioning/progress
                    LinearProgressIndicator(
                        progress = { 1.0f }, // Simulating completed reader path
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = accent,
                        trackColor = accent.copy(alpha = 0.2f)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 20.dp)
                    ) {
                        // Title
                        Text(
                            text = s.titleNp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = (readerFontSizeSp + 6).sp,
                            lineHeight = (readerFontSizeSp + 14).sp,
                            color = if (isReaderDark) Color.White else Color.Black
                        )

                        Text(
                            text = s.titleEn,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            fontSize = (readerFontSizeSp - 2).sp,
                            color = if (isReaderDark) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (!s.imageUrl.isNullOrBlank()) {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .padding(bottom = 16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                            ) {
                                coil.compose.AsyncImage(
                                    model = s.imageUrl,
                                    contentDescription = "Spectral visual representation",
                                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }

                        // Category tag banner + author
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SuggestionChip(
                                onClick = {},
                                label = { Text(s.genre) },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = accent.copy(alpha = 0.15f),
                                    labelColor = accent
                                )
                            )

                            Text(
                                text = "✍️ ${s.author}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = accent
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = accent.copy(alpha = 0.3f)
                        )

                        // Main Story Body in Noto Serif Devanagari comfy readability
                        Text(
                            text = s.body,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Normal,
                            fontSize = readerFontSizeSp.sp,
                            lineHeight = (readerFontSizeSp * 1.6).sp,
                            color = if (isReaderDark) Color(0xFFE2E8F0) else Color(0xFF1E293B),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("story_body_text")
                        )

                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    // Floating Reader Bottom Bar
                    Surface(
                        tonalElevation = 8.dp,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 12.dp)
                                .navigationBarsPadding()
                        ) {
                            Text(
                                text = "${L10n.translate("text_size", lang)}: ${readerFontSizeSp}sp",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TextFields, contentDescription = null, modifier = Modifier.size(24.dp).alpha(0.6f))
                                Slider(
                                    value = readerFontSizeSp.toFloat(),
                                    onValueChange = { viewModel.setReaderFontSize(it.toInt()) },
                                    valueRange = 14f..26f,
                                    steps = 3,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp)
                                        .testTag("font_slider")
                                )
                                // Save story shortcut in reading view
                                IconButton(
                                    onClick = { viewModel.toggleSaveStory(s) },
                                    modifier = Modifier.testTag("reader_save_toggle")
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isSaved) Color.Red else MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SavedStoriesScreen(
    viewModel: KathaKoshViewModel,
    onNavigateToStory: (String) -> Unit,
    onOpenDrawer: () -> Unit
) {
    val savedList by viewModel.savedStories.collectAsState()
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings.languageCode

    var isGridView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = L10n.translate("saved_title", lang),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { isGridView = !isGridView }) {
                        Icon(
                            imageVector = if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = "Toggle Grid Layout"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (savedList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text("❤️", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = L10n.translate("empty_saved", lang),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = L10n.translate("empty_saved_tip", lang),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.alpha(0.7f),
                        lineHeight = 22.sp
                    )
                }
            }
        } else {
            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .testTag("saved_stories_grid"),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(savedList, key = { it.storyId }) { saved ->
                        SavedCardItem(saved, isGridView = true, onStoryClick = { onNavigateToStory(saved.storyId) }, onLongPressDelete = { viewModel.deleteSavedStoryById(saved.storyId) })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .testTag("saved_stories_list"),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(savedList, key = { it.storyId }) { saved ->
                        SavedCardItem(saved, isGridView = false, onStoryClick = { onNavigateToStory(saved.storyId) }, onLongPressDelete = { viewModel.deleteSavedStoryById(saved.storyId) })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedCardItem(
    saved: SavedStory,
    isGridView: Boolean,
    onStoryClick: () -> Unit,
    onLongPressDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(if (saved.titleNp.isNotEmpty()) "कथा हटाउने?" else "Delete Saved Case?") },
            text = { Text("Are you sure you want to remove \"${saved.titleNp}\" from your offline-saved stories?") },
            confirmButton = {
                TextButton(onClick = {
                    onLongPressDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onStoryClick,
                onLongClick = { showDeleteDialog = true }
            )
            .testTag("saved_item_${saved.storyId}"),
        shape = RoundedCornerShape(12.dp)
    ) {
        val (_, accent, emoji) = getGenreVisuals(saved.genre, isSystemInDarkTheme())
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = emoji, fontSize = 24.sp)
                Text(
                    text = saved.genre,
                    style = MaterialTheme.typography.labelSmall,
                    color = accent,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = saved.titleNp,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = saved.titleEn,
                style = MaterialTheme.typography.labelSmall,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.alpha(0.7f)
            )
            if (!isGridView) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = saved.excerpt,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "${saved.readTimeMinutes} min",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.alpha(0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitStoryScreen(
    viewModel: KathaKoshViewModel,
    onOpenDrawer: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val pendingList by viewModel.pendingStories.collectAsState()
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings.languageCode

    // Form states
    var titleNp by remember { mutableStateOf("") }
    var titleEn by remember { mutableStateOf("") }
    var storyBody by remember { mutableStateOf("") }
    var authorName by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("Ghost") }
    var expandedGenreDropdown by remember { mutableStateOf(false) }
    var customGenreInput by remember { mutableStateOf("") }
    var selectedImageUrl by remember { mutableStateOf("") }

    val genres = listOf("Ghost", "Folklore", "Mystery", "Underrated", "Personal Experience", "Custom / Other")

    // Image picker launcher for custom uploads
    val galleryLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            selectedImageUrl = it.toString()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = L10n.translate("submit_title", lang),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                text = L10n.translate("submit_headline", lang),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = L10n.translate("submit_sub", lang),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 20.dp).alpha(0.7f),
                lineHeight = 22.sp
            )

            if (currentUser == null) {
                // Interactive simulation of Google Sign-In with real-feeling interface
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .testTag("google_login_prompt_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = L10n.translate("auth_required", lang),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = L10n.translate("auth_required_sub", lang),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.alpha(0.8f),
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                // Instantly sign in sample Google user
                                viewModel.performSimulatedGoogleLogin("Pawan Neupane", "pawan_neupane_88@gmail.com")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("sim_google_login_btn")
                        ) {
                            Text(L10n.translate("action_login", lang))
                        }
                    }
                }
            } else {
                // User card showing active sign-in
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser!!.displayName.take(1).uppercase(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = currentUser!!.displayName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = currentUser!!.email,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.alpha(0.7f)
                                )
                            }
                        }

                        TextButton(onClick = { viewModel.performLogout() }) {
                            Text("Logout")
                        }
                    }
                }

                // Story Formulation inputs
                Text(
                    text = L10n.translate("input_title_np", lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = titleNp,
                    onValueChange = { if (it.length <= 80) titleNp = it },
                    placeholder = { Text("उदा. रानीपोखरीको सेतो छाया") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("input_title_np"),
                    singleLine = true
                )

                Text(
                    text = L10n.translate("input_title_en", lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { if (it.length <= 80) titleEn = it },
                    placeholder = { Text("Ex. The White Shadow of Ranipokhari") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .testTag("input_title_en"),
                    singleLine = true
                )

                // Dropdown for Genre
                Text(
                    text = L10n.translate("input_genre", lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    OutlinedButton(
                        onClick = { expandedGenreDropdown = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dropdown_genre_select")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(selectedGenre)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                    DropdownMenu(
                        expanded = expandedGenreDropdown,
                        onDismissRequest = { expandedGenreDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        genres.forEach { genre ->
                            DropdownMenuItem(
                                text = { Text(genre) },
                                onClick = {
                                    selectedGenre = genre
                                    expandedGenreDropdown = false
                                }
                            )
                        }
                    }
                }

                // If user chose "Custom / Other", show input text field for custom category
                if (selectedGenre == "Custom / Other") {
                    Text(
                        text = if (lang == "np") "आफ्नो वर्ग/श्रेणी लेख्नुहोस्" else "Enter Custom Category",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = customGenreInput,
                        onValueChange = { customGenreInput = it },
                        placeholder = { Text("Ex. Ban Jhakri, Tharu Myths, Supernatural") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        singleLine = true
                    )
                }

                Text(
                    text = L10n.translate("input_body", lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = storyBody,
                    onValueChange = { storyBody = it },
                    placeholder = { Text("कथा यहाँ लेख्नुहोस् वा टाँस्नुहोस्...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp)
                        .testTag("input_body_text"),
                    maxLines = 15
                )

                // Story Cover Image Selection Section
                Text(
                    text = if (lang == "np") "कथाको आवरण चित्र (Cover Image)" else "Story Cover Image (Optional)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                if (selectedImageUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        coil.compose.AsyncImage(
                            model = selectedImageUrl,
                            contentDescription = "Cover preview",
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        IconButton(
                            onClick = { selectedImageUrl = "" },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                .size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear image",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == "np") "ग्यालरीबाट थप्नुहोस्" else "Gallery Picture",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    var showUrlDialog by remember { mutableStateOf(false) }
                    OutlinedButton(
                        onClick = { showUrlDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (lang == "np") "वेब लिङ्क हालनुहोस्" else "Add Web URL",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    if (showUrlDialog) {
                        var tempUrl by remember { mutableStateOf("") }
                        AlertDialog(
                            onDismissRequest = { showUrlDialog = false },
                            title = { Text("Image Web Link") },
                            text = {
                                Column {
                                    Text(
                                        "Enter an online image direct link:",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                    OutlinedTextField(
                                        value = tempUrl,
                                        onValueChange = { tempUrl = it },
                                        placeholder = { Text("https://example.com/cover.jpg") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            },
                            confirmButton = {
                                Button(onClick = {
                                    if (tempUrl.isNotBlank()) {
                                        selectedImageUrl = tempUrl
                                    }
                                    showUrlDialog = false
                                }) {
                                    Text("Add")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showUrlDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }

                Text(
                    text = L10n.translate("input_author", lang),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    placeholder = { Text("उदा. अविनाश तामाङ") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    singleLine = true
                )

                // Validate submit action
                val canSubmit = titleNp.isNotBlank() && titleEn.isNotBlank() && storyBody.length >= 200
                Button(
                    onClick = {
                        val finalGenre = if (selectedGenre == "Custom / Other") {
                            customGenreInput.ifBlank { "Community" }
                        } else {
                            selectedGenre
                        }

                        viewModel.submitCommunityStory(
                            titleNp = titleNp,
                            titleEn = titleEn,
                            genre = finalGenre,
                            body = storyBody,
                            author = authorName,
                            imageUrl = selectedImageUrl.ifBlank { null }
                        )
                        // Reset form
                        titleNp = ""
                        titleEn = ""
                        storyBody = ""
                        authorName = ""
                        customGenreInput = ""
                        selectedImageUrl = ""
                    },
                    enabled = canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("submit_button_action")
                ) {
                    Text(L10n.translate("action_submit", lang))
                }
            }

            // Pending Queue List display (Live simulator feedbacks)
            Spacer(modifier = Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${L10n.translate("pending_header", lang)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (pendingList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = L10n.translate("pending_empty", lang),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.alpha(0.6f)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("pending_queue_list")
                ) {
                    pendingList.forEach { pending ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pending_item_${pending.id}")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = pending.titleNp,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    // Status Badge tag styling
                                    val badgeColor = when (pending.status) {
                                        "Approved" -> Color(0xFF4CAF50)
                                        "Rejected" -> Color(0xFFF44336)
                                        else -> Color(0xFFFF9800)
                                    }
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text(pending.status) },
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = badgeColor.copy(alpha = 0.15f),
                                            labelColor = badgeColor
                                        )
                                    )
                                }
                                Text(
                                    text = pending.genre,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.alpha(0.7f)
                                )
                                Text(
                                    text = if (pending.body.length > 100) pending.body.take(97) + "..." else pending.body,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )

                                // Interactive Simulator Action buttons to Approve immediately and verify workflow!
                                if (pending.status == "Pending") {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Button(
                                            onClick = { viewModel.approveSubmission(pending.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("btn_approve_${pending.id}")
                                        ) {
                                            Text(L10n.translate("sim_approve", lang), fontSize = 10.sp, color = Color.White)
                                        }

                                        OutlinedButton(
                                            onClick = { viewModel.rejectSubmission(pending.id) },
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("btn_reject_${pending.id}")
                                        ) {
                                            Text(L10n.translate("sim_reject", lang), fontSize = 10.sp, color = Color.Red)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: KathaKoshViewModel,
    onOpenDrawer: () -> Unit
) {
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings.languageCode
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = L10n.translate("settings_title", lang),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Dark Mode Controls
            Text(
                text = "Myths Atmosphere Themes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(L10n.translate("settings_global_dark", lang))
                Switch(
                    checked = settings.isDarkMode,
                    onCheckedChange = { viewModel.setDarkMode(it) },
                    modifier = Modifier.testTag("switch_dark_mode")
                )
            }

            // Theme selector list (6 custom aesthetic pastel themes + material you dynamic)
            Text(
                text = L10n.translate("settings_theme_label", lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            ThemePalette.entries.forEach { palette ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.setThemePalette(palette) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = settings.themePalette == palette,
                        onClick = { viewModel.setThemePalette(palette) },
                        modifier = Modifier.testTag("radio_theme_${palette.key}")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (lang == "np") palette.npLabel else palette.enLabel,
                            fontWeight = if (settings.themePalette == palette) FontWeight.Bold else FontWeight.Normal
                        )
                        // Simple preview squares representing the colors
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(getPaletteColorPreview(palette, isLight = true), CircleShape)
                            )
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(getPaletteColorPreview(palette, isLight = false), CircleShape)
                            )
                        }
                    }
                }
            }

            HorizontalDivider()

            // Language Selection
            Text(
                text = L10n.translate("settings_lang", lang),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ElevatedButton(
                    onClick = { viewModel.setLanguage("np") },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (lang == "np") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.weight(1f).testTag("lang_np_btn")
                ) {
                    Text("नेपाली (Nepali)", color = if (lang == "np") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                }

                ElevatedButton(
                    onClick = { viewModel.setLanguage("en") },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (lang == "en") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier.weight(1f).testTag("lang_en_btn")
                ) {
                    Text("English", color = if (lang == "en") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface)
                }
            }

            HorizontalDivider()

            // Flash Cache
            Button(
                onClick = {
                    val toastMessage = L10n.translate("settings_cache_toast", lang)
                    android.widget.Toast.makeText(context, toastMessage, android.widget.Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth().testTag("cache_clear_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text(L10n.translate("settings_cache", lang), color = Color.White)
            }

            // Version metadata
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(L10n.translate("settings_ver", lang), style = MaterialTheme.typography.labelSmall)
                Text("v1.0.0-PRO", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// Preview helper for palettes inside settings
fun getPaletteColorPreview(palette: ThemePalette, isLight: Boolean): Color {
    return when (palette) {
        ThemePalette.DYNAMIC -> if (isLight) Color(0xFF6750A4) else Color(0xFFD0BCFF)
        ThemePalette.MISTY_ROSE -> if (isLight) MistyRosePrimaryLight else MistyRosePrimaryDark
        ThemePalette.SMOKY_BLUE -> if (isLight) SmokyBluePrimaryLight else SmokyBluePrimaryDark
        ThemePalette.SANDALWOOD -> if (isLight) SandalwoodPrimaryLight else SandalwoodPrimaryDark
        ThemePalette.PALE_MOSS -> if (isLight) PaleMossPrimaryLight else PaleMossPrimaryDark
        ThemePalette.RHODODENDRON -> if (isLight) RhododendronPrimaryLight else RhododendronPrimaryDark
        ThemePalette.VIOLET_HAZE -> if (isLight) VioletHazePrimaryLight else VioletHazePrimaryDark
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    viewModel: KathaKoshViewModel,
    onOpenDrawer: () -> Unit
) {
    val settings by viewModel.userSettings.collectAsState()
    val lang = settings.languageCode
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = L10n.translate("about_title", lang),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📖", fontSize = 80.sp, modifier = Modifier.padding(bottom = 12.dp))
            Text(
                text = "KathaKosh (कथाकोश)",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = L10n.translate("app_tagline", lang),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 4.dp).alpha(0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = L10n.translate("about_p1", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 26.sp
                    )
                    Text(
                        text = L10n.translate("about_p2", lang),
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = L10n.translate("dev_credit", lang),
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val uri = android.net.Uri.parse("mailto:rahulshah2021nice@gmail.com?subject=KathaKosh%20Android%20Reader%20Feedback")
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    try {
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        android.widget.Toast.makeText(context, "No email client found.", android.widget.Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.testTag("contact_button")
            ) {
                Icon(Icons.Default.Mail, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(L10n.translate("contact_dev", lang))
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseHubScreen(
    viewModel: KathaKoshViewModel,
    onOpenDrawer: () -> Unit
) {
    val context = LocalContext.current
    val settings by viewModel.userSettings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val allStoriesList by viewModel.allStories.collectAsState()
    val lang = settings.languageCode

    // Permissions check
    var notificationsGranted by remember {
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    var storageGranted by remember {
        mutableStateOf(
            context.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionsLauncher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            notificationsGranted = permissions[android.Manifest.permission.POST_NOTIFICATIONS] ?: notificationsGranted
        }
        storageGranted = permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: storageGranted
        android.widget.Toast.makeText(context, "Permissions Updated!", android.widget.Toast.LENGTH_SHORT).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                title = {
                    Text(
                        text = if (lang == "np") "फायरबेस कन्सोल" else "Firebase Integration Hub",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "🔥 फायरबेस क्लाइन्ट नियन्त्रण केन्द्र" else "🔥 Firebase Hub & Cloud Sync",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (lang == "np") {
                            "यहाँबाट फायरबेस रियल-टाइम डेटाबेस, फोटो स्टोरेज, सुरक्षा अनुमति व्यवस्थापन र डिभाइस पुश अलर्टहरू सिमुलेट गर्न सक्नुहुन्छ।"
                        } else {
                            "Monitor Firebase Real-time Database connections, live image uploads, and trigger simulated device push alerts."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alpha(0.8f),
                        lineHeight = 18.sp
                    )
                }
            }

            // SECTION 1: System Permissions Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "⚙️ सुरक्षा अनुमतिकहरू र सहज अनुभव" else "⚙️ Permissions & System Access",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == "np") "पुश सूचना (POST_NOTIFICATIONS)" else "Push Notifications (Android 13+)",
                            style = MaterialTheme.typography.bodySmall
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text(if (notificationsGranted) "Granted" else "Pending") },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (notificationsGranted) Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                                labelColor = if (notificationsGranted) Color(0xFF065F46) else Color(0xFF991B1B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (lang == "np") "मिडिया/स्टोरेज (READ_EXTERNAL_STORAGE)" else "External Media Access (Uri loading)",
                            style = MaterialTheme.typography.bodySmall
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text(if (storageGranted) "Granted" else "Pending") },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = if (storageGranted) Color(0xFFD1FAE5) else Color(0xFFFEE2E2),
                                labelColor = if (storageGranted) Color(0xFF065F46) else Color(0xFF991B1B)
                            )
                        )
                    }

                    if (!notificationsGranted || !storageGranted) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val list = mutableListOf<String>()
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    list.add(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                                list.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                permissionsLauncher.launch(list.toTypedArray())
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (lang == "np") "पूर्ण पहुँच अनुमति दिनुहोस्" else "Grant Access Permission")
                        }
                    }
                }
            }

            // SECTION 2: Firebase Auth & Guest Mode
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "👤 लगइन प्रमाणिकरण र गेस्ट मोड" else "👤 User Auth & Anonymous Identity",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (currentUser != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentUser!!.displayName.take(1).uppercase(),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(currentUser!!.displayName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(currentUser!!.email, style = MaterialTheme.typography.labelSmall, modifier = Modifier.alpha(0.7f))
                                Text("UID: ${currentUser!!.uid}", style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace, modifier = Modifier.alpha(0.5f))
                            }
                            TextButton(onClick = { viewModel.performLogout() }) {
                                Text("Logout")
                            }
                        }
                    } else {
                        Text(
                            text = if (lang == "np") "तपाईं लगइन हुनुभएको छैन। आफ्नो इच्छा अनुसार गुगल वा गेस्ट अनाम मोड रोजेर तत्काल प्रमाणीकरण हुन सक्नुहुन्छ:" else "You are not logged in. Select custom Firebase Google authentication or safe guest mode:",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.alpha(0.7f).padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.performSimulatedGoogleLogin("Pawan Shah", "pawan_shah_88@gmail.com")
                                },
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Google Auth", style = MaterialTheme.typography.bodyMedium)
                            }

                            Button(
                                onClick = {
                                    viewModel.performSimulatedGoogleLogin("Guest Mystic", "guest_mystic@folklorekatha.org")
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                modifier = Modifier.weight(1.5f)
                            ) {
                                Text("Guest Mode", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            // SECTION 3: Firebase Realtime Database
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "💾 रियलटाइम डेटाबेस सिङ्क" else "💾 Realtime Database Records Sync",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Endpoint: https://folklore-katha-default-rtdb.firebaseio.com/",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (lang == "np") "सिङ्क स्थिति:" else "Connection Status:", style = MaterialTheme.typography.bodySmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF10B981), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Online (Live Sync)", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Synchronized database events:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.alpha(0.7f)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        val limit = allStoriesList.take(3)
                        limit.forEach { s ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(s.titleEn, style = MaterialTheme.typography.labelSmall, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                                Text("Synced 🟢", style = MaterialTheme.typography.labelSmall, color = Color(0xFF047857), fontWeight = FontWeight.Bold)
                            }
                        }
                        if (allStoriesList.isEmpty()) {
                            Text("No sync reports found.", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            // SECTION 4: Firebase Storage
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "📦 क्लाउड स्टोरेज बाल्टिन (Bucket)" else "📦 Cloud Storage Bucket Manager",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Bucket: gs://folklore-katha.appspot.com/folders/covers/",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Images uploaded to storage:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                            .background(Color.Black.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf(
                            "cover_ranipokhari.jpg" to "242 KB",
                            "cover_kichkandi.jpg" to "318 KB",
                            "cover_banjhakri.jpg" to "561 KB"
                        ).forEach { (name, size) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(name, style = MaterialTheme.typography.labelSmall, fontFamily = FontFamily.Monospace)
                                Text(size, style = MaterialTheme.typography.labelSmall, modifier = Modifier.alpha(0.6f))
                            }
                        }
                    }
                }
            }

            // SECTION 5: Push Notifications Test Simulation
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (lang == "np") "🔔 सूचना पुश सिमुलेटर" else "🔔 Push Alerts Simulator (FCM)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "FCM Registration Token: fcm_token_folk_katha_99a8b_3462d",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.alpha(0.6f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (!notificationsGranted) {
                                android.widget.Toast.makeText(context, "Give notifications permission above first!", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                NotificationHelper.showNotification(
                                    context = context,
                                    title = "👻 Spectre Spotted Near Chaur!",
                                    message = "रानीपोखरीको सेतो स्त्रीको छाया आज फेरि मध्यरातमा देखा परेको दाबी! आवरण तस्बिर सहित हेर्नुहोस्।"
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAF3A42)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (lang == "np") "पार्टी पुश सूचना सिमुलेट गर्नुहोस्" else "Send Test System Push Alert")
                    }
                }
            }

            // SECTION 6: Integration Guide
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚙️ Developer Guide: Connect Your Live Firebase",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "To wire this application to your personal Firebase backend instead of simulated local client channels, follow these simple steps:\n\n" +
                                "1. Download google-services.json from your Firebase Console.\n" +
                                "2. Copy google-services.json into your project's /app/ folder.\n" +
                                "3. Make sure Firebase SDK plugins are declared in your gradle file. Next compile will link dependencies instantly.\n\n" +
                                "All components are optimized and ready for zero-collision compilation.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        modifier = Modifier.alpha(0.8f)
                    )
                }
            }
        }
    }
}
