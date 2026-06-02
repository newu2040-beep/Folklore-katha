package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class KathaKoshViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val savedStoryDao = database.savedStoryDao()
    private val preferencesManager = UserPreferencesManager(application)
    
    val repository = StoriesRepository(application, savedStoryDao)

    // User preferences (persisted in DataStore)
    val userSettings: StateFlow<UserSettings> = preferencesManager.userSettingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings(
                themePalette = ThemePalette.SANDALWOOD,
                isDarkMode = false,
                useSystemDarkMode = false,
                fontSizeSp = 18,
                languageCode = "np"
            )
        )

    // Reader Immersive Overrides (stored during screen lifecycle/session)
    private val _readerDarkModeOverride = MutableStateFlow<Boolean?>(null) // null means use global theme
    val readerDarkModeOverride: StateFlow<Boolean?> = _readerDarkModeOverride.asStateFlow()

    private val _readerFontSizeSp = MutableStateFlow(18)
    val readerFontSizeSp: StateFlow<Int> = _readerFontSizeSp.asStateFlow()

    // Screen State variables
    private val _selectedGenre = MutableStateFlow("सबै")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentStoryId = MutableStateFlow<String?>(null)
    val currentStoryId: StateFlow<String?> = _currentStoryId.asStateFlow()

    // Lists and Streams
    val savedStories: StateFlow<List<SavedStory>> = repository.savedStories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pendingStories: StateFlow<List<CommunitySubmission>> = repository.pendingStories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val currentUser: StateFlow<UserProfile?> = repository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Combined stream of Curated + Approved Community Stories
    val allStories: StateFlow<List<Story>> = combine(
        flowOf(repository.curatedStories),
        repository.communityStories
    ) { curated, community ->
        // Return curated first, then community approved
        curated + community
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.curatedStories
    )

    // Filtered Stories for active UI
    val filteredStories: StateFlow<List<Story>> = combine(
        allStories,
        _selectedGenre,
        _searchQuery
    ) { storiesList, genre, query ->
        storiesList.filter { story ->
            // Search match (title, body, or excerpt in either English or Nepali)
            val matchQuery = query.isBlank() || 
                    story.titleNp.contains(query, ignoreCase = true) ||
                    story.titleEn.contains(query, ignoreCase = true) ||
                    story.body.contains(query, ignoreCase = true) ||
                    story.excerpt.contains(query, ignoreCase = true)

            // Genre match
            val matchGenre = when (genre) {
                "सबै", "All" -> true
                "भूत", "Ghost" -> story.genre.lowercase() == "ghost"
                "लोककथा", "Folklore" -> story.genre.lowercase() == "folklore"
                "रहस्य", "Mystery" -> story.genre.lowercase() == "mystery"
                "Underrated" -> story.genre.lowercase() == "underrated"
                "Community", "सामुदायिक" -> story.genre.lowercase() == "community"
                else -> story.genre.lowercase() == genre.lowercase()
            }

            matchQuery && matchGenre
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Get specific story details
    val activeStory: StateFlow<Story?> = combine(
        allStories,
        _currentStoryId
    ) { storiesList, id ->
        if (id == null) null
        else storiesList.find { it.id == id } ?: savedStories.value.find { it.storyId == id }?.let { saved ->
            Story(
                id = saved.storyId,
                titleNp = saved.titleNp,
                titleEn = saved.titleEn,
                genre = saved.genre,
                author = saved.author,
                excerpt = saved.excerpt,
                body = saved.body,
                readTimeMinutes = saved.readTimeMinutes,
                coverEmoji = "🔖"
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    init {
        // Set up the reader's default font size matching persisted settings
        viewModelScope.launch {
            userSettings.collect { settings ->
                _readerFontSizeSp.value = settings.fontSizeSp
            }
        }
    }

    // Settings actions
    fun setThemePalette(palette: ThemePalette) {
        viewModelScope.launch {
            preferencesManager.updateThemePalette(palette)
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateDarkMode(isDark)
        }
    }

    fun setUseSystemDarkMode(useSystem: Boolean) {
        viewModelScope.launch {
            preferencesManager.updateUseSystemDarkMode(useSystem)
        }
    }

    fun setLanguage(langCode: String) {
        viewModelScope.launch {
            preferencesManager.updateLanguageCode(langCode)
        }
    }

    // Genre selection
    fun setGenreFilter(genre: String) {
        _selectedGenre.value = genre
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectStory(storyId: String?) {
        _currentStoryId.value = storyId
        // Reset reader dark mode overrides upon opening a new story
        _readerDarkModeOverride.value = null
    }

    // Bookmark actions
    fun toggleSaveStory(story: Story) {
        viewModelScope.launch {
            val savedList = savedStories.value
            val isAlreadySaved = savedList.any { it.storyId == story.id }
            if (isAlreadySaved) {
                repository.unsaveStory(story.id)
            } else {
                repository.saveStory(story)
            }
        }
    }

    fun deleteSavedStoryById(id: String) {
        viewModelScope.launch {
            repository.unsaveStory(id)
        }
    }

    fun isStorySavedFlow(storyId: String): Flow<Boolean> {
        return repository.isStorySaved(storyId)
    }

    // Auth actions
    fun performSimulatedGoogleLogin(name: String, email: String) {
        val avatarUrl = when (name.lowercase()) {
            "pawan" -> "https://api.dicebear.com/7.x/pixel-art/svg?seed=Pawan"
            "priya" -> "https://api.dicebear.com/7.x/pixel-art/svg?seed=Priya"
            "rahul" -> "https://api.dicebear.com/7.x/pixel-art/svg?seed=Rahul"
            else -> "https://api.dicebear.com/7.x/pixel-art/svg?seed=$name"
        }
        repository.signInWithGoogle(name, email, avatarUrl)
    }

    fun performLogout() {
        repository.signOut()
    }

    // Submission actions
    fun submitCommunityStory(titleNp: String, titleEn: String, genre: String, body: String, author: String, imageUrl: String? = null) {
        repository.submitStory(
            titleNp = titleNp,
            titleEn = titleEn,
            genre = genre,
            body = body,
            authorName = author,
            imageUrl = imageUrl
        )
    }

    // Admin commands
    fun approveSubmission(id: String) {
        repository.approveSubmission(id)
    }

    fun rejectSubmission(id: String) {
        repository.rejectSubmission(id)
    }

    // Reader UI actions
    fun toggleReaderDarkMode() {
        // Toggle: themeDefault -> lightDefault -> darkDefault -> themeDefault
        _readerDarkModeOverride.value = when (_readerDarkModeOverride.value) {
            null -> false // Go to Light explicit
            false -> true  // Go to Dark explicit
            true -> null   // Revert to global system default
        }
    }

    fun setReaderFontSize(sp: Int) {
        _readerFontSizeSp.value = sp
        viewModelScope.launch {
            preferencesManager.updateFontSize(sp)
        }
    }
}
