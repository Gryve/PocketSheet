package com.pocketsheet.data.mapper

import com.pocketsheet.data.local.entity.CharacterEntity
import com.pocketsheet.data.model.Character5eJson
import com.pocketsheet.data.model.Spell5eJson
import com.pocketsheet.data.model.Weapon5eJson
import com.pocketsheet.domain.model.Character
import com.pocketsheet.domain.model.Spell
import com.pocketsheet.domain.model.Weapon
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val defaultJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
    encodeDefaults = true
    prettyPrint = true
}

object CharacterMapper {

    fun jsonToDomain(json: Character5eJson, id: Long = 0, customImageUri: String? = null): Character {
        return Character(
            id = id,
            characterName = json.characterName,
            background = json.background,
            className = json.className,
            subclass = json.subclass,
            species = json.species,
            level = json.level,
            xp = json.xp,
            armorClass = json.armorClass,
            hasShield = json.hasShield,
            speed = json.speed,
            size = json.size,
            initiativeOverride = json.initiativeOverride,
            heroicInspiration = json.heroicInspiration,
            hpCurrent = json.hpCurrent,
            hpMax = json.hpMax,
            hpTemp = json.hpTemp,
            hitDiceSpent = json.hitDiceSpent,
            hitDiceMax = json.hitDiceMax,
            deathSaveSuccesses = json.deathSaveSuccesses,
            deathSaveFailures = json.deathSaveFailures,
            customImageUri = customImageUri,
            strength = json.strength,
            dexterity = json.dexterity,
            constitution = json.constitution,
            intelligence = json.intelligence,
            wisdom = json.wisdom,
            charisma = json.charisma,
            saveStrProf = json.saveStrProf,
            saveDexProf = json.saveDexProf,
            saveConProf = json.saveConProf,
            saveIntProf = json.saveIntProf,
            saveWisProf = json.saveWisProf,
            saveChaProf = json.saveChaProf,
            skillAthletics = json.skillAthletics,
            skillAcrobatics = json.skillAcrobatics,
            skillSleightOfHand = json.skillSleightOfHand,
            skillStealth = json.skillStealth,
            skillArcana = json.skillArcana,
            skillHistory = json.skillHistory,
            skillInvestigation = json.skillInvestigation,
            skillNature = json.skillNature,
            skillReligion = json.skillReligion,
            skillAnimalHandling = json.skillAnimalHandling,
            skillInsight = json.skillInsight,
            skillMedicine = json.skillMedicine,
            skillPerception = json.skillPerception,
            skillSurvival = json.skillSurvival,
            skillDeception = json.skillDeception,
            skillIntimidation = json.skillIntimidation,
            skillPerformance = json.skillPerformance,
            skillPersuasion = json.skillPersuasion,
            customSkillBonuses = json.customSkillBonuses,
            armorLight = json.armorLight,
            armorMedium = json.armorMedium,
            armorHeavy = json.armorHeavy,
            armorShields = json.armorShields,
            weaponsProficiency = json.weaponsProficiency,
            toolsProficiency = json.toolsProficiency,
            languages = json.languages,
            weapons = json.weapons.map { Weapon(it.name, it.atkBonusOverride, it.damageAndType, it.notes) },
            classFeatures = json.classFeatures,
            speciesTraits = json.speciesTraits,
            feats = json.feats,
            spellcastingAbility = json.spellcastingAbility,
            spellSlotsTotal = json.spellSlotsTotal,
            spellSlotsExpended = json.spellSlotsExpended,
            spells = json.spells.map {
                Spell(
                    level = it.level,
                    name = it.name,
                    castingTime = it.castingTime,
                    range = it.range,
                    concentration = it.concentration,
                    ritual = it.ritual,
                    material = it.material,
                    notes = it.notes
                )
            },
            appearance = json.appearance,
            backstory = json.backstory,
            alignment = json.alignment,
            equipment = json.equipment,
            attunement1 = json.attunement1,
            attunement2 = json.attunement2,
            attunement3 = json.attunement3,
            cp = json.cp,
            sp = json.sp,
            ep = json.ep,
            gp = json.gp,
            pp = json.pp
        )
    }

