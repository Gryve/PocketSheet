package com.pocketsheet.data.model

import com.pocketsheet.data.model.serializers.FlexibleIntSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Character5eJson(
    @SerialName("character_name") val characterName: String = "",
    @SerialName("background") val background: String = "",
    @SerialName("class_name") val className: String = "",
    @SerialName("subclass") val subclass: String = "",
    @SerialName("species") val species: String = "",
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("level") val level: Int = 1,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("xp") val xp: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("armor_class") val armorClass: Int = 10,
    @SerialName("has_shield") val hasShield: Boolean = false,
    @SerialName("speed") val speed: String = "30 ft",
    @SerialName("size") val size: String = "Medium",
    @SerialName("initiative_override") val initiativeOverride: String = "",
    @SerialName("heroic_inspiration") val heroicInspiration: Boolean = false,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("hp_current") val hpCurrent: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("hp_max") val hpMax: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("hp_temp") val hpTemp: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("hit_dice_spent") val hitDiceSpent: Int = 0,
    @SerialName("hit_dice_max") val hitDiceMax: String = "1d8",
    @SerialName("death_save_successes") val deathSaveSuccesses: Int = 0,
    @SerialName("death_save_failures") val deathSaveFailures: Int = 0,
    
    // Ability Scores
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("strength") val strength: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("dexterity") val dexterity: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("constitution") val constitution: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("intelligence") val intelligence: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("wisdom") val wisdom: Int = 10,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("charisma") val charisma: Int = 10,
    
    // Saving Throw Proficiencies
    @SerialName("save_str_prof") val saveStrProf: Boolean = false,
    @SerialName("save_dex_prof") val saveDexProf: Boolean = false,
    @SerialName("save_con_prof") val saveConProf: Boolean = false,
    @SerialName("save_int_prof") val saveIntProf: Boolean = false,
    @SerialName("save_wis_prof") val saveWisProf: Boolean = false,
    @SerialName("save_cha_prof") val saveChaProf: Boolean = false,
    
    // Skill Proficiencies (0 = none, 1 = proficient, 2 = expertise)
    @SerialName("skill_athletics") val skillAthletics: Int = 0,
    @SerialName("skill_acrobatics") val skillAcrobatics: Int = 0,
    @SerialName("skill_sleight_of_hand") val skillSleightOfHand: Int = 0,
    @SerialName("skill_stealth") val skillStealth: Int = 0,
    @SerialName("skill_arcana") val skillArcana: Int = 0,
    @SerialName("skill_history") val skillHistory: Int = 0,
    @SerialName("skill_investigation") val skillInvestigation: Int = 0,
    @SerialName("skill_nature") val skillNature: Int = 0,
    @SerialName("skill_religion") val skillReligion: Int = 0,
    @SerialName("skill_animal_handling") val skillAnimalHandling: Int = 0,
    @SerialName("skill_insight") val skillInsight: Int = 0,
    @SerialName("skill_medicine") val skillMedicine: Int = 0,
    @SerialName("skill_perception") val skillPerception: Int = 0,
    @SerialName("skill_survival") val skillSurvival: Int = 0,
    @SerialName("skill_deception") val skillDeception: Int = 0,
    @SerialName("skill_intimidation") val skillIntimidation: Int = 0,
    @SerialName("skill_performance") val skillPerformance: Int = 0,
    @SerialName("skill_persuasion") val skillPersuasion: Int = 0,
    
    // Armor Proficiencies
    @SerialName("armor_light") val armorLight: Boolean = false,
    @SerialName("armor_medium") val armorMedium: Boolean = false,
    @SerialName("armor_heavy") val armorHeavy: Boolean = false,
    @SerialName("armor_shields") val armorShields: Boolean = false,
    
    // Tool, Weapon, Language Proficiencies
    @SerialName("weapons_proficiency") val weaponsProficiency: String = "",
    @SerialName("tools_proficiency") val toolsProficiency: String = "",
    @SerialName("languages") val languages: String = "",
    
    // Weapons / Attacks
    @SerialName("weapons") val weapons: List<Weapon5eJson> = emptyList(),
    
    // Features & Traits
    @SerialName("class_features") val classFeatures: String = "",
    @SerialName("species_traits") val speciesTraits: String = "",
    @SerialName("feats") val feats: String = "",
    
    // Spellcasting
    @SerialName("spellcasting_ability") val spellcastingAbility: String = "",
    @SerialName("spell_slots_total") val spellSlotsTotal: Map<String, Int> = emptyMap(),
    @SerialName("spell_slots_expended") val spellSlotsExpended: Map<String, Int> = emptyMap(),
    @SerialName("spells") val spells: List<Spell5eJson> = emptyList(),
    
    // Lore & RP
    @SerialName("appearance") val appearance: String = "",
    @SerialName("backstory") val backstory: String = "",
    @SerialName("alignment") val alignment: String = "",
    
    // Equipment & Currency
    @SerialName("equipment") val equipment: String = "",
    @SerialName("attunement_1") val attunement1: String = "",
    @SerialName("attunement_2") val attunement2: String = "",
    @SerialName("attunement_3") val attunement3: String = "",
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("cp") val cp: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("sp") val sp: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("ep") val ep: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("gp") val gp: Int = 0,
    @Serializable(with = FlexibleIntSerializer::class)
    @SerialName("pp") val pp: Int = 0
)

@Serializable
data class Weapon5eJson(
    @SerialName("name") val name: String = "",
    @SerialName("atk_bonus_override") val atkBonusOverride: String = "",
    @SerialName("damage_and_type") val damageAndType: String = "",
    @SerialName("notes") val notes: String = ""
)

@Serializable
data class Spell5eJson(
    @SerialName("level") val level: String = "0",
    @SerialName("name") val name: String = "",
    @SerialName("casting_time") val castingTime: String = "",
    @SerialName("range") val range: String = "",
    @SerialName("concentration") val concentration: Boolean = false,
    @SerialName("ritual") val ritual: Boolean = false,
    @SerialName("material") val material: Boolean = false,
    @SerialName("notes") val notes: String = ""
)
