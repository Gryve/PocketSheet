package com.pocketsheet.ui.home

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketsheet.domain.model.Character
import com.pocketsheet.domain.repository.CharacterRepository
import com.pocketsheet.domain.rules.Rules5eCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    val characters: StateFlow<List<Character>> = repository.getAllCharacters()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun importJsonFromUri(context: Context, uri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val jsonString = context.contentResolver.openInputStream(uri)?.use { stream ->
                    stream.bufferedReader().use { it.readText() }
                } ?: run {
                    onResult(false, "Impossibile leggere il file selezionato.")
                    return@launch
                }

                val result = repository.importCharacterFromJson(jsonString)
                if (result.isSuccess) {
                    val character = result.getOrNull()
                    onResult(true, "Personaggio '${character?.characterName}' importato con successo!")
                } else {
                    onResult(false, "Errore durante l'importazione: ${result.exceptionOrNull()?.localizedMessage}")
                }
            } catch (e: Exception) {
                onResult(false, "Errore durante l'apertura del file: ${e.localizedMessage}")
            }
        }
    }

    /**
     * Creates a new character from scratch with automatic initial 5e calculations
     */
    fun createCharacter(
        name: String,
        className: String,
        subclass: String = "",
        species: String,
        level: Int = 1,
        strength: Int = 10,
        dexterity: Int = 10,
        constitution: Int = 10,
        intelligence: Int = 10,
        wisdom: Int = 10,
        charisma: Int = 10,
        background: String = "",
        alignment: String = "",
        onCreated: (Long) -> Unit
    ) {
        viewModelScope.launch {
            val safeLevel = level.coerceIn(1, 20)
            val conMod = Rules5eCalculator.calculateModifier(constitution)
            val dexMod = Rules5eCalculator.calculateModifier(dexterity)

            // Calculate standard base HP based on class hit die + Con modifier
            val hitDie = when (className.lowercase().trim()) {
                "barbarian", "barbaro" -> 12
                "fighter", "guerriero", "paladin", "paladino", "ranger" -> 10
                "sorcerer", "stregone", "wizard", "mago" -> 6
                else -> 8 // Bard, Cleric, Druid, Monk, Rogue, Warlock
            }

            val level1Hp = hitDie + conMod
            val extraLevelsHp = if (safeLevel > 1) (safeLevel - 1) * ((hitDie / 2 + 1) + conMod) else 0
            val totalMaxHp = (level1Hp + extraLevelsHp).coerceAtLeast(1)

            val baseAc = 10 + dexMod

            val newCharacter = Character(
                characterName = name.ifBlank { "Nuovo Avventuriero" },
                className = className,
                subclass = subclass,
                species = species,
                level = safeLevel,
                xp = 0,
                armorClass = baseAc,
                speed = "30 ft",
                hpCurrent = totalMaxHp,
                hpMax = totalMaxHp,
                hitDiceSpent = 0,
                hitDiceMax = "${safeLevel}d$hitDie",
                strength = strength,
                dexterity = dexterity,
                constitution = constitution,
                intelligence = intelligence,
                wisdom = wisdom,
                charisma = charisma,
                background = background,
                alignment = alignment,
                // Default spell slots for level 1 caster if applicable
                spellSlotsTotal = mapOf(
                    "1" to (if (safeLevel >= 1 && isSpellcaster(className)) 2 else 0),
                    "2" to (if (safeLevel >= 3 && isSpellcaster(className)) 2 else 0),
                    "3" to 0, "4" to 0, "5" to 0, "6" to 0, "7" to 0, "8" to 0, "9" to 0
                ),
                spellSlotsExpended = (1..9).associate { "$it" to 0 },
                gp = 10
            )

            val newId = repository.saveCharacter(newCharacter)
            onCreated(newId)
        }
    }

    private fun isSpellcaster(className: String): Boolean {
        val lower = className.lowercase().trim()
        return lower in listOf(
            "cleric", "chierico", "wizard", "mago", "sorcerer", "stregone",
            "druid", "druido", "bard", "bardo", "warlock", "paladin", "paladino", "ranger"
        )
    }

    fun deleteCharacter(id: Long) {
        viewModelScope.launch {
            repository.deleteCharacter(id)
        }
    }
}
