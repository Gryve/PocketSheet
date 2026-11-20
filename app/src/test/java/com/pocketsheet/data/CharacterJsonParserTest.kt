package com.pocketsheet.data

import com.pocketsheet.data.mapper.CharacterMapper
import com.pocketsheet.data.mapper.defaultJson
import com.pocketsheet.data.model.Character5eJson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class CharacterJsonParserTest {

    private val sampleJsonContent = """
{
  "character_name": "Elandor Dawnwhisper",
  "background": "Acolyte",
  "class_name": "Cleric",
  "subclass": "Life Domain",
  "species": "Elf (High)",
  "level": 3,
  "xp": "900",
  "armor_class": "18",
  "has_shield": true,
  "speed": "30 ft",
  "size": "Medium",
  "initiative_override": "",
  "heroic_inspiration": false,
  "hp_current": "24",
  "hp_max": "24",
  "hp_temp": "",
  "hit_dice_spent": "0",
  "hit_dice_max": "3d8",
  "death_save_successes": 0,
  "death_save_failures": 0,
  "strength": 14,
  "dexterity": 12,
  "constitution": 14,
  "intelligence": 10,
  "wisdom": 16,
  "charisma": 10,
  "save_str_prof": false,
  "save_dex_prof": false,
  "save_con_prof": false,
  "save_int_prof": false,
  "save_wis_prof": true,
  "save_cha_prof": true,
  "skill_athletics": 0,
  "skill_acrobatics": 0,
  "skill_sleight_of_hand": 0,
  "skill_stealth": 0,
  "skill_arcana": 0,
  "skill_history": 0,
  "skill_investigation": 0,
  "skill_nature": 0,
  "skill_religion": 1,
  "skill_animal_handling": 0,
  "skill_insight": 1,
  "skill_medicine": 2,
  "skill_perception": 1,
  "skill_survival": 0,
  "skill_deception": 0,
  "skill_intimidation": 0,
  "skill_performance": 0,
  "skill_persuasion": 0,
  "armor_light": true,
  "armor_medium": true,
  "armor_heavy": true,
  "armor_shields": true,
  "weapons_proficiency": "Simple weapons, Morningstar, War Pick",
  "tools_proficiency": "Herbalism Kit, Calligrapher's Supplies",
  "weapons": [
    {
      "name": "Mace",
      "atk_bonus_override": "+4",
      "damage_and_type": "1d6+2 Bludgeoning",
      "notes": "Simple, Melee"
    },
    {
      "name": "Light Crossbow",
      "atk_bonus_override": "+3",
      "damage_and_type": "1d8+1 Piercing",
      "notes": "Range 80/320, Loading"
    },
    {
      "name": "Sacred Flame",
      "atk_bonus_override": "DC 13 Dex",
      "damage_and_type": "1d8 Radiant",
      "notes": "60 ft, Ignores cover"
    }
  ],
  "class_features": "• Spellcasting (Cleric)\n• Channel Divinity (2/Rest): Turn Undead, Preserve Life\n• Disciple of Life (Healing spells heal an extra 2 + spell level HP)\n• Divine Order: Protector (Heavy Armor & Martial Weapons)",
  "species_traits": "• Darkvision 60 ft\n• Fey Ancestry: Adv. vs Charmed, immune to magical sleep\n• Trance: 4 hours long rest\n• Keen Senses: Proficiency in Perception\n• High Elf Lineage: Prestidigitation cantrip, Detect Magic 1/day",
  "feats": "• Magic Initiate (Cleric)\n• War Caster (Advantage on Con saves to maintain concentration)",
  "spellcasting_ability": "Wisdom",
  "spell_slots_total": {
    "1": 4,
    "2": 2,
    "3": 0,
    "4": 0,
    "5": 0,
    "6": 0,
    "7": 0,
    "8": 0,
    "9": 0
  },
  "spell_slots_expended": {
    "1": 1,
    "2": 0,
    "3": 0,
    "4": 0,
    "5": 0,
    "6": 0,
    "7": 0,
    "8": 0,
    "9": 0
  },
  "spells": [
    {
      "level": "0",
      "name": "Sacred Flame",
      "casting_time": "1 Action",
      "range": "60 ft",
      "concentration": false,
      "ritual": false,
      "material": false,
      "notes": "1d8 Radiant, Dex Save"
    },
    {
      "level": "0",
      "name": "Guidance",
      "casting_time": "1 Action",
      "range": "Touch",
      "concentration": true,
      "ritual": false,
      "material": false,
      "notes": "+1d4 to one ability check"
    },
    {
      "level": "1",
      "name": "Bless",
      "casting_time": "1 Action",
      "range": "30 ft",
      "concentration": true,
      "ritual": false,
      "material": true,
      "notes": "+1d4 to attacks and saves for 3 allies"
    }
  ],
  "appearance": "Tall silver-haired elf with luminous amber eyes, wearing polished chain mail adorned with sunburst emblems.",
  "backstory": "Raised in the temple of the Dawnfather, Elandor travels the realms to bring light into darkness and mend the wounded.",
  "alignment": "Neutral Good",
  "languages": "Common, Elvish, Celestial",
  "equipment": "Mace, Chain Mail, Shield (Holy Symbol), Light Crossbow + 20 bolts, Priest's Pack, Healer's Kit",
  "attunement_1": "Amulet of the Devout (+1)",
  "attunement_2": "Ring of Protection (+1 AC/Saves)",
  "attunement_3": "",
  "cp": "15",
  "sp": "40",
  "ep": "0",
  "gp": "125",
  "pp": "0"
}
    """.trimIndent()

    @Test
    fun testParseSampleJson() {
        val dto = defaultJson.decodeFromString<Character5eJson>(sampleJsonContent)
        assertEquals("Elandor Dawnwhisper", dto.characterName)
        assertEquals("Cleric", dto.className)
        assertEquals("Life Domain", dto.subclass)
        assertEquals("Elf (High)", dto.species)
        assertEquals(3, dto.level)
        assertEquals(900, dto.xp) // parsed from "900"
        assertEquals(18, dto.armorClass) // parsed from "18"
        assertTrue(dto.hasShield)
        assertEquals(24, dto.hpCurrent) // parsed from "24"
        assertEquals(24, dto.hpMax) // parsed from "24"
        assertEquals(0, dto.hpTemp) // parsed from "" -> 0 default
        assertEquals(14, dto.strength)
        assertEquals(16, dto.wisdom)
        assertTrue(dto.saveWisProf)
        assertTrue(dto.saveChaProf)
        assertEquals(2, dto.skillMedicine) // Expertise
        assertEquals(1, dto.skillReligion) // Proficient
        assertEquals(0, dto.skillAthletics) // None
        assertEquals(3, dto.weapons.size)
        assertEquals("Mace", dto.weapons[0].name)
        assertEquals(3, dto.spells.size)
        assertEquals("Wisdom", dto.spellcastingAbility)
        assertEquals(4, dto.spellSlotsTotal["1"])
        assertEquals(1, dto.spellSlotsExpended["1"])
        assertEquals(15, dto.cp)
        assertEquals(125, dto.gp)
    }

    @Test
    fun testRoundTripDomainAndJson() {
        val dto = defaultJson.decodeFromString<Character5eJson>(sampleJsonContent)
        val domain = CharacterMapper.jsonToDomain(dto, id = 42, customImageUri = "file:///dummy/avatar.png")

        assertEquals(42L, domain.id)
        assertEquals("file:///dummy/avatar.png", domain.customImageUri)
        assertEquals("Elandor Dawnwhisper", domain.characterName)
        assertEquals(18, domain.armorClass)
        assertEquals(3, domain.weapons.size)

        // Convert back to JSON DTO
        val backToDto = CharacterMapper.domainToJson(domain)
        assertEquals(domain.characterName, backToDto.characterName)
        assertEquals(domain.armorClass, backToDto.armorClass)
        assertEquals(domain.weapons.size, backToDto.weapons.size)
    }

    @Test
    fun testParseDirectFileIfExists() {
        val file = File("d:/documenti/coding/PocketSheet/Elandor_Dawnwhisper_5e.json")
        if (file.exists()) {
            val content = file.readText()
            val dto = defaultJson.decodeFromString<Character5eJson>(content)
            assertEquals("Elandor Dawnwhisper", dto.characterName)
            assertEquals(10, dto.spells.size)
            assertEquals(3, dto.weapons.size)
        }
    }
}
