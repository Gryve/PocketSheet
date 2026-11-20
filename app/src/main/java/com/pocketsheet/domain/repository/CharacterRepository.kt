package com.pocketsheet.domain.repository

import com.pocketsheet.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getAllCharacters(): Flow<List<Character>>
    fun getCharacterById(id: Long): Flow<Character?>
    suspend fun getCharacterByIdOnce(id: Long): Character?
    suspend fun importCharacterFromJson(jsonString: String): Result<Character>
    suspend fun exportCharacterToJson(character: Character): String
    suspend fun saveCharacter(character: Character): Long
    suspend fun updateCharacter(character: Character)
    suspend fun updateCharacterImage(id: Long, imageUri: String?)
    suspend fun deleteCharacter(id: Long)
}
