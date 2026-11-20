package com.pocketsheet.data.repository

import com.pocketsheet.data.local.CharacterDao
import com.pocketsheet.data.mapper.CharacterMapper
import com.pocketsheet.data.mapper.defaultJson
import com.pocketsheet.data.model.Character5eJson
import com.pocketsheet.domain.model.Character
import com.pocketsheet.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString

class CharacterRepositoryImpl(
    private val characterDao: CharacterDao
) : CharacterRepository {

    override fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map { list ->
            list.map { CharacterMapper.entityToDomain(it) }
        }
    }

    override fun getCharacterById(id: Long): Flow<Character?> {
        return characterDao.getCharacterById(id).map { entity ->
            entity?.let { CharacterMapper.entityToDomain(it) }
        }
    }

    override suspend fun getCharacterByIdOnce(id: Long): Character? {
        return characterDao.getCharacterByIdOnce(id)?.let {
            CharacterMapper.entityToDomain(it)
        }
    }

    override suspend fun importCharacterFromJson(jsonString: String): Result<Character> {
        return try {
            val jsonDto = defaultJson.decodeFromString<Character5eJson>(jsonString)
            val domainCharacter = CharacterMapper.jsonToDomain(jsonDto)
            val entity = CharacterMapper.domainToEntity(domainCharacter)
            val newId = characterDao.insertCharacter(entity)
            Result.success(domainCharacter.copy(id = newId))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun exportCharacterToJson(character: Character): String {
        val jsonDto = CharacterMapper.domainToJson(character)
        return defaultJson.encodeToString(jsonDto)
    }

    override suspend fun saveCharacter(character: Character): Long {
        val entity = CharacterMapper.domainToEntity(character)
        return characterDao.insertCharacter(entity)
    }

    override suspend fun updateCharacter(character: Character) {
        val entity = CharacterMapper.domainToEntity(character)
        characterDao.updateCharacter(entity)
    }

    override suspend fun updateCharacterImage(id: Long, imageUri: String?) {
        val existing = characterDao.getCharacterByIdOnce(id) ?: return
        val updated = existing.copy(customImageUri = imageUri, updatedAt = System.currentTimeMillis())
        characterDao.updateCharacter(updated)
    }

    override suspend fun deleteCharacter(id: Long) {
        characterDao.deleteCharacterById(id)
    }
}
