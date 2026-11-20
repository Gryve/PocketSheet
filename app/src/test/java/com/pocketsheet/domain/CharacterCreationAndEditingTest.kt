package com.pocketsheet.domain

import com.pocketsheet.data.mapper.CharacterMapper
import com.pocketsheet.data.mapper.defaultJson
import com.pocketsheet.data.model.Character5eJson
import com.pocketsheet.domain.model.Character
import com.pocketsheet.domain.model.Spell
import com.pocketsheet.domain.model.Weapon
import com.pocketsheet.domain.rules.Rules5eCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterCreationAndEditingTest {

    @Test
    fun testCreateCharacterFromScratchAndDerivedStats() {
        val level = 5
        val wisdom = 18
        val pb = Rules5eCalculator.calculateProficiencyBonus(level) // +3 for level 5
        assertEquals(3, pb)

        val wisMod = Rules5eCalculator.calculateModifier(wisdom) // +4
        assertEquals(4, wisMod)

        val character = Character(
            id = 1,
            characterName = "Valeros",
            className = "Fighter",
            species = "Human",
            level = level,
            strength = 16,
            dexterity = 14,
            constitution = 14,
            intelligence = 10,
            wisdom = wisdom,
            charisma = 12,
            armorClass = 18,
            hpCurrent = 44,
            hpMax = 44,
            saveStrProf = true,
            saveConProf = true,
            skillAthletics = 2, // Expertise
            skillPerception = 1 // Proficient
        )

        // Verify Automatic Calculations:
        // Strength mod = +3, Save = +3 + PB(+3) = +6
        val strMod = Rules5eCalculator.calculateModifier(character.strength)
        assertEquals(3, strMod)
        val strSave = strMod + if (character.saveStrProf) pb else 0
        assertEquals(6, strSave)

        // Athletics expertise = 3 (strMod) + (2 * 3 pb) = 9
        val athleticsTotal = strMod + (character.skillAthletics * pb)
        assertEquals(9, athleticsTotal)

        // Perception proficient = 4 (wisMod) + (1 * 3 pb) = 7
        val perceptionTotal = wisMod + (character.skillPerception * pb)
        assertEquals(7, perceptionTotal)

        // Passive perception = 10 + 4 + 3 = 17
        val passivePerception = Rules5eCalculator.calculatePassivePerception(character.wisdom, character.skillPerception, level)
        assertEquals(17, passivePerception)
    }

    @Test
    fun testEditingAbilityScoreUpdatesAllDerivedValues() {
        var char = Character(
            characterName = "Aramil",
            level = 1, // PB = 2
            wisdom = 14, // mod = +2
            saveWisProf = true,
            skillPerception = 1
        )

        val pb = Rules5eCalculator.calculateProficiencyBonus(char.level)
        assertEquals(2, pb)

        // Initial: mod +2, save +4, skill +4, passive 14
        var wisMod = Rules5eCalculator.calculateModifier(char.wisdom)
        assertEquals(2, wisMod)
        assertEquals(4, wisMod + (if (char.saveWisProf) pb else 0))
        assertEquals(4, wisMod + (char.skillPerception * pb))
        assertEquals(14, Rules5eCalculator.calculatePassivePerception(char.wisdom, char.skillPerception, char.level))

        // Level up to level 4 and increase wisdom to 16 (+3)
        char = char.copy(wisdom = 16)
        wisMod = Rules5eCalculator.calculateModifier(char.wisdom)
        assertEquals(3, wisMod)
        assertEquals(5, wisMod + (if (char.saveWisProf) pb else 0))
        assertEquals(5, wisMod + (char.skillPerception * pb))
        assertEquals(15, Rules5eCalculator.calculatePassivePerception(char.wisdom, char.skillPerception, char.level))

        // Level up to level 5 (PB becomes +3)
        char = char.copy(level = 5)
        val pb5 = Rules5eCalculator.calculateProficiencyBonus(char.level)
        assertEquals(3, pb5)
        assertEquals(6, wisMod + (if (char.saveWisProf) pb5 else 0))
        assertEquals(6, wisMod + (char.skillPerception * pb5))
        assertEquals(16, Rules5eCalculator.calculatePassivePerception(char.wisdom, char.skillPerception, char.level))
    }

    @Test
    fun testAddWeaponsSpellsAndExportJson() {
        val created = Character(
            id = 10,
            characterName = "Lyra Silversong",
            className = "Bard",
            species = "Half-Elf",
            level = 2,
            charisma = 16,
            weapons = listOf(
                Weapon(name = "Rapier", atkBonusOverride = "+5", damageAndType = "1d8+3 Piercing", notes = "Finesse")
            ),
            spells = listOf(
                Spell(level = "1", name = "Dissonant Whispers", castingTime = "1 Action", range = "60 ft", notes = "3d6 Psychic")
            ),
            attunement1 = "Pipes of Haunting",
            equipment = "Lute, Leather Armor, Dagger",
            gp = 50
        )

        val jsonDto = CharacterMapper.domainToJson(created)
        val jsonString = defaultJson.encodeToString(Character5eJson.serializer(), jsonDto)

        // Verify JSON string contains custom weapons and spells
        assertTrue(jsonString.contains("Rapier"))
        assertTrue(jsonString.contains("Dissonant Whispers"))
        assertTrue(jsonString.contains("Pipes of Haunting"))

        // Re-import and verify roundtrip integrity
        val reimportedDto = defaultJson.decodeFromString<Character5eJson>(jsonString)
        val reimported = CharacterMapper.jsonToDomain(reimportedDto, id = 10)

        assertEquals("Lyra Silversong", reimported.characterName)
        assertEquals(1, reimported.weapons.size)
        assertEquals("Rapier", reimported.weapons[0].name)
        assertEquals(1, reimported.spells.size)
        assertEquals("Dissonant Whispers", reimported.spells[0].name)
        assertEquals("Pipes of Haunting", reimported.attunement1)
    }

    @Test
    fun testManageAndAddHigherLevelSpellSlots() {
        val initialSlots = mapOf("1" to 4, "2" to 2)
        var char = Character(
            characterName = "Elandor",
            spellSlotsTotal = initialSlots,
            spellSlotsExpended = mapOf("1" to 1, "2" to 0)
        )

        // Verify initial state
        assertEquals(4, char.spellSlotsTotal["1"])
        assertEquals(2, char.spellSlotsTotal["2"])
        assertEquals(null, char.spellSlotsTotal["3"])

        // Player gains 3rd level slots (2 slots of level 3) and increases 1st level to 5
        val updatedMap = char.spellSlotsTotal.toMutableMap().apply {
            this["1"] = 5
            this["3"] = 2
            this["9"] = 1 // 9th level slot!
        }

        char = char.copy(spellSlotsTotal = updatedMap)

        assertEquals(5, char.spellSlotsTotal["1"])
        assertEquals(2, char.spellSlotsTotal["2"])
        assertEquals(2, char.spellSlotsTotal["3"])
        assertEquals(1, char.spellSlotsTotal["9"])

        // Export and round-trip verification
        val jsonDto = CharacterMapper.domainToJson(char)
        val jsonString = defaultJson.encodeToString(Character5eJson.serializer(), jsonDto)

        assertTrue(jsonString.contains("\"3\": 2"))
        assertTrue(jsonString.contains("\"9\": 1"))

        val reimportedDto = defaultJson.decodeFromString<Character5eJson>(jsonString)
        assertEquals(2, reimportedDto.spellSlotsTotal["3"])
        assertEquals(1, reimportedDto.spellSlotsTotal["9"])
    }

    @Test
    fun testCustomSkillBonusAndPassivePerception() {
        val level = 3 // PB = +2
        val wisdom = 14 // mod = +2
        val strength = 16 // mod = +3
        val pb = Rules5eCalculator.calculateProficiencyBonus(level)
        assertEquals(2, pb)

        val customBonuses = mapOf(
            "skill_athletics" to 1, // Custom +1 from magical item/homebrew
            "skill_perception" to 2  // Custom +2 from feat
        )

        val char = Character(
            id = 5,
            characterName = "Thorin",
            level = level,
            strength = strength,
            wisdom = wisdom,
            skillAthletics = 1, // Proficient -> 3 (strMod) + 2 (pb) = 5
            skillPerception = 0, // Not proficient -> 2 (wisMod)
            customSkillBonuses = customBonuses
        )

        // Verify skill extra bonuses
        assertEquals(1, char.getSkillExtraBonus("skill_athletics"))
        assertEquals(2, char.getSkillExtraBonus("skill_perception"))
        assertEquals(0, char.getSkillExtraBonus("skill_stealth"))

        // Total athletics: 3 (strMod) + (1 * 2 pb) + 1 (extra) = 6
        val strMod = Rules5eCalculator.calculateModifier(char.strength)
        val athleticsTotal = strMod + (char.skillAthletics * pb) + char.getSkillExtraBonus("skill_athletics")
        assertEquals(6, athleticsTotal)

        // Passive perception: 10 + 2 (wisMod) + 0 (prof) + 2 (extra) = 14
        val passivePerception = Rules5eCalculator.calculatePassivePerception(
            char.wisdom,
            char.skillPerception,
            char.level,
            char.getSkillExtraBonus("skill_perception")
        )
        assertEquals(14, passivePerception)

        // Test JSON serialization & round-trip
        val jsonDto = CharacterMapper.domainToJson(char)
        assertEquals(1, jsonDto.customSkillBonuses["skill_athletics"])
        assertEquals(2, jsonDto.customSkillBonuses["skill_perception"])

        val jsonString = defaultJson.encodeToString(Character5eJson.serializer(), jsonDto)
        assertTrue(jsonString.contains("custom_skill_bonuses"))
        assertTrue(jsonString.contains("\"skill_athletics\": 1"))

        val reimportedDto = defaultJson.decodeFromString<Character5eJson>(jsonString)
        val reimportedChar = CharacterMapper.jsonToDomain(reimportedDto, id = 5)

        assertEquals(1, reimportedChar.getSkillExtraBonus("skill_athletics"))
        assertEquals(2, reimportedChar.getSkillExtraBonus("skill_perception"))
    }
}
