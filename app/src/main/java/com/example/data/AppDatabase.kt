package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Member::class, MemberPayment::class, Expense::class, MandalNotification::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun paymentDao(): PaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mandal_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed database on creation in IO thread
                        CoroutineScope(Dispatchers.IO).launch {
                            populateInitialData(getDatabase(context))
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(db: AppDatabase) {
            val memberDao = db.memberDao()
            val paymentDao = db.paymentDao()
            val expenseDao = db.expenseDao()
            val notificationDao = db.notificationDao()

            // 1. Add members (Joining dates in 2026)
            val idRamesh = memberDao.insertMember(Member(name = "Ramesh Patel", phone = "+91 98765 43210", joinedMonth = 1, joinedYear = 2026))
            val idSuresh = memberDao.insertMember(Member(name = "Suresh Jadav", phone = "+91 95432 10987", joinedMonth = 1, joinedYear = 2026))
            val idDinesh = memberDao.insertMember(Member(name = "Dinesh Solanki", phone = "+91 91234 56789", joinedMonth = 1, joinedYear = 2026))
            val idMahesh = memberDao.insertMember(Member(name = "Mahesh Parmar", phone = "+91 88776 65544", joinedMonth = 3, joinedYear = 2026))
            val idHitesh = memberDao.insertMember(Member(name = "Hitesh Vaghela", phone = "+91 77665 54433", joinedMonth = 1, joinedYear = 2026))
            val idKamlesh = memberDao.insertMember(Member(name = "Kamlesh Mori", phone = "+91 66554 43322", joinedMonth = 2, joinedYear = 2026))

            // Approximate timestamps based on June 1st, 2026
            val baseTime = 1780303325000L // around June 2026

            // 2. Add payments
            // Ramesh Patel: fully paid up to the current month (Jan to June = 6 months)
            for (month in 1..6) {
                paymentDao.insertPayment(
                    MemberPayment(
                        memberId = idRamesh.toInt(),
                        amount = 100,
                        paidDate = baseTime - (6 - month) * 30 * 24 * 3600 * 1000L,
                        forMonth = month,
                        forYear = 2026
                    )
                )
            }

            // Suresh Jadav: paid full year (Jan to December = 12 months)
            // Months Jan-June are normal standard, and July-Dec are advances!
            for (month in 1..12) {
                paymentDao.insertPayment(
                    MemberPayment(
                        memberId = idSuresh.toInt(),
                        amount = 100,
                        paidDate = baseTime - (12 - month) * 15 * 24 * 3600 * 1000L,
                        forMonth = month,
                        forYear = 2026
                    )
                )
            }

            // Dinesh Solanki: paid Jan, Feb, Mar. (April, May, June pending!)
            for (month in 1..3) {
                paymentDao.insertPayment(
                    MemberPayment(
                        memberId = idDinesh.toInt(),
                        amount = 100,
                        paidDate = baseTime - (8 - month) * 30 * 24 * 3600 * 1000L,
                        forMonth = month,
                        forYear = 2026
                    )
                )
            }

            // Mahesh Parmar (joined March): paid March, April, May. (June pending!)
            for (month in 3..5) {
                paymentDao.insertPayment(
                    MemberPayment(
                        memberId = idMahesh.toInt(),
                        amount = 100,
                        paidDate = baseTime - (6 - month) * 20 * 24 * 3600 * 1000L,
                        forMonth = month,
                        forYear = 2026
                    )
                )
            }

            // Kamlesh Mori (joined Feb): paid Feb, March, April. (May, June pending)
            for (month in 2..4) {
                paymentDao.insertPayment(
                    MemberPayment(
                        memberId = idKamlesh.toInt(),
                        amount = 100,
                        paidDate = baseTime - (7 - month) * 30 * 24 * 3600 * 1000L,
                        forMonth = month,
                        forYear = 2026
                    )
                )
            }

            // Hitesh Vaghela has paid nothing (Jan to June all 6 months pending!)

            // 3. Add expenses (Javak)
            expenseDao.insertExpense(
                Expense(
                    title = "Ram Pratistha Seva",
                    amount = 1500,
                    date = baseTime - 45 * 24 * 3600 * 1000L, // April
                    description = "Ram Mandir Pratistha Mahotsav ma Mandir samiti ne dan aapyu.",
                    category = "Donation"
                )
            )
            expenseDao.insertExpense(
                Expense(
                    title = "Annakshetra Food Donation",
                    amount = 500,
                    date = baseTime - 12 * 24 * 3600 * 1000L, // Mid-May
                    description = "Ram Navami nimitte garibo ane jaruriyatmando ne bhojan vitaran karyu.",
                    category = "Event"
                )
            )

            // 4. Seed friendly announcements (Mandal Notifications)
            notificationDao.insertNotification(
                MandalNotification(
                    title = "મંડળનું નવું શિડ્યુલ અને બજેટ 📋",
                    message = "નમસ્તે ભાઈઓ, આપણી જૂનની આગામી સભા રવિવારે સાંજે ૫:૦૦ કલાકે કમિટી હોલ ખાતે રાખેલ છે. તમામ સભ્યોને સમયસર હાજર રહેવા નમ્ર વિનંતી.",
                    date = baseTime - 2 * 24 * 3600 * 1000L, // 2 days ago
                    senderName = "એડમિન (Admin)"
                )
            )
            notificationDao.insertNotification(
                MandalNotification(
                    title = "શ્રી રામ પ્રતિષ્ઠા મહોત્સવ બદલ આભાર 🙏",
                    message = "મંડળના રમેશભાઈ તેમજ તમામ સભ્યોએ રામ પ્રતિષ્ઠા મહોત્સવમાં સક્રિય સેવા જમા કરી ખૂબ સારો યશ કમાવ્યો છે. બધાય ભાઈઓનો હૃદયથી આભાર!",
                    date = baseTime - 5 * 24 * 3600 * 1000L, // 5 days ago
                    senderName = "એડમિન (Admin)"
                )
            )
        }
    }
}
