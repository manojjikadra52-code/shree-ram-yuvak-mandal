package com.example.ui

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import org.json.JSONArray
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class BusinessBanner(
    val id: String,
    val title: String,
    val description: String,
    val phone: String,
    val imageUrl: String = "",
    val bgColorHex: String = "#FFF3E0"
)

class MandalViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    val repo = MandalRepository(db.memberDao(), db.paymentDao(), db.expenseDao(), db.notificationDao())

    private val sharedPrefs = application.getSharedPreferences("mandal_prefs", Context.MODE_PRIVATE)

    val bannersState = MutableStateFlow<List<BusinessBanner>>(emptyList())

    // Form: Business Ads
    var newAdTitle by mutableStateOf("")
    var newAdDescription by mutableStateOf("")
    var newAdPhone by mutableStateOf("")
    var newAdImageUrl by mutableStateOf("")
    var newAdBgColor by mutableStateOf("#FFF3E0")

    init {
        loadBanners()
    }

    fun loadBanners() {
        val jsonStr = sharedPrefs.getString("business_banners", null)
        if (jsonStr.isNullOrEmpty()) {
            val defaultBanners = listOf(
                BusinessBanner(
                    UUID.randomUUID().toString(),
                    "MARUTI MOBILE & ELECTRONICS",
                    "મોબાઈલ અને ઈલેક્ટ્રોનિક્સ વસ્તુઓ પર મેળવો ભવ્ય ડિસ્કાઉન્ટ! નવું વર્ષ, નવો ફોન!",
                    "9876543210",
                    "",
                    "#FFF3E0"
                ),
                BusinessBanner(
                    UUID.randomUUID().toString(),
                    "SHREEJI RESTAURANT & BANQUET",
                    "બર્થડે પાર્ટી અને લગ્ન પ્રસંગ માટે સ્પેશિયલ કાઠિયાવાડી મેનુ. અસલ અને સ્વાદિષ્ટ!",
                    "8765432109",
                    "",
                    "#E8F5E9"
                ),
                BusinessBanner(
                    UUID.randomUUID().toString(),
                    "PATEL GARMENTS & BOUTIQUE",
                    "લેટેસ્ટ ટ્રેન્ડીંગ કપડાં અને ટ્રેડિશનલ વેરનો અદભુત ખજાનો! ખરીદી પર ૨૦% ડિસ્કાઉન્ટ!",
                    "9988776655",
                    "",
                    "#E3F2FD"
                ),
                BusinessBanner(
                    UUID.randomUUID().toString(),
                    "HARI OM TRAVELS",
                    "ગુજરાત અને ભારતના મનોહર સ્થળોના પ્રવાસનું વહેલું બુકિંગ કરવા માટે આજે જ સંપર્ક કરો.",
                    "9123456789",
                    "",
                    "#F3E5F5"
                ),
                BusinessBanner(
                    UUID.randomUUID().toString(),
                    "GAJANAND SWEETS & NAMKEEN",
                    "શુદ્ધ દેશી ઘી માંથી બનેલી ટેસ્ટી મીઠાઈઓ અને ચટાકેદાર અસલ સુરતી નમકીન!",
                    "9000011111",
                    "",
                    "#FFFDE7"
                )
            )
            saveBanners(defaultBanners)
        } else {
            try {
                val array = JSONArray(jsonStr)
                val list = mutableListOf<BusinessBanner>()
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        BusinessBanner(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            title = obj.getString("title"),
                            description = obj.getString("description"),
                            phone = obj.getString("phone"),
                            imageUrl = obj.optString("imageUrl", ""),
                            bgColorHex = obj.optString("bgColorHex", "#FFF3E0")
                        )
                    )
                }
                bannersState.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveBanners(list: List<BusinessBanner>) {
        try {
            val array = JSONArray()
            for (item in list) {
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("title", item.title)
                obj.put("description", item.description)
                obj.put("phone", item.phone)
                obj.put("imageUrl", item.imageUrl)
                obj.put("bgColorHex", item.bgColorHex)
                array.put(obj)
            }
            sharedPrefs.edit().putString("business_banners", array.toString()).apply()
            bannersState.value = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addBanner(title: String, description: String, phone: String, imageUrl: String, bgColorHex: String) {
        val currentList = bannersState.value.toMutableList()
        val path = if (imageUrl.trim().isNotEmpty()) imageUrl.trim() else ""
        val newBanner = BusinessBanner(
            id = UUID.randomUUID().toString(),
            title = title.trim(),
            description = description.trim(),
            phone = phone.trim(),
            imageUrl = path,
            bgColorHex = if (bgColorHex.trim().isNotEmpty()) bgColorHex.trim() else "#FFF3E0"
        )
        if (currentList.size >= 10) {
            currentList.removeAt(0)
        }
        currentList.add(newBanner)
        saveBanners(currentList)
        
        // Reset form variables
        newAdTitle = ""
        newAdDescription = ""
        newAdPhone = ""
        newAdImageUrl = ""
        newAdBgColor = "#FFF3E0"
    }

    fun deleteBanner(id: String) {
        val currentList = bannersState.value.filter { it.id != id }
        saveBanners(currentList)
    }

    var userRole by mutableStateOf<String?>(sharedPrefs.getString("user_role", null))
        private set

    fun login(idInput: String, passInput: String): Boolean {
        val role = when {
            idInput == "admin" && passInput == "admin" -> "ADMIN"
            idInput == "123" && passInput == "123" -> "USER"
            else -> null
        }
        if (role != null) {
            sharedPrefs.edit().putString("user_role", role).apply()
            userRole = role
            return true
        }
        return false
    }

    fun logout() {
        sharedPrefs.edit().remove("user_role").apply()
        userRole = null
    }

    val isAdmin: Boolean
        get() = userRole == "ADMIN"

    val isUser: Boolean
        get() = userRole == "USER"

    val isLoggedIn: Boolean
        get() = userRole != null

    val members: StateFlow<List<Member>> = repo.allMembers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val payments: StateFlow<List<MemberPayment>> = repo.allPayments.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val expenses: StateFlow<List<Expense>> = repo.allExpenses.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val notifications: StateFlow<List<MandalNotification>> = repo.allNotifications.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var lastReadNotificationTime by mutableStateOf(sharedPrefs.getLong("last_read_notifications_time", 0L))
        private set

    val unreadNotificationsCount: Int
        get() = notifications.value.count { it.date > lastReadNotificationTime }

    fun markNotificationsAsRead() {
        val now = System.currentTimeMillis()
        sharedPrefs.edit().putLong("last_read_notifications_time", now).apply()
        lastReadNotificationTime = now
    }

    // Gemini AI Bulk Processing state variables
    var isGeminiProcessing by mutableStateOf(false)
    var geminiSuccessMessage by mutableStateOf<String?>(null)
    var geminiErrorMessage by mutableStateOf<String?>(null)

    // Reporting selection states
    var selectedReportYear by mutableStateOf(2026)
    var selectedReportMonth by mutableStateOf(6) // default: June 2026

    var reportType by mutableStateOf(0) // 0: Monthly, 1: Custom Date Range
    var reportStartDate by mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    )
    var reportEndDate by mutableStateOf(
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    )

    // Form: Member
    var newMemberName by mutableStateOf("")
    var newMemberPhone by mutableStateOf("")
    var newMemberJoinedMonth by mutableStateOf(6)
    var newMemberJoinedYear by mutableStateOf(2026)

    // Form: Expense (Javak)
    var newExpenseTitle by mutableStateOf("")
    var newExpenseAmount by mutableStateOf("")
    var newExpenseDescription by mutableStateOf("")
    var newExpenseCategory by mutableStateOf("Donation")

    // Form: Payment (Aavak)
    var payAmount by mutableStateOf("100")
    var payMemberId by mutableStateOf<Int?>(null)

    // Form: Notification (Message)
    var newNotificationTitle by mutableStateOf("")
    var newNotificationMessage by mutableStateOf("")

    fun addMember(onSuccess: () -> Unit = {}) {
        val name = newMemberName.trim()
        if (name.isEmpty()) return
        val phone = newMemberPhone.trim().ifEmpty { "+91 99999 99999" }
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertMember(
                Member(
                    name = name,
                    phone = phone,
                    joinedMonth = newMemberJoinedMonth,
                    joinedYear = newMemberJoinedYear
                )
            )
            viewModelScope.launch(Dispatchers.Main) {
                newMemberName = ""
                newMemberPhone = ""
                onSuccess()
            }
        }
    }

    fun deleteMember(member: Member) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteMember(member)
        }
    }

    fun sendNotification(onSuccess: () -> Unit = {}) {
        val title = newNotificationTitle.trim()
        val text = newNotificationMessage.trim()
        if (title.isEmpty() || text.isEmpty()) return
        
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertNotification(
                MandalNotification(
                    title = title,
                    message = text,
                    date = System.currentTimeMillis(),
                    senderName = if (isAdmin) "એડમિન (Admin)" else "મંડળ સભ્ય"
                )
            )
            viewModelScope.launch(Dispatchers.Main) {
                newNotificationTitle = ""
                newNotificationMessage = ""
                onSuccess()
            }
        }
    }

    fun deleteNotification(notification: MandalNotification) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNotification(notification)
        }
    }

    fun addExpense(onSuccess: () -> Unit = {}) {
        var title = newExpenseTitle.trim()
        val amtStr = newExpenseAmount.trim()
        val desc = newExpenseDescription.trim()
        if (amtStr.isEmpty()) return
        val amt = amtStr.toIntOrNull() ?: return
        
        // If user didn't enter a title, but entered details, fallback to details or category.
        if (title.isEmpty()) {
            title = if (desc.isNotEmpty()) {
                if (desc.length > 30) desc.take(27) + "..." else desc
            } else {
                newExpenseCategory
            }
        }
        
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertExpense(
                Expense(
                    title = title,
                    amount = amt,
                    date = System.currentTimeMillis(),
                    description = desc,
                    category = newExpenseCategory
                )
            )
            viewModelScope.launch(Dispatchers.Main) {
                newExpenseTitle = ""
                newExpenseAmount = ""
                newExpenseDescription = ""
                newExpenseCategory = "Donation"
                onSuccess()
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteExpense(expense)
        }
    }

    fun addPayment(memberId: Int, amount: Int, onSuccess: () -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            val member = members.value.find { it.id == memberId } ?: return@launch
            val mPayments = payments.value.filter { it.memberId == memberId }
            val paidSet = mPayments.map { Pair(it.forYear, it.forMonth) }.toSet()

            var remainingAmount = amount
            var currentY = member.joinedYear
            var currentM = member.joinedMonth
            
            val limitYear = Calendar.getInstance().get(Calendar.YEAR) + 3
            val now = System.currentTimeMillis()

            // 1. Pay oldest pending months first
            while (remainingAmount > 0 && currentY <= limitYear) {
                val key = Pair(currentY, currentM)
                if (!paidSet.contains(key)) {
                    val paySegment = if (remainingAmount >= 100) 100 else remainingAmount
                    repo.insertPayment(
                        MemberPayment(
                            memberId = memberId,
                            amount = paySegment,
                            paidDate = now,
                            forMonth = currentM,
                            forYear = currentY
                        )
                    )
                    remainingAmount -= paySegment
                }
                
                currentM++
                if (currentM > 12) {
                    currentM = 1
                    currentY++
                }
            }

            // 2. Pay future advances if money is left
            while (remainingAmount > 0) {
                val paySegment = if (remainingAmount >= 100) 100 else remainingAmount
                repo.insertPayment(
                    MemberPayment(
                        memberId = memberId,
                        amount = paySegment,
                        paidDate = now,
                        forMonth = currentM,
                        forYear = currentY
                    )
                )
                remainingAmount -= paySegment
                currentM++
                if (currentM > 12) {
                    currentM = 1
                    currentY++
                }
            }

            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

    fun deletePayment(payment: MemberPayment) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deletePayment(payment)
        }
    }

    fun processBulkImportWithGemini(rawText: String, onComplete: () -> Unit = {}) {
        if (rawText.trim().isEmpty()) {
            geminiErrorMessage = "કૃપા કરીને લખાણ દાખલ કરો!"
            return
        }
        
        isGeminiProcessing = true
        geminiSuccessMessage = null
        geminiErrorMessage = null
        
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
                    viewModelScope.launch(Dispatchers.Main) {
                        geminiErrorMessage = "Gemini API Key સેટ કરેલ નથી! Secrets પેનલ માં સેટ કરો."
                        isGeminiProcessing = false
                    }
                    return@launch
                }
                
                // Build systemInstruction and prompt
                val systemInstruction = "You are a perfect data parsing assistant for Shree Ram Yuvak Mandal app. Parse unstructured text in Gujarati or English and return ONLY a clean JSON object with keys: 'members' (array of {name, phone, joinedMonth, joinedYear}), 'payments' (array of {memberName, amount}), and 'expenses' (array of {title, category, amount, description}). DO NOT wrap in markdown code blocks. NO explanation text."
                
                val prompt = """
                    Extract data from the following text:
                    $rawText
                    
                    Remember:
                    1. Classify members: name, phone (10 digits, fallback if none), joinedMonth (1 to 12, fallback 6), joinedYear (fallback 2026).
                    2. Classify payments: memberName, amount (Int).
                    3. Classify expenses: title, category (Donation, Event, Rent, or Other), amount (Int), description.
                    
                    Only output the JSON object.
                """.trimIndent()
                
                // Build dynamic request JSON
                val jsonRequest = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            })
                        })
                    })
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    })
                    put("generationConfig", JSONObject().apply {
                        put("responseMimeType", "application/json")
                        put("temperature", 0.1)
                    })
                }
                
                val client = OkHttpClient.Builder()
                    .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                    .build()
                    
                val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
                val body = jsonRequest.toString().toRequestBody(mediaType)
                
                val request = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(body)
                    .build()
                    
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                if (!response.isSuccessful || responseBody == null) {
                    viewModelScope.launch(Dispatchers.Main) {
                        geminiErrorMessage = "Gemini API પ્રતિસાદ નિષ્ફળ: ${response.code}"
                        isGeminiProcessing = false
                    }
                    return@launch
                }
                
                // Parse response
                val respObj = JSONObject(responseBody)
                val candidates = respObj.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val contentObj = firstCandidate?.optJSONObject("content")
                val partsArr = contentObj?.optJSONArray("parts")
                val firstPart = partsArr?.optJSONObject(0)
                var responseText = firstPart?.optString("text") ?: ""
                
                // Clean up any markdown code blocks
                if (responseText.contains("```json")) {
                    responseText = responseText.substringAfter("```json").substringBeforeLast("```").trim()
                } else if (responseText.contains("```")) {
                    responseText = responseText.substringAfter("```").substringBeforeLast("```").trim()
                }
                
                val dataObj = JSONObject(responseText)
                
                val membersArr = dataObj.optJSONArray("members")
                val paymentsArr = dataObj.optJSONArray("payments")
                val expensesArr = dataObj.optJSONArray("expenses")
                
                var membersCount = 0
                var paymentsCount = 0
                var expensesCount = 0
                
                // 1. Process members insert
                val insertedMembers = mutableMapOf<String, Int>() // cache name -> id
                val currentMembersBefore = repo.allMembers.stateIn(viewModelScope).value
                
                if (membersArr != null) {
                    for (i in 0 until membersArr.length()) {
                        val m = membersArr.getJSONObject(i)
                        val name = m.getString("name").trim()
                        val phone = m.optString("phone").trim().ifEmpty { "+91 99999 99999" }
                        val joinedMonth = m.optInt("joinedMonth", 6)
                        val joinedYear = m.optInt("joinedYear", 2026)
                        
                        if (name.isNotEmpty()) {
                            // Check if member already exists
                            val existing = currentMembersBefore.find { it.name.trim().equals(name, ignoreCase = true) }
                            val id = if (existing == null) {
                                val newMember = Member(
                                    name = name,
                                    phone = phone,
                                    joinedMonth = joinedMonth,
                                    joinedYear = joinedYear
                                )
                                val insertedId = repo.insertMember(newMember)
                                membersCount++
                                insertedId.toInt()
                            } else {
                                existing.id
                            }
                            insertedMembers[name.lowercase(Locale.ROOT)] = id
                        }
                    }
                }
                
                // 2. Process payments insert
                if (paymentsArr != null) {
                    val currentMembersAfter = repo.allMembers.stateIn(viewModelScope).value
                    for (i in 0 until paymentsArr.length()) {
                        val p = paymentsArr.getJSONObject(i)
                        val memberName = p.getString("memberName").trim()
                        val amount = p.getInt("amount")
                        
                        if (memberName.isNotEmpty() && amount > 0) {
                            val mId = insertedMembers[memberName.lowercase(Locale.ROOT)]
                                ?: currentMembersAfter.find { it.name.trim().equals(memberName, ignoreCase = true) }?.id
                            
                            if (mId != null) {
                                viewModelScope.launch(Dispatchers.Main) {
                                    addPayment(mId, amount)
                                }
                                paymentsCount++
                            }
                        }
                    }
                }
                
                // 3. Process expenses insert
                if (expensesArr != null) {
                    for (i in 0 until expensesArr.length()) {
                        val e = expensesArr.getJSONObject(i)
                        val title = e.getString("title").trim()
                        val amount = e.getInt("amount")
                        val category = e.optString("category", "Event").trim()
                        val description = e.optString("description", "").trim()
                        
                        if (title.isNotEmpty() && amount > 0) {
                            val selectedCategory = when (category.uppercase(Locale.ROOT)) {
                                "DONATION" -> "Donation"
                                "RENT" -> "Rent"
                                "OTHER" -> "Other"
                                else -> "Event"
                            }
                            val newExpense = Expense(
                                title = title,
                                amount = amount,
                                date = System.currentTimeMillis(),
                                description = description,
                                category = selectedCategory
                            )
                            repo.insertExpense(newExpense)
                            expensesCount++
                        }
                    }
                }
                
                viewModelScope.launch(Dispatchers.Main) {
                    geminiSuccessMessage = "સફળતાપૂર્વક અપડેટ થયું: $membersCount સભ્યો ઉમેર્યા, $paymentsCount પેમેન્ટ સોંપ્યા, $expensesCount જાવક ఖર્ચ નોંધાયા! 🎉"
                    isGeminiProcessing = false
                    onComplete()
                }
                
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    geminiErrorMessage = "ડેટા પ્રોસેસ કરવામાં ભૂલ આવી: ${e.localizedMessage ?: e.message}"
                    isGeminiProcessing = false
                }
            }
        }
    }

    fun getMonthName(m: Int): String {
        return when (m) {
            1 -> "January (જાન્યુઆરી)"
            2 -> "February (ફેબ્રુઆરી)"
            3 -> "March (માર્ચ)"
            4 -> "April (એપ્રિલ)"
            5 -> "May (મે)"
            6 -> "June (જૂન)"
            7 -> "July (જુલાઈ)"
            8 -> "August (ઓગસ્ટ)"
            9 -> "September (સપ્ટેમ્બર)"
            10 -> "October (ઓક્ટોબર)"
            11 -> "November (નવેમ્બર)"
            12 -> "December (ડિસેમ્બર)"
            else -> ""
        }
    }

    fun getMonthNameShort(m: Int): String {
        return when (m) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> ""
        }
    }

    private fun isTimestampInMonthYear(timestamp: Long, month: Int, year: Int): Boolean {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp
        return cal.get(Calendar.YEAR) == year && (cal.get(Calendar.MONTH) + 1) == month
    }

    // PDF generation and share trigger
    fun downloadReport(context: Context) {
        val y = selectedReportYear
        val m = selectedReportMonth
        
        val isRange = reportType == 1
        
        val refCalendar = Calendar.getInstance().apply { timeInMillis = reportEndDate }
        val refYear = if (isRange) refCalendar.get(Calendar.YEAR) else y
        val refMonth = if (isRange) refCalendar.get(Calendar.MONTH) + 1 else m
        
        val currentMonthPayments = if (isRange) {
            payments.value.filter { it.paidDate in reportStartDate..reportEndDate }
        } else {
            payments.value.filter { isTimestampInMonthYear(it.paidDate, m, y) }
        }
        
        val currentMonthExpenses = if (isRange) {
            expenses.value.filter { it.date in reportStartDate..reportEndDate }
        } else {
            expenses.value.filter { isTimestampInMonthYear(it.date, m, y) }
        }
        
        val totalAavak = currentMonthPayments.sumOf { it.amount }
        val totalJavak = currentMonthExpenses.sumOf { it.amount }
        
        val totalAllAavak = payments.value.sumOf { it.amount }
        val totalAllJavak = expenses.value.sumOf { it.amount }
        val netBalance = totalAllAavak - totalAllJavak

        // Income names map aggregated by member name to keep report very compact
        val groupedPayments = currentMonthPayments.groupBy { it.memberId }
        val incomeList = groupedPayments.map { (memberId, pList) ->
            val memberName = members.value.find { it.id == memberId }?.name ?: "Unknown"
            val totalAmt = pList.sumOf { it.amount }
            Pair(memberName, totalAmt)
        }.sortedBy { it.first }

        // Expenses map
        val expenseList = currentMonthExpenses.map { exp ->
            Pair("${exp.title} (${exp.category})", exp.amount)
        }

        // Advance payer list (months > selected report indices)
        val advanceList = mutableListOf<Pair<String, Int>>()
        for (member in members.value) {
            val memberPayments = payments.value.filter { it.memberId == member.id }
            val advanceAmount = memberPayments.filter { 
                it.forYear > refYear || (it.forYear == refYear && it.forMonth > refMonth)
            }.sumOf { it.amount }
            
            if (advanceAmount > 0) {
                advanceList.add(Pair(member.name, advanceAmount))
            }
        }

        // Pending lists (unpaid months up to report indices)
        val pendingList = mutableListOf<Pair<String, String>>()
        for (member in members.value) {
            val memberPayments = payments.value.filter { it.memberId == member.id }
            val pList = calculatePendingMonths(member, memberPayments, refYear, refMonth)
            if (pList.isNotEmpty()) {
                val pStr = pList.joinToString(", ") { "${getMonthNameShort(it.second)} ${it.first}" }
                pendingList.add(Pair(member.name, pStr))
            }
        }

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val displayPeriodName = if (isRange) {
            "${dateFormat.format(Date(reportStartDate))} to ${dateFormat.format(Date(reportEndDate))}"
        } else {
            getMonthName(m)
        }

        val designReport = PdfReportData(
            monthName = displayPeriodName,
            year = if (isRange) 0 else y,
            totalAavak = totalAavak,
            totalJavak = totalJavak,
            netBalance = netBalance,
            incomeList = incomeList,
            expenseList = expenseList,
            advanceList = advanceList,
            pendingList = pendingList
        )

        val file = PdfGenerator.generateMonthlyReport(context, designReport)
        if (file != null) {
            Toast.makeText(context, "PDF Report saved successfully!", Toast.LENGTH_SHORT).show()
            sharePdf(context, file)
        } else {
            Toast.makeText(context, "Failed to generate PDF!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePdf(context: Context, file: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            context.startActivity(Intent.createChooser(intent, "Share Report via"))
        } catch (e: Exception) {
            Toast.makeText(context, "Error sharing PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun calculatePendingMonths(member: Member, memberPayments: List<MemberPayment>, targetYear: Int, targetMonth: Int): List<Pair<Int, Int>> {
        val paidSet = memberPayments.map { Pair(it.forYear, it.forMonth) }.toSet()
        val list = mutableListOf<Pair<Int, Int>>()
        
        var y = member.joinedYear
        var m = member.joinedMonth
        
        while (y < targetYear || (y == targetYear && m <= targetMonth)) {
            val key = Pair(y, m)
            if (!paidSet.contains(key)) {
                list.add(key)
            }
            m++
            if (m > 12) {
                m = 1
                y++
            }
        }
        return list
    }
}