    fun domainToJson(domain: Character): Character5eJson {
        return Character5eJson(
            characterName = domain.characterName,
            background = domain.background,
            className = domain.className,
            subclass = domain.subclass,
            species = domain.species,
            level = domain.level,
            xp = domain.xp,
            armorClass = domain.armorClass,
            hasShield = domain.hasShield,
            speed = domain.speed,
            size = domain.size,
            initiativeOverride = domain.initiativeOverride,
            heroicInspiration = domain.heroicInspiration,
            hpCurrent = domain.hpCurrent,
            hpMax = domain.hpMax,
            hpTemp = domain.hpTemp,
            hitDiceSpent = domain.hitDiceSpent,
            hitDiceMax = domain.hitDiceMax,
            deathSaveSuccesses = domain.deathSaveSuccesses,
            deathSaveFailures = domain.deathSaveFailures,
            strength = domain.strength,
            dexterity = domain.dexterity,
            constitution = domain.constitution,
            intelligence = domain.intelligence,
            wisdom = domain.wisdom,
            charisma = domain.charisma,
            saveStrProf = domain.saveStrProf,
            saveDexProf = domain.saveDexProf,
            saveConProf = domain.saveConProf,
            saveIntProf = domain.saveIntProf,
            saveWisProf = domain.saveWisProf,
            saveChaProf = domain.saveChaProf,
            skillAthletics = domain.skillAthletics,
            skillAcrobatics = domain.skillAcrobatics,
            skillSleightOfHand = domain.skillSleightOfHand,
            skillStealth = domain.skillStealth,
            skillArcana = domain.skillArcana,
            skillHistory = domain.skillHistory,
            skillInvestigation = domain.skillInvestigation,
            skillNature = domain.skillNature,
            skillReligion = domain.skillReligion,
            skillAnimalHandling = domain.skillAnimalHandling,
            skillInsight = domain.skillInsight,
            skillMedicine = domain.skillMedicine,
            skillPerception = domain.skillPerception,
            skillSurvival = domain.skillSurvival,
            skillDeception = domain.skillDeception,
            skillIntimidation = domain.skillIntimidation,
            skillPerformance = domain.skillPerformance,
            skillPersuasion = domain.skillPersuasion,
            customSkillBonuses = domain.customSkillBonuses,
            armorLight = domain.armorLight,
            armorMedium = domain.armorMedium,
            armorHeavy = domain.armorHeavy,
            armorShields = domain.armorShields,
            weaponsProficiency = domain.weaponsProficiency,
            toolsProficiency = domain.toolsProficiency,
            languages = domain.languages,
            weapons = domain.weapons.map { Weapon5eJson(it.name, it.atkBonusOverride, it.damageAndType, it.notes) },
            classFeatures = domain.classFeatures,
            speciesTraits = domain.speciesTraits,
            feats = domain.feats,
            spellcastingAbility = domain.spellcastingAbility,
            spellSlotsTotal = domain.spellSlotsTotal,
            spellSlotsExpended = domain.spellSlotsExpended,
            spells = domain.spells.map {
                Spell5eJson(
                    level = it.level,
                    name = it.name,
                    castingTime = it.castingTime,
                    range = it.range,
                    concentration = it.concentration,
                    ritual = it.ritual,
                    material = it.material,
                    notes = it.notes
                )
            },
            appearance = domain.appearance,
            backstory = domain.backstory,
            alignment = domain.alignment,
            equipment = domain.equipment,
            attunement1 = domain.attunement1,
            attunement2 = domain.attunement2,
            attunement3 = domain.attunement3,
            cp = domain.cp,
            sp = domain.sp,
            ep = domain.ep,
            gp = domain.gp,
            pp = domain.pp
        )
    }

