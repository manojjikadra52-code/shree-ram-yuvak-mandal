package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class Member(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phone: String,
    val joinedMonth: Int, // 1 to 12
    val joinedYear: Int  // e.g. 2026
)

@Entity(tableName = "payments")
data class MemberPayment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val memberId: Int,
    val amount: Int,       // typically standard ₹100, but can be any amount
    val paidDate: Long,    // timestamp
    val forMonth: Int,     // 1 to 12
    val forYear: Int       // e.g. 2026
)

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Int,
    val date: Long,        // timestamp
    val description: String,
    val category: String   // e.g., "Donation", "Event", "Rent", "Other"
)

@Entity(tableName = "notifications")
data class MandalNotification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val date: Long,        // timestamp
    val senderName: String = "Admin"
)
