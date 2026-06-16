package com.example.eas

import com.example.eas.util.PointRules
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PointCalculationTest {

    @Test
    fun `Rp10000 earns exactly 1 point`() {
        assertEquals(1, PointRules.calculatePoints(10_000L))
    }

    @Test
    fun `Rp150000 earns exactly 15 points - Scenario B`() {
        assertEquals(15, PointRules.calculatePoints(150_000L))
    }

    @Test
    fun `Rp9999 earns 0 points - below threshold`() {
        assertEquals(0, PointRules.calculatePoints(9_999L))
    }

    @Test
    fun `Rp25000 earns 2 points - floor division`() {
        assertEquals(2, PointRules.calculatePoints(25_000L))
    }

    @Test
    fun `Rp0 earns 0 points`() {
        assertEquals(0, PointRules.calculatePoints(0L))
    }

    @Test
    fun `Rp1000000 earns 100 points`() {
        assertEquals(100, PointRules.calculatePoints(1_000_000L))
    }

    @Test
    fun `Member with exactly 50 points can redeem Espresso - Scenario C`() {
        assertTrue(PointRules.canRedeem(50, 50))
    }

    @Test
    fun `Member with 49 points cannot redeem Espresso - Scenario D`() {
        assertFalse(PointRules.canRedeem(49, 50))
    }

    @Test
    fun `Member with 100 points can redeem Cappuccino`() {
        assertTrue(PointRules.canRedeem(100, 100))
    }

    @Test
    fun `Member with 149 points cannot redeem Latte Gratis`() {
        assertFalse(PointRules.canRedeem(149, 150))
    }

    @Test
    fun `Member with 200 points can redeem any reward`() {
        assertTrue(PointRules.canRedeem(200, 50))
        assertTrue(PointRules.canRedeem(200, 100))
        assertTrue(PointRules.canRedeem(200, 150))
    }

    @Test
    fun `Points deducted correctly after redeem`() {
        assertEquals(50, PointRules.deductPoints(100, 50))
        assertEquals(0, PointRules.deductPoints(100, 100))
        assertEquals(50, PointRules.deductPoints(200, 150))
    }
}