    fun domainToEntity(domain: Character): CharacterEntity {
        return CharacterEntity(
            id = domain.id,
            characterName = domain.characterName,
            background = domain.background,
            className = domain.className,
            subclass = domain.subclass,
            species = domain.species,
            level = domain.level,
            xp = domain.xp,
            armorClass = domain.armorClass,
            hasShield = domain.hasShield,
            speed = domain.speed,
            size = domain.size,
            initiativeOverride = domain.initiativeOverride,
            heroicInspiration = domain.heroicInspiration,
            hpCurrent = domain.hpCurrent,
            hpMax = domain.hpMax,
            hpTemp = domain.hpTemp,
            hitDiceSpent = domain.hitDiceSpent,
            hitDiceMax = domain.hitDiceMax,
            deathSaveSuccesses = domain.deathSaveSuccesses,
            deathSaveFailures = domain.deathSaveFailures,
            customImageUri = domain.customImageUri,
            strength = domain.strength,
            dexterity = domain.dexterity,
            constitution = domain.constitution,
            intelligence = domain.intelligence,
            wisdom = domain.wisdom,
            charisma = domain.charisma,
            saveStrProf = domain.saveStrProf,
            saveDexProf = domain.saveDexProf,
            saveConProf = domain.saveConProf,
            saveIntProf = domain.saveIntProf,
            saveWisProf = domain.saveWisProf,
            saveChaProf = domain.saveChaProf,
            skillAthletics = domain.skillAthletics,
            skillAcrobatics = domain.skillAcrobatics,
            skillSleightOfHand = domain.skillSleightOfHand,
            skillStealth = domain.skillStealth,
            skillArcana = domain.skillArcana,
            skillHistory = domain.skillHistory,
            skillInvestigation = domain.skillInvestigation,
            skillNature = domain.skillNature,
            skillReligion = domain.skillReligion,
            skillAnimalHandling = domain.skillAnimalHandling,
            skillInsight = domain.skillInsight,
            skillMedicine = domain.skillMedicine,
            skillPerception = domain.skillPerception,
            skillSurvival = domain.skillSurvival,
            skillDeception = domain.skillDeception,
            skillIntimidation = domain.skillIntimidation,
            skillPerformance = domain.skillPerformance,
            skillPersuasion = domain.skillPersuasion,
            customSkillBonusesJson = defaultJson.encodeToString(domain.customSkillBonuses),
            armorLight = domain.armorLight,
            armorMedium = domain.armorMedium,
            armorHeavy = domain.armorHeavy,
            armorShields = domain.armorShields,
            weaponsProficiency = domain.weaponsProficiency,
            toolsProficiency = domain.toolsProficiency,
            languages = domain.languages,
            weaponsJson = defaultJson.encodeToString(domain.weapons.map { Weapon5eJson(it.name, it.atkBonusOverride, it.damageAndType, it.notes) }),
            spellsJson = defaultJson.encodeToString(domain.spells.map {
                Spell5eJson(it.level, it.name, it.castingTime, it.range, it.concentration, it.ritual, it.material, it.notes)
            }),
            spellSlotsTotalJson = defaultJson.encodeToString(domain.spellSlotsTotal),
            spellSlotsExpendedJson = defaultJson.encodeToString(domain.spellSlotsExpended),
            classFeatures = domain.classFeatures,
            speciesTraits = domain.speciesTraits,
            feats = domain.feats,
            spellcastingAbility = domain.spellcastingAbility,
            appearance = domain.appearance,
            backstory = domain.backstory,
            alignment = domain.alignment,
            equipment = domain.equipment,
            attunement1 = domain.attunement1,
            attunement2 = domain.attunement2,
            attunement3 = domain.attunement3,
            cp = domain.cp,
            sp = domain.sp,
            ep = domain.ep,
            gp = domain.gp,
            pp = domain.pp,
            updatedAt = System.currentTimeMillis()
        )
    }

