package com.pocketsheet.ui.stats

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketsheet.domain.rules.Rules5eCalculator
import com.pocketsheet.ui.overview.OverviewViewModel
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndCrimsonDark
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard
import com.pocketsheet.ui.theme.ParchmentHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    viewModel: OverviewViewModel,
    onBackClick: () -> Unit
) {
    val character by viewModel.character.collectAsState()
    val char = character ?: return

    val pb = Rules5eCalculator.calculateProficiencyBonus(char.level)
    val passivePerception = Rules5eCalculator.calculatePassivePerception(
        char.wisdom,
        char.skillPerception,
        char.level,
        char.getSkillExtraBonus("skill_perception")
    )

    var editingAbility by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var editingSkill by remember { mutableStateOf<SkillInfo?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CARATTERISTICHE & ABILITÀ",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 19.sp,
                        letterSpacing = 1.sp,
                        color = DndCrimsonDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro",
                            tint = DndCrimsonDark
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Proficiency bonus & Passive perception quick bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DndQuickPill("BONUS COMPETENZA", "+$pb", Modifier.weight(1f))
                    DndQuickPill("PERCEZIONE PASSIVA", "$passivePerception", Modifier.weight(1f))
                }
            }

            // 6 Ability Scores in 3x2 grid (Interactive: tap to edit!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("PUNTEGGI DI CARATTERISTICA")
                    Text("Tocca per modificare", fontSize = 11.sp, color = DndGoldDark)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DndAbilityBox("FORZA", "FOR", char.strength, Modifier.weight(1f)) {
                        editingAbility = "FOR" to char.strength
                    }
                    DndAbilityBox("DESTREZZA", "DES", char.dexterity, Modifier.weight(1f)) {
                        editingAbility = "DES" to char.dexterity
                    }
                    DndAbilityBox("COSTITUZIONE", "COS", char.constitution, Modifier.weight(1f)) {
                        editingAbility = "COS" to char.constitution
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DndAbilityBox("INTELLIGENZA", "INT", char.intelligence, Modifier.weight(1f)) {
                        editingAbility = "INT" to char.intelligence
                    }
                    DndAbilityBox("SAGGEZZA", "SAG", char.wisdom, Modifier.weight(1f)) {
                        editingAbility = "SAG" to char.wisdom
                    }
                    DndAbilityBox("CARISMA", "CAR", char.charisma, Modifier.weight(1f)) {
                        editingAbility = "CAR" to char.charisma
                    }
                }
            }

            // Saving Throws (Interactive: tap to toggle proficiency!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("TIRI SALVEZZA")
                    Text("Tocca per attivare competenza", fontSize = 11.sp, color = DndGoldDark)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        DndSaveRow("Forza", char.strength, char.saveStrProf, pb) {
                            viewModel.toggleSaveProficiency("forza")
                        }
                        DndSaveRow("Destrezza", char.dexterity, char.saveDexProf, pb) {
                            viewModel.toggleSaveProficiency("destrezza")
                        }
                        DndSaveRow("Costituzione", char.constitution, char.saveConProf, pb) {
                            viewModel.toggleSaveProficiency("costituzione")
                        }
                        DndSaveRow("Intelligenza", char.intelligence, char.saveIntProf, pb) {
                            viewModel.toggleSaveProficiency("intelligenza")
                        }
                        DndSaveRow("Saggezza", char.wisdom, char.saveWisProf, pb) {
                            viewModel.toggleSaveProficiency("saggezza")
                        }
                        DndSaveRow("Carisma", char.charisma, char.saveChaProf, pb) {
                            viewModel.toggleSaveProficiency("carisma")
                        }
                    }
                }
            }

            // 18 Skills Table (Interactive: tap to cycle proficiency 0 -> 1 -> 2 -> 0, or tap row to edit bonus!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("ABILITÀ (18 SKILLS)")
                    Text("Tocca riga per bonus extra", fontSize = 10.sp, color = DndGoldDark)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        val skills = listOf(
                            SkillInfo("skill_acrobatics", "Acrobazia", "DES", char.dexterity, char.skillAcrobatics, char.getSkillExtraBonus("skill_acrobatics")),
                            SkillInfo("skill_animal_handling", "Addestrare Animali", "SAG", char.wisdom, char.skillAnimalHandling, char.getSkillExtraBonus("skill_animal_handling")),
                            SkillInfo("skill_arcana", "Arcano", "INT", char.intelligence, char.skillArcana, char.getSkillExtraBonus("skill_arcana")),
                            SkillInfo("skill_athletics", "Atletica", "FOR", char.strength, char.skillAthletics, char.getSkillExtraBonus("skill_athletics")),
                            SkillInfo("skill_stealth", "Furtività", "DES", char.dexterity, char.skillStealth, char.getSkillExtraBonus("skill_stealth")),
                            SkillInfo("skill_investigation", "Indagare", "INT", char.intelligence, char.skillInvestigation, char.getSkillExtraBonus("skill_investigation")),
                            SkillInfo("skill_deception", "Inganno", "CAR", char.charisma, char.skillDeception, char.getSkillExtraBonus("skill_deception")),
                            SkillInfo("skill_intimidation", "Intimidire", "CAR", char.charisma, char.skillIntimidation, char.getSkillExtraBonus("skill_intimidation")),
                            SkillInfo("skill_performance", "Intrattenere", "CAR", char.charisma, char.skillPerformance, char.getSkillExtraBonus("skill_performance")),
                            SkillInfo("skill_insight", "Intuizione", "SAG", char.wisdom, char.skillInsight, char.getSkillExtraBonus("skill_insight")),
                            SkillInfo("skill_medicine", "Medicina", "SAG", char.wisdom, char.skillMedicine, char.getSkillExtraBonus("skill_medicine")),
                            SkillInfo("skill_nature", "Natura", "INT", char.intelligence, char.skillNature, char.getSkillExtraBonus("skill_nature")),
                            SkillInfo("skill_perception", "Percezione", "SAG", char.wisdom, char.skillPerception, char.getSkillExtraBonus("skill_perception")),
                            SkillInfo("skill_persuasion", "Persuasione", "CAR", char.charisma, char.skillPersuasion, char.getSkillExtraBonus("skill_persuasion")),
                            SkillInfo("skill_sleight_of_hand", "Rapidità di Mano", "DES", char.dexterity, char.skillSleightOfHand, char.getSkillExtraBonus("skill_sleight_of_hand")),
                            SkillInfo("skill_religion", "Religione", "INT", char.intelligence, char.skillReligion, char.getSkillExtraBonus("skill_religion")),
                            SkillInfo("skill_survival", "Sopravvivenza", "SAG", char.wisdom, char.skillSurvival, char.getSkillExtraBonus("skill_survival")),
                            SkillInfo("skill_history", "Storia", "INT", char.intelligence, char.skillHistory, char.getSkillExtraBonus("skill_history"))
                        )

                        skills.forEachIndexed { index, skill ->
                            DndSkillRow(
                                skill = skill,
                                pb = pb,
                                onProfDotClick = {
                                    viewModel.cycleSkillProficiency(skill.name)
                                },
                                onRowClick = {
                                    editingSkill = skill
                                }
                            )
                            if (index < skills.size - 1) {
                                HorizontalDivider(color = ParchmentBorder.copy(alpha = 0.5f), thickness = 1.dp)
                            }
                        }
                    }
                }
            }

            // Proficiencies summary
            item {
                SectionHeader("COMPETENZE & LINGUAGGI")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "🛡️ Armature: " + listOfNotNull(
                                if (char.armorLight) "Leggere" else null,
                                if (char.armorMedium) "Medie" else null,
                                if (char.armorHeavy) "Pesanti" else null,
                                if (char.armorShields) "Scudi" else null
                            ).joinToString(", ").ifEmpty { "Nessuna" },
                            fontSize = 14.sp,
                            color = InkTextPrimary
                        )

                        if (char.weaponsProficiency.isNotBlank()) {
                            Text(text = "⚔️ Armi: ${char.weaponsProficiency}", fontSize = 14.sp, color = InkTextPrimary)
                        }
                        if (char.toolsProficiency.isNotBlank()) {
                            Text(text = "🛠️ Strumenti: ${char.toolsProficiency}", fontSize = 14.sp, color = InkTextPrimary)
                        }
                        if (char.languages.isNotBlank()) {
                            Text(text = "📜 Linguaggi: ${char.languages}", fontSize = 14.sp, color = InkTextPrimary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Dialog for adjusting an ability score
    editingAbility?.let { (code, score) ->
        EditAbilityScoreDialog(
            abilityCode = code,
            currentScore = score,
            onDismiss = { editingAbility = null },
            onSave = { newScore ->
                editingAbility = null
                viewModel.updateAbilityScore(code, newScore)
            }
        )
    }

    // Dialog for adjusting a skill (proficiency and custom extra bonus)
    editingSkill?.let { skill ->
        EditSkillDialog(
            skill = skill,
            pb = pb,
            onDismiss = { editingSkill = null },
            onSave = { newProf, newExtra ->
                editingSkill = null
                viewModel.updateSkillConfig(skill.key, newProf, newExtra)
            }
        )
    }
}

@Composable
fun EditAbilityScoreDialog(
    abilityCode: String,
    currentScore: Int,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var score by remember { mutableStateOf(currentScore) }
    val mod = Rules5eCalculator.calculateModifier(score)
    val modStr = if (mod >= 0) "+$mod" else "$mod"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modifica $abilityCode",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DndCrimson
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Modificatore calcolato: $modStr", fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndGoldDark)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { score = (score - 1).coerceAtLeast(1) },
                        modifier = Modifier
                            .size(44.dp)
                            .border(1.5.dp, DndCrimson, CircleShape)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Diminuisci", tint = DndCrimson)
                    }

                    Text(
                        text = "$score",
                        fontSize = 36.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Black,
                        color = InkTextPrimary
                    )

                    IconButton(
                        onClick = { score = (score + 1).coerceAtMost(30) },
                        modifier = Modifier
                            .size(44.dp)
                            .border(1.5.dp, DndGold, CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumenta", tint = DndGoldDark)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(score) },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Salva", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla", color = InkTextMuted)
            }
        }
    )
}

