package com.example.data

import kotlinx.coroutines.flow.Flow

class MandalRepository(
    private val memberDao: MemberDao,
    private val paymentDao: PaymentDao,
    private val expenseDao: ExpenseDao,
    private val notificationDao: NotificationDao
) {
    val allMembers: Flow<List<Member>> = memberDao.getAllMembers()
    val allPayments: Flow<List<MemberPayment>> = paymentDao.getAllPayments()
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val allNotifications: Flow<List<MandalNotification>> = notificationDao.getAllNotifications()

    fun getPaymentsByMember(memberId: Int): Flow<List<MemberPayment>> {
        return paymentDao.getPaymentsByMember(memberId)
    }

    suspend fun getMemberByIdDirect(id: Int): Member? {
        return memberDao.getMemberById(id)
    }

    suspend fun insertMember(member: Member): Long {
        return memberDao.insertMember(member)
    }

    suspend fun deleteMember(member: Member) {
        memberDao.deleteMember(member)
    }

    suspend fun insertPayment(payment: MemberPayment): Long {
        return paymentDao.insertPayment(payment)
    }

    suspend fun deletePayment(payment: MemberPayment) {
        paymentDao.deletePayment(payment)
    }

    suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun insertNotification(notification: MandalNotification): Long {
        return notificationDao.insertNotification(notification)
    }

    suspend fun deleteNotification(notification: MandalNotification) {
        notificationDao.deleteNotification(notification)
    }
}
