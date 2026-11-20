package com.pocketsheet.domain.rules

import kotlin.math.floor

object Rules5eCalculator {

    /**
     * Calculates ability modifier: floor((score - 10) / 2)
     */
    fun calculateModifier(score: Int): Int {
        return floor((score - 10) / 2.0).toInt()
    }

    /**
     * Formats modifier to string with explicit sign (e.g. +3, -1, +0)
     */
    fun formatModifier(score: Int): String {
        val mod = calculateModifier(score)
        return if (mod >= 0) "+$mod" else "$mod"
    }

    /**
     * Formats integer bonus with sign (e.g. +4, -1, +0)
     */
    fun formatBonus(bonus: Int): String {
        return if (bonus >= 0) "+$bonus" else "$bonus"
    }

    /**
     * Calculates proficiency bonus based on character level:
     * Levels 1-4: +2
     * Levels 5-8: +3
     * Levels 9-12: +4
     * Levels 13-16: +5
     * Levels 17-20: +6
     */
    fun calculateProficiencyBonus(level: Int): Int {
        val safeLevel = level.coerceIn(1, 20)
        return 2 + ((safeLevel - 1) / 4)
    }

    /**
     * Calculates passive perception: 10 + WIS mod + (Perception Prof Level * Proficiency Bonus)
     */
    fun calculatePassivePerception(wisdomScore: Int, perceptionSkillLevel: Int, level: Int): Int {
        val wisMod = calculateModifier(wisdomScore)
        val pb = calculateProficiencyBonus(level)
        return 10 + wisMod + (perceptionSkillLevel * pb)
    }

    /**
     * Calculates Spell Save DC: 8 + PB + Casting Ability Modifier
     */
    fun calculateSpellSaveDc(abilityScore: Int, level: Int): Int {
        return 8 + calculateProficiencyBonus(level) + calculateModifier(abilityScore)
    }

    /**
     * Calculates Spell Attack Bonus: PB + Casting Ability Modifier
     */
    fun calculateSpellAttackBonus(abilityScore: Int, level: Int): Int {
        return calculateProficiencyBonus(level) + calculateModifier(abilityScore)
    }
}