data class SkillInfo(
    val key: String,
    val name: String,
    val abilityCode: String,
    val abilityScore: Int,
    val profLevel: Int,
    val extraBonus: Int = 0
)

@Composable
fun SectionHeader(title: String) {
    Text(
        text = "✦ $title",
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        color = DndCrimson,
        letterSpacing = 1.sp
    )
}

@Composable
fun DndQuickPill(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .border(1.dp, DndGoldDark.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentHeader),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 10.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndLeather)
            Text(value, fontSize = 22.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Black, color = DndCrimsonDark)
        }
    }
}

@Composable
fun DndAbilityBox(name: String, code: String, score: Int, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val mod = Rules5eCalculator.calculateModifier(score)
    val modStr = if (mod >= 0) "+$mod" else "$mod"

    Card(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .border(1.dp, DndGold.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(code, fontSize = 12.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.ExtraBold, color = DndLeather)
            Spacer(modifier = Modifier.height(2.dp))
            Text(modStr, fontSize = 24.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Black, color = DndCrimson)
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(ParchmentHeader)
                    .border(1.dp, ParchmentBorder, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("$score", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = InkTextPrimary)
            }
        }
    }
}

@Composable
fun DndSaveRow(name: String, abilityScore: Int, isProf: Boolean, pb: Int, onClick: () -> Unit) {
    val baseMod = Rules5eCalculator.calculateModifier(abilityScore)
    val total = baseMod + if (isProf) pb else 0
    val totalStr = if (total >= 0) "+$total" else "$total"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (isProf) DndGoldDark else Color.Transparent)
                .border(2.dp, if (isProf) DndGoldDark else ParchmentBorder, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = if (isProf) FontWeight.Bold else FontWeight.Normal,
            color = if (isProf) DndCrimsonDark else InkTextPrimary
        )
        Text(
            totalStr,
            fontSize = 15.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = if (isProf) DndCrimson else InkTextPrimary
        )
    }
}

