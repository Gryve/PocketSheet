package com.pocketsheet.ui.overview

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketsheet.domain.model.Character
import com.pocketsheet.domain.model.Spell
import com.pocketsheet.domain.model.Weapon
import com.pocketsheet.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class OverviewViewModel(
    private val characterId: Long,
    private val repository: CharacterRepository
) : ViewModel() {

    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getCharacterById(characterId).collect {
                _character.value = it
            }
        }
    }

    fun setCustomImage(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val avatarsDir = File(context.filesDir, "avatars").apply { mkdirs() }
                val targetFile = File(avatarsDir, "char_${characterId}.jpg")

                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(targetFile).use { output ->
                        input.copyTo(output)
                    }
                }

                val localUri = targetFile.toURI().toString()
                repository.updateCharacterImage(characterId, localUri)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateBasicInfo(
        name: String,
        className: String,
        subclass: String,
        species: String,
        level: Int,
        background: String,
        alignment: String,
        armorClass: Int,
        hpMax: Int,
        speed: String,
        hitDiceMax: String
    ) {
        val current = _character.value ?: return
        val safeLevel = level.coerceIn(1, 20)
        val updated = current.copy(
            characterName = name,
            className = className,
            subclass = subclass,
            species = species,
            level = safeLevel,
            background = background,
            alignment = alignment,
            armorClass = armorClass,
            hpMax = hpMax,
            hpCurrent = current.hpCurrent.coerceAtMost(hpMax),
            speed = speed,
            hitDiceMax = hitDiceMax
        )
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateAbilityScore(abilityCode: String, newScore: Int) {
        val current = _character.value ?: return
        val safeScore = newScore.coerceIn(1, 30)
        val updated = when (abilityCode.uppercase()) {
            "FOR", "STR" -> current.copy(strength = safeScore)
            "DES", "DEX" -> current.copy(dexterity = safeScore)
            "COS", "CON" -> current.copy(constitution = safeScore)
            "INT" -> current.copy(intelligence = safeScore)
            "SAG", "WIS" -> current.copy(wisdom = safeScore)
            "CAR", "CHA" -> current.copy(charisma = safeScore)
            else -> current
        }
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun toggleSaveProficiency(saveName: String) {
        val current = _character.value ?: return
        val updated = when (saveName.lowercase()) {
            "forza", "str" -> current.copy(saveStrProf = !current.saveStrProf)
            "destrezza", "dex" -> current.copy(saveDexProf = !current.saveDexProf)
            "costituzione", "con" -> current.copy(saveConProf = !current.saveConProf)
            "intelligenza", "int" -> current.copy(saveIntProf = !current.saveIntProf)
            "saggezza", "wis" -> current.copy(saveWisProf = !current.saveWisProf)
            "carisma", "cha" -> current.copy(saveChaProf = !current.saveChaProf)
            else -> current
        }
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun cycleSkillProficiency(skillName: String) {
        val current = _character.value ?: return
        fun nextVal(cur: Int) = (cur + 1) % 3 // 0 -> 1 -> 2 -> 0

        val updated = when (skillName.lowercase().trim()) {
            "acrobazia" -> current.copy(skillAcrobatics = nextVal(current.skillAcrobatics))
            "addestrare animali" -> current.copy(skillAnimalHandling = nextVal(current.skillAnimalHandling))
            "arcano" -> current.copy(skillArcana = nextVal(current.skillArcana))
            "atletica" -> current.copy(skillAthletics = nextVal(current.skillAthletics))
            "furtività" -> current.copy(skillStealth = nextVal(current.skillStealth))
            "indagare" -> current.copy(skillInvestigation = nextVal(current.skillInvestigation))
            "inganno" -> current.copy(skillDeception = nextVal(current.skillDeception))
            "intimidire" -> current.copy(skillIntimidation = nextVal(current.skillIntimidation))
            "intrattenere" -> current.copy(skillPerformance = nextVal(current.skillPerformance))
            "intuizione" -> current.copy(skillInsight = nextVal(current.skillInsight))
            "medicina" -> current.copy(skillMedicine = nextVal(current.skillMedicine))
            "natura" -> current.copy(skillNature = nextVal(current.skillNature))
            "percezione" -> current.copy(skillPerception = nextVal(current.skillPerception))
            "persuasione" -> current.copy(skillPersuasion = nextVal(current.skillPersuasion))
            "rapidità di mano" -> current.copy(skillSleightOfHand = nextVal(current.skillSleightOfHand))
            "religione" -> current.copy(skillReligion = nextVal(current.skillReligion))
            "sopravvivenza" -> current.copy(skillSurvival = nextVal(current.skillSurvival))
            "storia" -> current.copy(skillHistory = nextVal(current.skillHistory))
            else -> current
        }
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateCombatMetrics(ac: Int, initiative: String, speed: String, hitDiceSpent: Int) {
        val current = _character.value ?: return
        val updated = current.copy(
            armorClass = ac,
            initiativeOverride = initiative,
            speed = speed,
            hitDiceSpent = hitDiceSpent
        )
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun addWeapon(weapon: Weapon) {
        val current = _character.value ?: return
        val updated = current.copy(weapons = current.weapons + weapon)
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun deleteWeapon(index: Int) {
        val current = _character.value ?: return
        if (index in current.weapons.indices) {
            val list = current.weapons.toMutableList().apply { removeAt(index) }
            viewModelScope.launch {
                repository.updateCharacter(current.copy(weapons = list))
            }
        }
    }

    fun addSpell(spell: Spell) {
        val current = _character.value ?: return
        val updated = current.copy(spells = current.spells + spell)
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun deleteSpell(index: Int) {
        val current = _character.value ?: return
        if (index in current.spells.indices) {
            val list = current.spells.toMutableList().apply { removeAt(index) }
            viewModelScope.launch {
                repository.updateCharacter(current.copy(spells = list))
            }
        }
    }

    fun updateSpellSlotsTotal(levelStr: String, total: Int) {
        val current = _character.value ?: return
        val map = current.spellSlotsTotal.toMutableMap()
        map[levelStr] = total.coerceAtLeast(0)
        viewModelScope.launch {
            repository.updateCharacter(current.copy(spellSlotsTotal = map))
        }
    }

    fun updateLore(
        classFeatures: String,
        speciesTraits: String,
        feats: String,
        appearance: String,
        backstory: String
    ) {
        val current = _character.value ?: return
        val updated = current.copy(
            classFeatures = classFeatures,
            speciesTraits = speciesTraits,
            feats = feats,
            appearance = appearance,
            backstory = backstory
        )
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateAttunement(slotIndex: Int, itemName: String) {
        val current = _character.value ?: return
        val updated = when (slotIndex) {
            1 -> current.copy(attunement1 = itemName)
            2 -> current.copy(attunement2 = itemName)
            3 -> current.copy(attunement3 = itemName)
            else -> current
        }
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateEquipment(equipment: String) {
        val current = _character.value ?: return
        val updated = current.copy(equipment = equipment)
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateHp(newCurrent: Int) {
        val current = _character.value ?: return
        val clamped = newCurrent.coerceIn(0, current.hpMax + current.hpTemp)
        val updated = current.copy(hpCurrent = clamped)
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateTempHp(tempHp: Int) {
        val current = _character.value ?: return
        val updated = current.copy(hpTemp = tempHp.coerceAtLeast(0))
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun toggleSpellSlot(levelStr: String, slotIndex: Int) {
        val current = _character.value ?: return
        val total = current.spellSlotsTotal[levelStr] ?: 0
        val expended = current.spellSlotsExpended[levelStr] ?: 0

        val newExpended = if (slotIndex < expended) {
            (expended - 1).coerceAtLeast(0)
        } else {
            (expended + 1).coerceAtMost(total)
        }

        val updatedMap = current.spellSlotsExpended.toMutableMap()
        updatedMap[levelStr] = newExpended
        val updated = current.copy(spellSlotsExpended = updatedMap)

        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateDeathSaves(successes: Int, failures: Int) {
        val current = _character.value ?: return
        val updated = current.copy(
            deathSaveSuccesses = successes.coerceIn(0, 3),
            deathSaveFailures = failures.coerceIn(0, 3)
        )
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateCurrencies(cp: Int, sp: Int, ep: Int, gp: Int, pp: Int) {
        val current = _character.value ?: return
        val updated = current.copy(cp = cp, sp = sp, ep = ep, gp = gp, pp = pp)
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun updateCharacter(updated: Character) {
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    fun performLongRest() {
        val current = _character.value ?: return
        val resetSlots = current.spellSlotsExpended.keys.associateWith { 0 }
        val updated = current.copy(
            hpCurrent = current.hpMax,
            hpTemp = 0,
            hitDiceSpent = (current.hitDiceSpent - (current.level / 2).coerceAtLeast(1)).coerceAtLeast(0),
            deathSaveSuccesses = 0,
            deathSaveFailures = 0,
            spellSlotsExpended = resetSlots
        )
        viewModelScope.launch {
            repository.updateCharacter(updated)
        }
    }

    suspend fun exportJson(): String? {
        val current = _character.value ?: return null
        return repository.exportCharacterToJson(current)
    }
}
