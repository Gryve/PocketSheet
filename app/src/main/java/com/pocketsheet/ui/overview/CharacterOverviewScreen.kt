package com.pocketsheet.ui.overview

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketsheet.domain.model.Character
import com.pocketsheet.ui.components.CharacterAvatar
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndCrimsonDark
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.DndGoldLight
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard
import com.pocketsheet.ui.theme.ParchmentHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterOverviewScreen(
    viewModel: OverviewViewModel,
    onBackClick: () -> Unit,
    onNavigateStats: (Long) -> Unit,
    onNavigateCombat: (Long) -> Unit,
    onNavigateLore: (Long) -> Unit,
    onNavigateEquip: (Long) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val character by viewModel.character.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.setCustomImage(context, it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = (character?.characterName?.ifBlank { "NOME COGNOME" } ?: "CARICAMENTO...").uppercase(),
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 21.sp,
                        letterSpacing = 1.5.sp,
                        color = DndCrimsonDark,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro",
                            tint = DndCrimsonDark,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    // Quick edit button
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Modifica Eroe",
                            tint = DndCrimsonDark
                        )
                    }
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Altre opzioni",
                            tint = DndCrimsonDark
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Modifica Informazioni Base",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, null, tint = DndCrimson) },
                            onClick = {
                                showMenu = false
                                showEditDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Riposo Lungo (Reset HP & Slot)",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Bedtime, null, tint = DndCrimson) },
                            onClick = {
                                showMenu = false
                                viewModel.performLongRest()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Riposo Lungo completato! HP e slot ripristinati.")
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Condividi / Esporta JSON",
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            leadingIcon = { Icon(Icons.Default.Share, null, tint = DndGoldDark) },
                            onClick = {
                                showMenu = false
                                coroutineScope.launch {
                                    val json = viewModel.exportJson()
                                    if (json != null) {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(Intent.EXTRA_TEXT, json)
                                            type = "application/json"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Esporta Scheda D&D 5e"))
                                    }
                                }
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val char = character ?: return@Column

            // Large Hero Portrait with Ornate Gold Frame
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .aspectRatio(1f)
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .border(3.dp, DndGold, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = ParchmentCard)
            ) {
                CharacterAvatar(
                    imageUri = char.customImageUri,
                    modifier = Modifier.fillMaxSize(),
                    size = 320.dp,
                    shapeRadius = 18.dp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // "CARICA IMMAGINE" Action
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(ParchmentHeader)
                    .border(1.5.dp, DndGoldDark, RoundedCornerShape(20.dp))
                    .clickable {
                        imagePicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                    .padding(horizontal = 18.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CARICA IMMAGINE",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DndLeather,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.FileUpload,
                    contentDescription = "Carica immagine eroe",
                    tint = DndCrimson,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Character Mini Info Summary Pill (Clickable to Edit!)
            Surface(
                color = ParchmentCard,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showEditDialog = true }
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${char.species} • ${char.className} (${char.subclass}) Lv. ${char.level}",
                        fontFamily = FontFamily.Serif,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = InkTextMuted
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Modifica",
                        tint = DndGoldDark,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2x2 Grid Section Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DndSectionNavCard(
                    title = "STATISTICHE",
                    subtitle = "Caratteristiche & Abilità",
                    icon = Icons.Default.Person,
                    accentColor = DndCrimson,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateStats(char.id) }
                )
                DndSectionNavCard(
                    title = "COMBAT",
                    subtitle = "HP, Slot & Attacchi",
                    icon = Icons.Default.SportsKabaddi,
                    accentColor = DndCrimsonDark,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateCombat(char.id) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DndSectionNavCard(
                    title = "LORE & PRIVILEGI",
                    subtitle = "Tratti & Background",
                    icon = Icons.Default.AutoStories,
                    accentColor = DndGoldDark,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateLore(char.id) }
                )
                DndSectionNavCard(
                    title = "EQUIP",
                    subtitle = "Monete & Inventario",
                    icon = Icons.Default.Backpack,
                    accentColor = DndLeather,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateEquip(char.id) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog for editing basic info of the character
    if (showEditDialog) {
        val char = character
        if (char != null) {
            EditBasicInfoDialog(
                character = char,
                onDismiss = { showEditDialog = false },
                onSave = { name, className, subclass, species, level, bg, align, ac, maxHp, speed, hd ->
                    showEditDialog = false
                    viewModel.updateBasicInfo(
                        name = name,
                        className = className,
                        subclass = subclass,
                        species = species,
                        level = level,
                        background = bg,
                        alignment = align,
                        armorClass = ac,
                        hpMax = maxHp,
                        speed = speed,
                        hitDiceMax = hd
                    )
                }
            )
        }
    }
}

@Composable
fun EditBasicInfoDialog(
    character: Character,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        className: String,
        subclass: String,
        species: String,
        level: Int,
        bg: String,
        align: String,
        ac: Int,
        maxHp: Int,
        speed: String,
        hd: String
    ) -> Unit
) {
    var name by remember { mutableStateOf(character.characterName) }
    var className by remember { mutableStateOf(character.className) }
    var subclass by remember { mutableStateOf(character.subclass) }
    var species by remember { mutableStateOf(character.species) }
    var levelText by remember { mutableStateOf("${character.level}") }
    var background by remember { mutableStateOf(character.background) }
    var alignment by remember { mutableStateOf(character.alignment) }
    var acText by remember { mutableStateOf("${character.armorClass}") }
    var maxHpText by remember { mutableStateOf("${character.hpMax}") }
    var speed by remember { mutableStateOf(character.speed) }
    var hitDiceMax by remember { mutableStateOf(character.hitDiceMax) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modifica Informazioni Eroe",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DndCrimson
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Personaggio") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Classe") },
                        modifier = Modifier.weight(1.2f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = levelText,
                        onValueChange = { levelText = it.filter { c -> c.isDigit() } },
                        label = { Text("Livello") },
                        modifier = Modifier.weight(0.8f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = species,
                        onValueChange = { species = it },
                        label = { Text("Specie / Razza") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = subclass,
                        onValueChange = { subclass = it },
                        label = { Text("Sottoclasse") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = background,
                        onValueChange = { background = it },
                        label = { Text("Background") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = alignment,
                        onValueChange = { alignment = it },
                        label = { Text("Allineamento") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = acText,
                        onValueChange = { acText = it.filter { c -> c.isDigit() } },
                        label = { Text("CA (Armor Class)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = maxHpText,
                        onValueChange = { maxHpText = it.filter { c -> c.isDigit() } },
                        label = { Text("HP Massimi") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = speed,
                        onValueChange = { speed = it },
                        label = { Text("Velocità") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = hitDiceMax,
                        onValueChange = { hitDiceMax = it },
                        label = { Text("Dadi Vita Totali") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        name.ifBlank { character.characterName },
                        className,
                        subclass,
                        species,
                        levelText.toIntOrNull() ?: character.level,
                        background,
                        alignment,
                        acText.toIntOrNull() ?: character.armorClass,
                        maxHpText.toIntOrNull() ?: character.hpMax,
                        speed,
                        hitDiceMax
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Salva Modifiche", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla", color = InkTextMuted)
            }
        }
    )
}

@Composable
fun DndSectionNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .border(1.5.dp, DndGold.copy(alpha = 0.8f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ParchmentHeader)
                    .border(1.dp, DndGoldDark.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = if (title.contains("&")) 14.sp else 16.sp,
                color = DndCrimsonDark,
                textAlign = TextAlign.Center,
                letterSpacing = 0.5.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = InkTextMuted,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