@Composable
fun DndSkillRow(
    skill: SkillInfo,
    pb: Int,
    onProfDotClick: () -> Unit,
    onRowClick: () -> Unit
) {
    val baseMod = Rules5eCalculator.calculateModifier(skill.abilityScore)
    val total = baseMod + (skill.profLevel * pb) + skill.extraBonus
    val totalStr = if (total >= 0) "+$total" else "$total"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable(onClick = onRowClick)
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dot for cycling proficiency directly
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .clickable(onClick = onProfDotClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(
                        when (skill.profLevel) {
                            1 -> DndGoldDark
                            2 -> DndCrimson
                            else -> Color.Transparent
                        }
                    )
                    .border(
                        2.dp,
                        if (skill.profLevel > 0) DndGoldDark else ParchmentBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (skill.profLevel == 2) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(DndGold)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(
            skill.name,
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = if (skill.profLevel > 0) FontWeight.SemiBold else FontWeight.Normal,
            color = if (skill.profLevel > 0) DndCrimsonDark else InkTextPrimary
        )

        // Custom bonus badge if present
        if (skill.extraBonus != 0) {
            val badgeText = if (skill.extraBonus > 0) "+${skill.extraBonus} extra" else "${skill.extraBonus} extra"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(DndGold.copy(alpha = 0.2f))
                    .border(1.dp, DndGoldDark.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeText,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = DndCrimsonDark
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(skill.abilityCode, fontSize = 11.sp, fontFamily = FontFamily.Serif, color = InkTextMuted)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            totalStr,
            fontSize = 15.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = if (skill.profLevel > 0 || skill.extraBonus != 0) DndCrimson else InkTextPrimary
        )
    }
}

@Composable
fun EditSkillDialog(
    skill: SkillInfo,
    pb: Int,
    onDismiss: () -> Unit,
    onSave: (newProf: Int, newExtra: Int) -> Unit
) {
    var profLevel by remember { mutableStateOf(skill.profLevel) }
    var extraBonus by remember { mutableStateOf(skill.extraBonus) }

    val baseMod = Rules5eCalculator.calculateModifier(skill.abilityScore)
    val profBonus = profLevel * pb
    val total = baseMod + profBonus + extraBonus
    val totalStr = if (total >= 0) "+$total" else "$total"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modifica ${skill.name}",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DndCrimson
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Formula breakdown box
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentHeader),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Totale Calcolato: $totalStr",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.ExtraBold,
                            color = DndCrimsonDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        val baseModStr = if (baseMod >= 0) "+$baseMod" else "$baseMod"
                        val extraStr = if (extraBonus >= 0) "+$extraBonus" else "$extraBonus"
                        Text(
                            text = "${skill.abilityCode} ($baseModStr) + Comp (+$profBonus) + Extra ($extraStr)",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Serif,
                            color = DndLeather
                        )
                    }
                }

                // Proficiency selection
                Text(
                    text = "Grado di Competenza",
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = DndGoldDark
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        0 to "Nessuna",
                        1 to "Competente",
                        2 to "Maestria"
                    ).forEach { (level, label) ->
                        val isSelected = profLevel == level
                        Button(
                            onClick = { profLevel = level },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) DndCrimson else ParchmentCard,
                                contentColor = if (isSelected) Color.White else InkTextPrimary
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) DndCrimsonDark else ParchmentBorder
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Extra Bonus Stepper
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bonus Extra / Manuale (Oggetti magici, Talenti)",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        color = DndGoldDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconButton(
                            onClick = { extraBonus-- },
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.5.dp, DndCrimson, CircleShape)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Diminuisci", tint = DndCrimson)
                        }

                        val displayExtra = if (extraBonus >= 0) "+$extraBonus" else "$extraBonus"
                        Text(
                            text = displayExtra,
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Black,
                            color = InkTextPrimary
                        )

                        IconButton(
                            onClick = { extraBonus++ },
                            modifier = Modifier
                                .size(40.dp)
                                .border(1.5.dp, DndGold, CircleShape)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Aumenta", tint = DndGoldDark)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(profLevel, extraBonus) },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Salva", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla", color = InkTextMuted)
            }
        }
    )
}
