package com.pocketsheet.ui.lore

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketsheet.ui.overview.OverviewViewModel
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndCrimsonDark
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoreScreen(
    viewModel: OverviewViewModel,
    onBackClick: () -> Unit
) {
    val character by viewModel.character.collectAsState()
    val char = character ?: return

    var editingSection by remember { mutableStateOf<Pair<String, String>?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "LORE & PRIVILEGI DELL'EROE",
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
            // General RP info
            item {
                DndLoreSectionCard(
                    title = "✦ IDENTITÀ & ALLINEAMENTO",
                    content = "Specie / Razza: ${char.species}\nClasse: ${char.className} (${char.subclass})\nBackground: ${char.background}\nAllineamento: ${char.alignment.ifBlank { "Nessuno specificato" }}",
                    onEdit = null
                )
            }

            // Class Features
            item {
                DndLoreSectionCard(
                    title = "✦ PRIVILEGI DI CLASSE",
                    content = char.classFeatures.ifBlank { "Nessun privilegio registrato." },
                    onEdit = { editingSection = "Privilegi di Classe" to char.classFeatures }
                )
            }

            // Species Traits
            item {
                DndLoreSectionCard(
                    title = "✦ TRATTI DI RAZZA",
                    content = char.speciesTraits.ifBlank { "Nessun tratto registrato." },
                    onEdit = { editingSection = "Tratti di Razza" to char.speciesTraits }
                )
            }

            // Feats
            item {
                DndLoreSectionCard(
                    title = "✦ TALENTI",
                    content = char.feats.ifBlank { "Nessun talento registrato." },
                    onEdit = { editingSection = "Talenti" to char.feats }
                )
            }

            // Appearance
            item {
                DndLoreSectionCard(
                    title = "✦ ASPETTO FISICO",
                    content = char.appearance.ifBlank { "Nessuna descrizione fisica." },
                    onEdit = { editingSection = "Aspetto Fisico" to char.appearance }
                )
            }

            // Backstory
            item {
                DndLoreSectionCard(
                    title = "✦ STORIA & RETROSCENA",
                    content = char.backstory.ifBlank { "Nessuna storia registrata." },
                    onEdit = { editingSection = "Storia & Retroscena" to char.backstory }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    editingSection?.let { (title, currentContent) ->
        EditLoreDialog(
            title = title,
            initialText = currentContent,
            onDismiss = { editingSection = null },
            onSave = { newText ->
                editingSection = null
                when (title) {
                    "Privilegi di Classe" -> viewModel.updateLore(newText, char.speciesTraits, char.feats, char.appearance, char.backstory)
                    "Tratti di Razza" -> viewModel.updateLore(char.classFeatures, newText, char.feats, char.appearance, char.backstory)
                    "Talenti" -> viewModel.updateLore(char.classFeatures, char.speciesTraits, newText, char.appearance, char.backstory)
                    "Aspetto Fisico" -> viewModel.updateLore(char.classFeatures, char.speciesTraits, char.feats, newText, char.backstory)
                    "Storia & Retroscena" -> viewModel.updateLore(char.classFeatures, char.speciesTraits, char.feats, char.appearance, newText)
                }
            }
        )
    }
}

@Composable
fun DndLoreSectionCard(title: String, content: String, onEdit: (() -> Unit)?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DndGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = ParchmentCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = DndCrimson,
                    letterSpacing = 1.sp
                )
                if (onEdit != null) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Edit, "Modifica", tint = DndGoldDark, modifier = Modifier.size(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                color = InkTextPrimary
            )
        }
    }
}

@Composable
fun EditLoreDialog(
    title: String,
    initialText: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Modifica $title",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = DndCrimson
            )
        },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(title) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { onSave(text) },
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
