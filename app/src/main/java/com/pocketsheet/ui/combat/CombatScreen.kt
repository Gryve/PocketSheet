package com.pocketsheet.ui.combat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.pocketsheet.domain.model.Spell
import com.pocketsheet.domain.model.Weapon
import com.pocketsheet.ui.overview.OverviewViewModel
import com.pocketsheet.ui.stats.SectionHeader
import com.pocketsheet.ui.theme.DndArcaneBlue
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndCrimsonDark
import com.pocketsheet.ui.theme.DndCrimsonLight
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.DndGreenHp
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.DndRedDmg
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard
import com.pocketsheet.ui.theme.ParchmentHeader

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CombatScreen(
    viewModel: OverviewViewModel,
    onBackClick: () -> Unit
) {
    val character by viewModel.character.collectAsState()
    val char = character ?: return

    var showAddWeaponDialog by remember { mutableStateOf(false) }
    var showAddSpellDialog by remember { mutableStateOf(false) }
    var showEditCombatMetricsDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "COMBATTIMENTO & INCANTESIMI",
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
            // HP HERO CARD
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(4.dp, RoundedCornerShape(16.dp))
                        .border(1.5.dp, DndGold.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✦ PUNTI FERITA (HP) ✦",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            color = DndLeather,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${char.hpCurrent}",
                                fontSize = 46.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Black,
                                color = if (char.hpCurrent <= 0) DndRedDmg else DndGreenHp
                            )
                            Text(
                                text = " / ${char.hpMax}",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = InkTextMuted,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        if (char.hpTemp > 0) {
                            Text(
                                text = "+${char.hpTemp} HP Temporanei",
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = DndArcaneBlue
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick +/- buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            DndQuickHpButton("-5", DndRedDmg) { viewModel.updateHp(char.hpCurrent - 5) }
                            DndQuickHpButton("-1", DndRedDmg) { viewModel.updateHp(char.hpCurrent - 1) }
                            DndQuickHpButton("+1", DndGreenHp) { viewModel.updateHp(char.hpCurrent + 1) }
                            DndQuickHpButton("+5", DndGreenHp) { viewModel.updateHp(char.hpCurrent + 5) }
                        }
                    }
                }
            }

            // AC, SPEED, INITIATIVE, HIT DICE (Clickable to edit!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DndCombatMetricBox("CLASSE ARMATURA", "${char.armorClass}" + if (char.hasShield) " 🛡️" else "", Modifier.weight(1f)) {
                        showEditCombatMetricsDialog = true
                    }
                    DndCombatMetricBox("INIZIATIVA", char.initiativeOverride.ifBlank { "+1" }, Modifier.weight(1f)) {
                        showEditCombatMetricsDialog = true
                    }
                    DndCombatMetricBox("VELOCITÀ", char.speed, Modifier.weight(1f)) {
                        showEditCombatMetricsDialog = true
                    }
                    DndCombatMetricBox("DADI VITA", "${char.hitDiceSpent}/${char.hitDiceMax}", Modifier.weight(1.2f)) {
                        showEditCombatMetricsDialog = true
                    }
                }
            }

            // DEATH SAVES
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SUCCESSI CONTRO MORTE", fontSize = 11.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndGreenHp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (1..3).forEach { index ->
                                    val checked = char.deathSaveSuccesses >= index
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (checked) DndGreenHp else Color.Transparent)
                                            .border(2.dp, DndGreenHp, CircleShape)
                                            .clickable {
                                                val newSuccesses = if (checked && char.deathSaveSuccesses == index) index - 1 else index
                                                viewModel.updateDeathSaves(newSuccesses, char.deathSaveFailures)
                                            }
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("FALLIMENTI MORTALI", fontSize = 11.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndRedDmg)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                (1..3).forEach { index ->
                                    val checked = char.deathSaveFailures >= index
                                    Box(
                                        modifier = Modifier
                                            .size(26.dp)
                                            .clip(CircleShape)
                                            .background(if (checked) DndRedDmg else Color.Transparent)
                                            .border(2.dp, DndRedDmg, CircleShape)
                                            .clickable {
                                                val newFailures = if (checked && char.deathSaveFailures == index) index - 1 else index
                                                viewModel.updateDeathSaves(char.deathSaveSuccesses, newFailures)
                                            }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SPELL SLOTS TRACKER
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("SLOT INCANTESIMI (ARCANE GEMS)")
                }
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val levelsWithSlots = char.spellSlotsTotal.keys.sorted().filter { (char.spellSlotsTotal[it] ?: 0) > 0 }
                        if (levelsWithSlots.isEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Nessuno slot attivo.", fontSize = 13.sp, color = InkTextMuted)
                                TextButton(onClick = { viewModel.updateSpellSlotsTotal("1", 2) }) {
                                    Text("+ Attiva Slot Livello 1", color = DndCrimson, fontSize = 12.sp)
                                }
                            }
                        } else {
                            levelsWithSlots.forEach { levelStr ->
                                val total = char.spellSlotsTotal[levelStr] ?: 0
                                val expended = char.spellSlotsExpended[levelStr] ?: 0
                                val available = total - expended

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Livello $levelStr",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DndLeather,
                                        modifier = Modifier.width(84.dp)
                                    )

                                    Row(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        (0 until total).forEach { slotIndex ->
                                            val isExpended = slotIndex < expended
                                            Box(
                                                modifier = Modifier
                                                    .size(26.dp)
                                                    .clip(CircleShape)
                                                    .background(if (!isExpended) DndArcaneBlue else ParchmentHeader)
                                                    .border(2.dp, if (!isExpended) DndArcaneBlue else ParchmentBorder, CircleShape)
                                                .clickable {
                                                    viewModel.toggleSpellSlot(levelStr, slotIndex)
                                                }
                                            )
                                        }
                                    }

                                    Text(
                                        text = "$available/$total",
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (available > 0) DndArcaneBlue else DndRedDmg
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // WEAPONS & ATTACKS (with Add weapon button!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("ARMI ED ATTACCHI")
                    TextButton(onClick = { showAddWeaponDialog = true }) {
                        Icon(Icons.Default.Add, null, tint = DndCrimson, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aggiungi Arma", color = DndCrimson, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            itemsIndexed(char.weapons) { index, weapon ->
                DndWeaponCard(
                    weapon = weapon,
                    onDelete = { viewModel.deleteWeapon(index) }
                )
            }

            // SPELLS LIST (with Add spell button!)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("GRIMORIO INCANTESIMI")
                    TextButton(onClick = { showAddSpellDialog = true }) {
                        Icon(Icons.Default.Add, null, tint = DndCrimson, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Aggiungi Incantesimo", color = DndCrimson, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            val spellsByLevel = char.spells.groupBy { it.level }
            spellsByLevel.keys.sorted().forEach { lvl ->
                item {
                    Text(
                        text = if (lvl == "0") "✦ Trucchetti (Cantrips)" else "✦ Incantesimi di Livello $lvl",
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DndCrimson,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                itemsIndexed(spellsByLevel[lvl] ?: emptyList()) { _, spell ->
                    val globalIndex = char.spells.indexOf(spell)
                    DndSpellCard(
                        spell = spell,
                        onDelete = { if (globalIndex >= 0) viewModel.deleteSpell(globalIndex) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Add Weapon Dialog
    if (showAddWeaponDialog) {
        AddWeaponDialog(
            onDismiss = { showAddWeaponDialog = false },
            onAdd = { weapon ->
                showAddWeaponDialog = false
                viewModel.addWeapon(weapon)
            }
        )
    }

    // Add Spell Dialog
    if (showAddSpellDialog) {
        AddSpellDialog(
            onDismiss = { showAddSpellDialog = false },
            onAdd = { spell ->
                showAddSpellDialog = false
                viewModel.addSpell(spell)
            }
        )
    }

    // Edit Combat Metrics Dialog
    if (showEditCombatMetricsDialog) {
        EditCombatMetricsDialog(
            ac = char.armorClass,
            initiative = char.initiativeOverride,
            speed = char.speed,
            hitDiceSpent = char.hitDiceSpent,
            onDismiss = { showEditCombatMetricsDialog = false },
            onSave = { ac, init, speed, hdSpent ->
                showEditCombatMetricsDialog = false
                viewModel.updateCombatMetrics(ac, init, speed, hdSpent)
            }
        )
    }
}

@Composable
fun AddWeaponDialog(
    onDismiss: () -> Unit,
    onAdd: (Weapon) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var atkBonus by remember { mutableStateOf("+4") }
    var damage by remember { mutableStateOf("1d8+2 Tagliente") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Aggiungi Arma / Attacco", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndCrimson)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Arma (es. Spada Lunga)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = atkBonus,
                    onValueChange = { atkBonus = it },
                    label = { Text("Bonus Attacco (es. +5 o CD 13)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = damage,
                    onValueChange = { damage = it },
                    label = { Text("Danno e Tipo (es. 1d8+3 Tagliente)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Note (es. Versatile, Gittata)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(Weapon(name, atkBonus, damage, notes))
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Aggiungi", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla", color = InkTextMuted) }
        }
    )
}

@Composable
fun AddSpellDialog(
    onDismiss: () -> Unit,
    onAdd: (Spell) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("1") }
    var castingTime by remember { mutableStateOf("1 Azione") }
    var range by remember { mutableStateOf("60 ft") }
    var notes by remember { mutableStateOf("") }
    var concentration by remember { mutableStateOf(false) }
    var ritual by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Aggiungi Incantesimo", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndCrimson)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Incantesimo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = level,
                        onValueChange = { level = it.filter { c -> c.isDigit() }.take(1) },
                        label = { Text("Livello (0=Trucchetto)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    OutlinedTextField(
                        value = castingTime,
                        onValueChange = { castingTime = it },
                        label = { Text("Tempo") },
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = range,
                    onValueChange = { range = it },
                    label = { Text("Gittata / Range") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Descrizione / Effetti") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = concentration,
                        onCheckedChange = { concentration = it },
                        colors = CheckboxDefaults.colors(checkedColor = DndCrimson)
                    )
                    Text("Concentrazione", fontSize = 13.sp)

                    Spacer(modifier = Modifier.width(16.dp))

                    Checkbox(
                        checked = ritual,
                        onCheckedChange = { ritual = it },
                        colors = CheckboxDefaults.colors(checkedColor = DndArcaneBlue)
                    )
                    Text("Rituale", fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            Spell(
                                level = level.ifBlank { "0" },
                                name = name,
                                castingTime = castingTime,
                                range = range,
                                concentration = concentration,
                                ritual = ritual,
                                notes = notes
                            )
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Aggiungi", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla", color = InkTextMuted) }
        }
    )
}

@Composable
fun EditCombatMetricsDialog(
    ac: Int,
    initiative: String,
    speed: String,
    hitDiceSpent: Int,
    onDismiss: () -> Unit,
    onSave: (Int, String, String, Int) -> Unit
) {
    var acText by remember { mutableStateOf("$ac") }
    var initText by remember { mutableStateOf(initiative) }
    var speedText by remember { mutableStateOf(speed) }
    var hdSpentText by remember { mutableStateOf("$hitDiceSpent") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Parametri di Combattimento", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndCrimson)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = acText,
                    onValueChange = { acText = it.filter { c -> c.isDigit() } },
                    label = { Text("Classe Armatura (CA)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = initText,
                    onValueChange = { initText = it },
                    label = { Text("Iniziativa Override (es. +3)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = speedText,
                    onValueChange = { speedText = it },
                    label = { Text("Velocità (es. 30 ft)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = hdSpentText,
                    onValueChange = { hdSpentText = it.filter { c -> c.isDigit() } },
                    label = { Text("Dadi Vita Spesi") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        acText.toIntOrNull() ?: ac,
                        initText,
                        speedText,
                        hdSpentText.toIntOrNull() ?: hitDiceSpent
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Salva", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla", color = InkTextMuted) }
        }
    )
}

@Composable
fun DndQuickHpButton(text: String, color: Color, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, color.copy(alpha = 0.7f))
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = color
        )
    }
}

@Composable
fun DndCombatMetricBox(label: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .border(1.dp, DndGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, fontSize = 9.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndLeather)
            Spacer(modifier = Modifier.height(3.dp))
            Text(value, fontSize = 16.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Black, color = DndCrimsonDark)
        }
    }
}

@Composable
fun DndWeaponCard(weapon: Weapon, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = weapon.name,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DndCrimsonDark
                )
                if (weapon.notes.isNotBlank()) {
                    Text(weapon.notes, fontSize = 12.sp, color = InkTextMuted)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = weapon.atkBonusOverride.ifBlank { "+0" },
                    fontFamily = FontFamily.Serif,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DndGoldDark
                )
                Text(
                    text = weapon.damageAndType,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = InkTextPrimary
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Delete, "Elimina", tint = DndCrimsonLight, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DndSpellCard(spell: Spell, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ParchmentBorder, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = spell.name,
                    fontFamily = FontFamily.Serif,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DndCrimsonDark,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${spell.castingTime} • ${spell.range}",
                    fontSize = 12.sp,
                    color = InkTextMuted
                )
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, "Elimina", tint = DndCrimsonLight, modifier = Modifier.size(16.dp))
                }
            }

            if (spell.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(spell.notes, fontSize = 13.sp, color = InkTextPrimary)
            }

            if (spell.concentration || spell.ritual || spell.material) {
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (spell.concentration) {
                        DndTagBadge("Concentrazione", DndCrimson)
                    }
                    if (spell.ritual) {
                        DndTagBadge("Rituale", DndArcaneBlue)
                    }
                    if (spell.material) {
                        DndTagBadge("Materiale", DndLeather)
                    }
                }
            }
        }
    }
}

@Composable
fun DndTagBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, fontSize = 10.sp, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = color)
    }
}
