package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id LIMIT 1")
    fun getMemberById(id: Int): Member?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: Member): Long

    @Delete
    suspend fun deleteMember(member: Member)
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payments ORDER BY paidDate DESC")
    fun getAllPayments(): Flow<List<MemberPayment>>

    @Query("SELECT * FROM payments WHERE memberId = :memberId ORDER BY forYear DESC, forMonth DESC")
    fun getPaymentsByMember(memberId: Int): Flow<List<MemberPayment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: MemberPayment): Long

    @Delete
    suspend fun deletePayment(payment: MemberPayment)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Delete
    suspend fun deleteExpense(expense: Expense)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY date DESC")
    fun getAllNotifications(): Flow<List<MandalNotification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: MandalNotification): Long

    @Delete
    suspend fun deleteNotification(notification: MandalNotification)
}
