package com.pocketsheet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val characterName: String,
    val background: String = "",
    val className: String = "",
    val subclass: String = "",
    val species: String = "",
    val level: Int = 1,
    val xp: Int = 0,
    val armorClass: Int = 10,
    val hasShield: Boolean = false,
    val speed: String = "30 ft",
    val size: String = "Medium",
    val initiativeOverride: String = "",
    val heroicInspiration: Boolean = false,
    val hpCurrent: Int = 10,
    val hpMax: Int = 10,
    val hpTemp: Int = 0,
    val hitDiceSpent: Int = 0,
    val hitDiceMax: String = "1d8",
    val deathSaveSuccesses: Int = 0,
    val deathSaveFailures: Int = 0,
    val customImageUri: String? = null,

    // Ability Scores
    val strength: Int = 10,
    val dexterity: Int = 10,
    val constitution: Int = 10,
    val intelligence: Int = 10,
    val wisdom: Int = 10,
    val charisma: Int = 10,

    // Saving Throws
    val saveStrProf: Boolean = false,
    val saveDexProf: Boolean = false,
    val saveConProf: Boolean = false,
    val saveIntProf: Boolean = false,
    val saveWisProf: Boolean = false,
    val saveChaProf: Boolean = false,

    // Skills
    val skillAthletics: Int = 0,
    val skillAcrobatics: Int = 0,
    val skillSleightOfHand: Int = 0,
    val skillStealth: Int = 0,
    val skillArcana: Int = 0,
    val skillHistory: Int = 0,
    val skillInvestigation: Int = 0,
    val skillNature: Int = 0,
    val skillReligion: Int = 0,
    val skillAnimalHandling: Int = 0,
    val skillInsight: Int = 0,
    val skillMedicine: Int = 0,
    val skillPerception: Int = 0,
    val skillSurvival: Int = 0,
    val skillDeception: Int = 0,
    val skillIntimidation: Int = 0,
    val skillPerformance: Int = 0,
    val skillPersuasion: Int = 0,

    // Armor Proficiencies
    val armorLight: Boolean = false,
    val armorMedium: Boolean = false,
    val armorHeavy: Boolean = false,
    val armorShields: Boolean = false,

    // Proficiencies & Languages
    val weaponsProficiency: String = "",
    val toolsProficiency: String = "",
    val languages: String = "",

    // JSON encoded complex structures
    val weaponsJson: String = "[]",
    val spellsJson: String = "[]",
    val spellSlotsTotalJson: String = "{}",
    val spellSlotsExpendedJson: String = "{}",

    // Features
    val classFeatures: String = "",
    val speciesTraits: String = "",
    val feats: String = "",

    // Spellcasting
    val spellcastingAbility: String = "",

    // Lore
    val appearance: String = "",
    val backstory: String = "",
    val alignment: String = "",

    // Equipment & Currency
    val equipment: String = "",
    val attunement1: String = "",
    val attunement2: String = "",
    val attunement3: String = "",
    val cp: Int = 0,
    val sp: Int = 0,
    val ep: Int = 0,
    val gp: Int = 0,
    val pp: Int = 0,
    val updatedAt: Long = System.currentTimeMillis()
)
