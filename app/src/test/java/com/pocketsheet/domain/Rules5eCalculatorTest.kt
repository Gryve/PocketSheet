package com.pocketsheet.domain

import com.pocketsheet.domain.rules.Rules5eCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class Rules5eCalculatorTest {

    @Test
    fun testAbilityModifiers() {
        assertEquals(-1, Rules5eCalculator.calculateModifier(8))
        assertEquals(-1, Rules5eCalculator.calculateModifier(9))
        assertEquals(0, Rules5eCalculator.calculateModifier(10))
        assertEquals(0, Rules5eCalculator.calculateModifier(11))
        assertEquals(1, Rules5eCalculator.calculateModifier(12))
        assertEquals(2, Rules5eCalculator.calculateModifier(14))
        assertEquals(3, Rules5eCalculator.calculateModifier(16))
        assertEquals(4, Rules5eCalculator.calculateModifier(18))
        assertEquals(5, Rules5eCalculator.calculateModifier(20))
    }

    @Test
    fun testFormatModifier() {
        assertEquals("-1", Rules5eCalculator.formatModifier(8))
        assertEquals("+0", Rules5eCalculator.formatModifier(10))
        assertEquals("+2", Rules5eCalculator.formatModifier(14))
        assertEquals("+3", Rules5eCalculator.formatModifier(16))
    }

    @Test
    fun testProficiencyBonus() {
        // Level 1-4: +2
        assertEquals(2, Rules5eCalculator.calculateProficiencyBonus(1))
        assertEquals(2, Rules5eCalculator.calculateProficiencyBonus(3))
        assertEquals(2, Rules5eCalculator.calculateProficiencyBonus(4))

        // Level 5-8: +3
        assertEquals(3, Rules5eCalculator.calculateProficiencyBonus(5))
        assertEquals(3, Rules5eCalculator.calculateProficiencyBonus(8))

        // Level 9-12: +4
        assertEquals(4, Rules5eCalculator.calculateProficiencyBonus(9))
        assertEquals(4, Rules5eCalculator.calculateProficiencyBonus(12))

        // Level 13-16: +5
        assertEquals(5, Rules5eCalculator.calculateProficiencyBonus(13))

        // Level 17-20: +6
        assertEquals(6, Rules5eCalculator.calculateProficiencyBonus(17))
        assertEquals(6, Rules5eCalculator.calculateProficiencyBonus(20))
    }

    @Test
    fun testPassivePerception() {
        // Wis 16 (+3), Level 3 (PB +2), Perception Prof Level 1 (+2) -> 10 + 3 + 2 = 15
        assertEquals(15, Rules5eCalculator.calculatePassivePerception(16, 1, 3))

        // Wis 16 (+3), Level 3 (PB +2), Expertise Level 2 (+4) -> 10 + 3 + 4 = 17
        assertEquals(17, Rules5eCalculator.calculatePassivePerception(16, 2, 3))

        // Wis 10 (+0), Level 1, Prof Level 0 -> 10
        assertEquals(10, Rules5eCalculator.calculatePassivePerception(10, 0, 1))
    }

    @Test
    fun testSpellSaveDcAndAttack() {
        // Wis 16 (+3), Level 3 (PB +2)
        // DC: 8 + 2 + 3 = 13
        assertEquals(13, Rules5eCalculator.calculateSpellSaveDc(16, 3))
        // Attack: 2 + 3 = 5
        assertEquals(5, Rules5eCalculator.calculateSpellAttackBonus(16, 3))
    }
}
