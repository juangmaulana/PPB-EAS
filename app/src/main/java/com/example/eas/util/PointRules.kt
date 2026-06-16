package com.example.eas.util

object PointRules {
    fun calculatePoints(amount: Long): Int = (amount / 10_000L).toInt()

    fun canRedeem(memberPoints: Int, rewardCost: Int): Boolean = memberPoints >= rewardCost

    fun deductPoints(currentPoints: Int, cost: Int): Int = currentPoints - cost
}
