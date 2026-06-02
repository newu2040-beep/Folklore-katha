package com.example.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CommunitySubmission(
    val id: String,
    val titleNp: String,
    val titleEn: String,
    val body: String,
    val genre: String,
    val author: String,
    val status: String, // "Pending" | "Approved" | "Rejected"
    val submittedBy: String,
    val imageUrl: String? = null,
    val submittedAt: Long = System.currentTimeMillis()
)

data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val photoUrl: String?
)

class StoriesRepository(
    private val context: Context,
    private val savedStoryDao: SavedStoryDao
) {
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    // Curated pre-seeded stories (always available)
    val curatedStories: List<Story> = Story.seededStories

    // Simulated community submissions in-memory so they are interactive and dynamic
    private val _communityStories = MutableStateFlow<List<Story>>(emptyList())
    val communityStories: StateFlow<List<Story>> = _communityStories.asStateFlow()

    private val _pendingStories = MutableStateFlow<List<CommunitySubmission>>(emptyList())
    val pendingStories: StateFlow<List<CommunitySubmission>> = _pendingStories.asStateFlow()

    // Authentication State
    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    // Room Database Favorite Stories Flow
    val savedStories: Flow<List<SavedStory>> = savedStoryDao.getAllSavedStories()

    init {
        // Initialize with default community-approved stories
        _communityStories.value = listOf(
            Story(
                id = "comm_01",
                titleNp = "चितवनको कुइरे ढोकाको प्रेत",
                titleEn = "The Wraith of Chitwan Kuire Door",
                genre = "Community",
                author = "अविनाश तामाङ",
                excerpt = "गाउँको पुरानो कुइरे ढोका चोकमा राती ठ्याक्कै बाह्र बजे रोइरहने रहस्यमयी सेतो सिँउदो र सिन्दूर लगाएकी आत्मा।",
                readTimeMinutes = 5,
                coverEmoji = "🚪",
                body = """
                    मेरो नाम अविनाश हो। म यो घटना चितवनको रामपुर नजिकै रहेको मेरो पुर्ख्यौली गाउँमा भोग्न पुगेको हुँ। गाउँको बुढो चोकमा एउटा पुरानो इँटा र काठको घर छ, जसलाई स्थानीयहरू 'कुइरे ढोका' भन्छन्। भनिन्छ, राणाकालमा त्यहाँ सैनिकहरू बस्ने गर्थे।
                    
                    २०७५ सालको तिहारको एकादशीको राती म आफ्नो भतिजोको विवाह सकेर फर्किँदै थिएँ। रातको १ बजिसकेको थियो। कुइरे ढोका निर पुग्दा चिसो सिरेटो चल्यो। अचानक कसैले कोमल स्वरमा रुँदै गरेको सुनेँ। मैले टर्च बालेँ— ढोकाको सिँढीमा एउटी महिला बेहुलीको भेषमा बसेकी थिइन्। उनको सिँउदोमा रगत जस्तै रातो ताजा सिन्दूर सजिएको थियो।
                    
                    तर जब उनले मतिर हेरिन्, उनको अनुहारबाट सिन्दूर बिस्तारै पानी बनेर बग्न थाल्यो र सिंगो अनुहार रगतमा डुबे झैँ भयो। म डरले कम्पमान भएँ र भागेँ। पछि बुझ्दा गाउँमा ७० वर्ष पहिले एक दुलहीको विवाहकै दिन सोही ढोका चोकमा हृदयघात भई मृत्यु भएको रहेछ।
                """.trimIndent()
            ),
            Story(
                id = "comm_02",
                titleNp = "सिन्धुपाल्चोकको जङ्गली डाकिनी",
                titleEn = "The Wild Witch of Sindhupalchok",
                genre = "Community",
                author = "निमा शेर्पा",
                excerpt = "सिन्धुपाल्चोकको भिरालो जङ्गलमा राती दाउरा टिप्न जाँदा भेटिएकी अनौठी जंगली बुढी जससँग कुरा गर्दा होस हराम हुन्छ।",
                readTimeMinutes = 6,
                coverEmoji = "🔮",
                body = """
                    यो घटना मेरो हजुरबुबाले सुनाउनुभएको हो। सिन्धुपाल्चोकको जुगल हिमालको फेदमा पर्ने घना जङ्गलमा रातीको समयमा कोही पनि गोठालाहरू दाउरा वा जडिबुटी लिन एक्लै जाँदैनथे। उनीहरू भन्थे, जङ्गलको बीचमा एउटी भयङ्कर 'डाकिनी' बस्छे।
                    
                    एक पटक हिउँदमा एउटा गोठालो दाजु साहसी बनेर राती जङ्गल पसे। उनले ठूलो चिनी जडिबुटी फेला पारे। खुशी हुँदै फर्किँदा पछाडिबाट एउटी बुढी आइन्। बुढीका नङहरू ४ इन्च लामा थिए र कपाल पूरै सेतो थियो। बुढीले भनिन्, "मेरो घरसम्म आइदेऊ बाबु, म तिमीलाई अनौठो मणि दिन्छु।"
                    
                    गोठालो दाजुलाई शंका लाग्यो। उनले नियालेर हेर्दा बुढीको छाया जमिनमा थिएन र उनी हिँड्दा रुखका पातहरू हल्लिरहेका थिएनन्। बुढी डाकिनी थिइन्! उनले खुकुरी झिकेर 'जुगल माईको जय' भन्दै हाने। बुढी ठूलो चित्कार सुसेल्दै अँध्यारो वनमा भागेकी थिइन्।
                """.trimIndent()
            )
        )
    }

    fun isStorySaved(storyId: String): Flow<Boolean> {
        return savedStoryDao.isStorySaved(storyId)
    }

    suspend fun saveStory(story: Story) {
        savedStoryDao.insertSavedStory(story.toSavedStory())
    }

    suspend fun unsaveStory(storyId: String) {
        savedStoryDao.deleteSavedStoryById(storyId)
    }

    // Interactive Google Sign-In with full state persistence inside application session
    fun signInWithGoogle(displayName: String, email: String, photoUrl: String?) {
        val uid = "user_sim_" + email.hashCode()
        _currentUser.value = UserProfile(
            uid = uid,
            displayName = displayName,
            email = email,
            photoUrl = photoUrl
        )
    }

    fun signOut() {
        _currentUser.value = null
    }

    // Submit community post
    fun submitStory(titleNp: String, titleEn: String, genre: String, body: String, authorName: String, imageUrl: String? = null) {
        val user = _currentUser.value ?: return
        val newId = "user_post_" + System.currentTimeMillis()
        val excerpt = if (body.length > 100) body.take(97) + "..." else body

        val submission = CommunitySubmission(
            id = newId,
            titleNp = titleNp,
            titleEn = titleEn,
            body = body,
            genre = genre,
            author = authorName.ifBlank { user.displayName },
            status = "Pending",
            submittedBy = user.uid,
            imageUrl = imageUrl
        )

        // Add to pending pool
        val currentPending = _pendingStories.value.toMutableList()
        currentPending.add(0, submission)
        _pendingStories.value = currentPending
    }

    // Admin-Simulator function to immediately approve stories so users can experience approvals and community updates!
    fun approveSubmission(id: String) {
        val pendingList = _pendingStories.value.toMutableList()
        val index = pendingList.indexOfFirst { it.id == id }
        if (index != -1) {
            val approved = pendingList[index].copy(status = "Approved")
            pendingList[index] = approved
            _pendingStories.value = pendingList

            // Add to client-side feed of approved community stories
            val currentCommunity = _communityStories.value.toMutableList()
            val newStory = Story(
                id = approved.id,
                titleNp = approved.titleNp,
                titleEn = approved.titleEn,
                genre = approved.genre,
                author = approved.author,
                excerpt = if (approved.body.length > 120) approved.body.take(117) + "..." else approved.body,
                readTimeMinutes = maxOf(3, approved.body.length / 300),
                coverEmoji = "✍️",
                imageUrl = approved.imageUrl,
                body = approved.body
            )
            currentCommunity.add(0, newStory)
            _communityStories.value = currentCommunity
        }
    }

    fun rejectSubmission(id: String) {
        val pendingList = _pendingStories.value.toMutableList()
        val index = pendingList.indexOfFirst { it.id == id }
        if (index != -1) {
            pendingList[index] = pendingList[index].copy(status = "Rejected")
            _pendingStories.value = pendingList
        }
    }
}