    fun entityToDomain(entity: CharacterEntity): Character {
        val weapons = try {
            defaultJson.decodeFromString<List<Weapon5eJson>>(entity.weaponsJson).map {
                Weapon(it.name, it.atkBonusOverride, it.damageAndType, it.notes)
            }
        } catch (_: Exception) {
            emptyList()
        }

        val spells = try {
            defaultJson.decodeFromString<List<Spell5eJson>>(entity.spellsJson).map {
                Spell(it.level, it.name, it.castingTime, it.range, it.concentration, it.ritual, it.material, it.notes)
            }
        } catch (_: Exception) {
            emptyList()
        }

        val spellSlotsTotal = try {
            defaultJson.decodeFromString<Map<String, Int>>(entity.spellSlotsTotalJson)
        } catch (_: Exception) {
            emptyMap()
        }

        val spellSlotsExpended = try {
            defaultJson.decodeFromString<Map<String, Int>>(entity.spellSlotsExpendedJson)
        } catch (_: Exception) {
            emptyMap()
        }

        val customSkillBonuses = try {
            defaultJson.decodeFromString<Map<String, Int>>(entity.customSkillBonusesJson)
        } catch (_: Exception) {
            emptyMap()
        }

        return Character(
            id = entity.id,
            characterName = entity.characterName,
            background = entity.background,
            className = entity.className,
            subclass = entity.subclass,
            species = entity.species,
            level = entity.level,
            xp = entity.xp,
            armorClass = entity.armorClass,
            hasShield = entity.hasShield,
            speed = entity.speed,
            size = entity.size,
            initiativeOverride = entity.initiativeOverride,
            heroicInspiration = entity.heroicInspiration,
            hpCurrent = entity.hpCurrent,
            hpMax = entity.hpMax,
            hpTemp = entity.hpTemp,
            hitDiceSpent = entity.hitDiceSpent,
            hitDiceMax = entity.hitDiceMax,
            deathSaveSuccesses = entity.deathSaveSuccesses,
            deathSaveFailures = entity.deathSaveFailures,
            customImageUri = entity.customImageUri,
            strength = entity.strength,
            dexterity = entity.dexterity,
            constitution = entity.constitution,
            intelligence = entity.intelligence,
            wisdom = entity.wisdom,
            charisma = entity.charisma,
            saveStrProf = entity.saveStrProf,
            saveDexProf = entity.saveDexProf,
            saveConProf = entity.saveConProf,
            saveIntProf = entity.saveIntProf,
            saveWisProf = entity.saveWisProf,
            saveChaProf = entity.saveChaProf,
            skillAthletics = entity.skillAthletics,
            skillAcrobatics = entity.skillAcrobatics,
            skillSleightOfHand = entity.skillSleightOfHand,
            skillStealth = entity.skillStealth,
            skillArcana = entity.skillArcana,
            skillHistory = entity.skillHistory,
            skillInvestigation = entity.skillInvestigation,
            skillNature = entity.skillNature,
            skillReligion = entity.skillReligion,
            skillAnimalHandling = entity.skillAnimalHandling,
            skillInsight = entity.skillInsight,
            skillMedicine = entity.skillMedicine,
            skillPerception = entity.skillPerception,
            skillSurvival = entity.skillSurvival,
            skillDeception = entity.skillDeception,
            skillIntimidation = entity.skillIntimidation,
            skillPerformance = entity.skillPerformance,
            skillPersuasion = entity.skillPersuasion,
            customSkillBonuses = customSkillBonuses,
            armorLight = entity.armorLight,
            armorMedium = entity.armorMedium,
            armorHeavy = entity.armorHeavy,
            armorShields = entity.armorShields,
            weaponsProficiency = entity.weaponsProficiency,
            toolsProficiency = entity.toolsProficiency,
            languages = entity.languages,
            weapons = weapons,
            classFeatures = entity.classFeatures,
            speciesTraits = entity.speciesTraits,
            feats = entity.feats,
            spellcastingAbility = entity.spellcastingAbility,
            spellSlotsTotal = spellSlotsTotal,
            spellSlotsExpended = spellSlotsExpended,
            spells = spells,
            appearance = entity.appearance,
            backstory = entity.backstory,
            alignment = entity.alignment,
            equipment = entity.equipment,
            attunement1 = entity.attunement1,
            attunement2 = entity.attunement2,
            attunement3 = entity.attunement3,
            cp = entity.cp,
            sp = entity.sp,
            ep = entity.ep,
            gp = entity.gp,
            pp = entity.pp
        )
    }
}
