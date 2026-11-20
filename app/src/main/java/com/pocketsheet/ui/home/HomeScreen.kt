package com.pocketsheet.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.pocketsheet.ui.theme.DndCrimsonLight
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.DndGoldLight
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBg
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard
import com.pocketsheet.ui.theme.ParchmentHeader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCharacterClick: (Long) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val characters by viewModel.characters.collectAsState()

    var characterToDelete by remember { mutableStateOf<Character?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }

    val jsonPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            viewModel.importJsonFromUri(context, it) { success, message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message ?: if (success) "Scheda Importata!" else "Errore")
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "⚔️ POCKET SHEET ⚔️",
                                color = DndCrimson,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "SCHEDE PERSONAGGIO D&D 5e",
                                color = DndGoldDark,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp,
                                letterSpacing = 3.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            // Dual Action Bottom Bar: CREA PERSONAGGIO & IMPORTA JSON
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Button 1: CREA EROE
                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DndCrimson,
                        contentColor = DndGoldLight
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .border(1.5.dp, DndGold, RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nuovo Eroe",
                        tint = DndGoldLight,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NUOVO EROE",
                        fontFamily = FontFamily.Serif,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // Button 2: IMPORTA JSON
                Button(
                    onClick = {
                        jsonPickerLauncher.launch(arrayOf("application/json", "text/*", "*/*"))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ParchmentCard,
                        contentColor = DndLeather
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .border(1.5.dp, DndGoldDark, RoundedCornerShape(14.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = "Importa JSON",
                        tint = DndCrimson,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "IMPORTA JSON",
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DndLeather,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (characters.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = DndGold.copy(alpha = 0.5f),
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nessun Avventuriero Registrato",
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DndCrimson
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Crea un nuovo eroe da zero toccando 'NUOVO EROE' oppure carica una scheda esistente con 'IMPORTA JSON'.",
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 14.sp,
                        color = InkTextMuted,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(characters, key = { it.id }) { character ->
                        DndCharacterCard(
                            character = character,
                            onClick = { onCharacterClick(character.id) },
                            onDelete = { characterToDelete = character }
                        )
                    }
                }
            }
        }
    }

    // Confirmation dialog for deletion
    characterToDelete?.let { target ->
        AlertDialog(
            onDismissRequest = { characterToDelete = null },
            title = {
                Text(
                    text = "Congedare l'Eroe?",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = DndCrimson
                )
            },
            text = {
                Text(
                    text = "Sei sicuro di voler rimuovere '${target.characterName}' dal registro degli eroi? Questa azione non può essere revocata.",
                    color = InkTextPrimary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCharacter(target.id)
                        characterToDelete = null
                    }
                ) {
                    Text("Elimina", color = DndCrimson, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { characterToDelete = null }) {
                    Text("Annulla", color = InkTextMuted)
                }
            }
        )
    }

    // Dialog for creating a new character from scratch
    if (showCreateDialog) {
        CreateCharacterDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, className, subclass, species, level, str, dex, con, intScore, wis, cha, bg, align ->
                showCreateDialog = false
                viewModel.createCharacter(
                    name = name,
                    className = className,
                    subclass = subclass,
                    species = species,
                    level = level,
                    strength = str,
                    dexterity = dex,
                    constitution = con,
                    intelligence = intScore,
                    wisdom = wis,
                    charisma = cha,
                    background = bg,
                    alignment = align
                ) { newId ->
                    onCharacterClick(newId)
                }
            }
        )
    }
}

/**
 * Dialog for creating a brand new D&D 5e character from scratch
 */
@Composable
fun CreateCharacterDialog(
    onDismiss: () -> Unit,
    onCreate: (
        name: String,
        className: String,
        subclass: String,
        species: String,
        level: Int,
        str: Int,
        dex: Int,
        con: Int,
        intScore: Int,
        wis: Int,
        cha: Int,
        bg: String,
        align: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("Guerriero") }
    var subclass by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("Umano") }
    var levelText by remember { mutableStateOf("1") }
    var background by remember { mutableStateOf("Soldato") }
    var alignment by remember { mutableStateOf("Neutrale Buono") }

    var str by remember { mutableStateOf("15") }
    var dex by remember { mutableStateOf("14") }
    var con by remember { mutableStateOf("13") }
    var intScore by remember { mutableStateOf("12") }
    var wis by remember { mutableStateOf("10") }
    var cha by remember { mutableStateOf("8") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "⚔️ Crea Nuovo Eroe 5e",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DndCrimson,
                fontSize = 20.sp
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
                    label = { Text("Nome del Personaggio") },
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

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Caratteristiche (1-20):",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = DndLeather,
                    fontSize = 14.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatInputField("FOR", str, Modifier.weight(1f)) { str = it }
                    StatInputField("DES", dex, Modifier.weight(1f)) { dex = it }
                    StatInputField("COS", con, Modifier.weight(1f)) { con = it }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatInputField("INT", intScore, Modifier.weight(1f)) { intScore = it }
                    StatInputField("SAG", wis, Modifier.weight(1f)) { wis = it }
                    StatInputField("CAR", cha, Modifier.weight(1f)) { cha = it }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val lvl = levelText.toIntOrNull() ?: 1
                    onCreate(
                        name.ifBlank { "Nuovo Eroe" },
                        className.ifBlank { "Guerriero" },
                        subclass,
                        species.ifBlank { "Umano" },
                        lvl,
                        str.toIntOrNull() ?: 10,
                        dex.toIntOrNull() ?: 10,
                        con.toIntOrNull() ?: 10,
                        intScore.toIntOrNull() ?: 10,
                        wis.toIntOrNull() ?: 10,
                        cha.toIntOrNull() ?: 10,
                        background,
                        alignment
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
            ) {
                Text("Crea Eroe", color = Color.White, fontWeight = FontWeight.Bold)
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
fun StatInputField(label: String, value: String, modifier: Modifier = Modifier, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter { c -> c.isDigit() }.take(2)) },
        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        singleLine = true
    )
}

/**
 * Character card with D&D 5e aesthetic:
 * Aged parchment background with antique gold border, crimson typography, and badges
 */
@Composable
fun DndCharacterCard(
    character: Character,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(14.dp))
            .border(1.5.dp, DndGoldDark, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CharacterAvatar(
                imageUri = character.customImageUri,
                size = 72.dp,
                shapeRadius = 10.dp
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = character.characterName.ifBlank { "NOME SCONOSCIUTO" },
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = DndCrimsonDark,
                    maxLines = 1
                )

                if (character.className.isNotBlank()) {
                    Text(
                        text = "${character.species} • ${character.className} (${character.subclass}) Lv. ${character.level}",
                        fontSize = 12.sp,
                        color = InkTextMuted,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatBadge("CA ${character.armorClass}")
                    StatBadge("HP ${character.hpCurrent}/${character.hpMax}")
                    StatBadge(character.speed)
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Elimina eroe",
                    tint = DndCrimsonLight.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun StatBadge(text: String) {
    Surface(
        color = ParchmentHeader,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ParchmentBorder)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = DndLeather,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
