package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "kathakosh_preferences")

enum class ThemePalette(val key: String, val npLabel: String, val enLabel: String) {
    MISTY_ROSE("MISTY_ROSE", "मिस्टी रोज", "Misty Rose"),
    SMOKY_BLUE("SMOKY_BLUE", "धुँवाली नील", "Smoky Blue"),
    SANDALWOOD("SANDALWOOD", "चन्दन (Natural Tones)", "Natural Tones (Sandalwood)"),
    PALE_MOSS("PALE_MOSS", "फिका हरियो", "Pale Moss"),
    RHODODENDRON("RHODODENDRON", "लालीगुराँस", "Rhododendron"),
    VIOLET_HAZE("VIOLET_HAZE", "बैजनी धुन", "Violet Haze"),
    DYNAMIC("DYNAMIC", "मटेरियल यु (Dynamic)", "Material You (Dynamic)")
}

enum class ReadingFontSize(val sizeSp: Int, val labelNp: String, val labelEn: String) {
    S(14, "सानो (S)", "Small (S)"),
    M(18, "मध्यम (M)", "Medium (M)"),
    L(22, "ठूलो (L)", "Large (L)"),
    XL(26, "अति ठूलो (XL)", "Extra Large (XL)");

    companion object {
        fun fromSp(sp: Int): ReadingFontSize {
            return entries.find { it.sizeSp == sp } ?: M
        }
    }
}

data class UserSettings(
    val themePalette: ThemePalette,
    val isDarkMode: Boolean,
    val useSystemDarkMode: Boolean,
    val fontSizeSp: Int,
    val languageCode: String // "np" or "en"
)

class UserPreferencesManager(private val context: Context) {

    private object PreferencesKeys {
        val THEME_PALETTE = stringPreferencesKey("theme_palette")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val USE_SYSTEM_DARK_MODE = booleanPreferencesKey("use_system_dark_mode")
        val FONT_SIZE_SP = intPreferencesKey("font_size_sp")
        val LANGUAGE_CODE = stringPreferencesKey("language_code")
    }

    val userSettingsFlow: Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeKey = preferences[PreferencesKeys.THEME_PALETTE] ?: ThemePalette.SANDALWOOD.key
            val themePalette = ThemePalette.entries.find { it.key == themeKey } ?: ThemePalette.SANDALWOOD

            val isDarkMode = preferences[PreferencesKeys.DARK_MODE] ?: false // Default to light mode (Natural Tones) by default
            val useSystemDarkMode = preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] ?: false
            val fontSizeSp = preferences[PreferencesKeys.FONT_SIZE_SP] ?: ReadingFontSize.M.sizeSp
            val languageCode = preferences[PreferencesKeys.LANGUAGE_CODE] ?: "np"

            UserSettings(
                themePalette = themePalette,
                isDarkMode = isDarkMode,
                useSystemDarkMode = useSystemDarkMode,
                fontSizeSp = fontSizeSp,
                languageCode = languageCode
            )
        }

    suspend fun updateThemePalette(palette: ThemePalette) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_PALETTE] = palette.key
        }
    }

    suspend fun updateDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_MODE] = isDark
            preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] = false
        }
    }

    suspend fun updateUseSystemDarkMode(useSystem: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USE_SYSTEM_DARK_MODE] = useSystem
        }
    }

    suspend fun updateFontSize(sizeSp: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FONT_SIZE_SP] = sizeSp
        }
    }

    suspend fun updateLanguageCode(langCode: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LANGUAGE_CODE] = langCode
        }
    }
}
