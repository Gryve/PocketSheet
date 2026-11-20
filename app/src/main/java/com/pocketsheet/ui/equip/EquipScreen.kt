package com.pocketsheet.ui.equip

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pocketsheet.ui.overview.OverviewViewModel
import com.pocketsheet.ui.stats.SectionHeader
import com.pocketsheet.ui.theme.CoinCopper
import com.pocketsheet.ui.theme.CoinElectrum
import com.pocketsheet.ui.theme.CoinGold
import com.pocketsheet.ui.theme.CoinPlatinum
import com.pocketsheet.ui.theme.CoinSilver
import com.pocketsheet.ui.theme.DndCrimson
import com.pocketsheet.ui.theme.DndCrimsonDark
import com.pocketsheet.ui.theme.DndGold
import com.pocketsheet.ui.theme.DndGoldDark
import com.pocketsheet.ui.theme.DndLeather
import com.pocketsheet.ui.theme.InkTextMuted
import com.pocketsheet.ui.theme.InkTextPrimary
import com.pocketsheet.ui.theme.ParchmentBorder
import com.pocketsheet.ui.theme.ParchmentCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipScreen(
    viewModel: OverviewViewModel,
    onBackClick: () -> Unit
) {
    val character by viewModel.character.collectAsState()
    val char = character ?: return

    var editingAttunementSlot by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var showEditInventoryDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "EQUIPAGGIAMENTO & MONETE",
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
            // CURRENCY WALLET
            item {
                SectionHeader("BORSA DELLE MONETE")
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DndGold.copy(alpha = 0.5f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        DndCoinRow("Monete di Rame (CP)", char.cp, CoinCopper) { newAmount ->
                            viewModel.updateCurrencies(newAmount, char.sp, char.ep, char.gp, char.pp)
                        }
                        DndCoinRow("Monete d'Argento (SP)", char.sp, CoinSilver) { newAmount ->
                            viewModel.updateCurrencies(char.cp, newAmount, char.ep, char.gp, char.pp)
                        }
                        DndCoinRow("Monete d'Electrum (EP)", char.ep, CoinElectrum) { newAmount ->
                            viewModel.updateCurrencies(char.cp, char.sp, newAmount, char.gp, char.pp)
                        }
                        DndCoinRow("Monete d'Oro (GP)", char.gp, CoinGold) { newAmount ->
                            viewModel.updateCurrencies(char.cp, char.sp, char.ep, newAmount, char.pp)
                        }
                        DndCoinRow("Monete di Platino (PP)", char.pp, CoinPlatinum) { newAmount ->
                            viewModel.updateCurrencies(char.cp, char.sp, char.ep, char.gp, newAmount)
                        }
                    }
                }
            }

            // ATTUNEMENT SLOTS (3 SLOTS) - Clickable to edit item!
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("OGGETTI IN SINTONIA (ATTUNEMENT)")
                    Text("Tocca per modificare", fontSize = 11.sp, color = DndGoldDark)
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
                        DndAttunementSlot(1, char.attunement1) {
                            editingAttunementSlot = 1 to char.attunement1
                        }
                        DndAttunementSlot(2, char.attunement2) {
                            editingAttunementSlot = 2 to char.attunement2
                        }
                        DndAttunementSlot(3, char.attunement3) {
                            editingAttunementSlot = 3 to char.attunement3
                        }
                    }
                }
            }

            // INVENTORY / EQUIPMENT LIST (Clickable to edit!)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SectionHeader("INVENTARIO & SACCA DA VIAGGIO")
                    IconButton(onClick = { showEditInventoryDialog = true }, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Edit, "Modifica", tint = DndGoldDark, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ParchmentBorder, RoundedCornerShape(14.dp))
                        .clickable { showEditInventoryDialog = true },
                    colors = CardDefaults.cardColors(containerColor = ParchmentCard),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = char.equipment.ifBlank { "Nessun equipaggiamento registrato nella sacca (Tocca per aggiungere)." },
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = InkTextPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Edit Attunement Slot Dialog
    editingAttunementSlot?.let { (slotIndex, currentItem) ->
        var itemName by remember { mutableStateOf(currentItem) }
        AlertDialog(
            onDismissRequest = { editingAttunementSlot = null },
            title = {
                Text("Oggetto in Sintonia - Slot $slotIndex", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndCrimson)
            },
            text = {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Nome Oggetto Magico") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        editingAttunementSlot = null
                        viewModel.updateAttunement(slotIndex, itemName)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
                ) {
                    Text("Salva", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { editingAttunementSlot = null }) { Text("Annulla", color = InkTextMuted) }
            }
        )
    }

    // Edit Full Inventory Dialog
    if (showEditInventoryDialog) {
        var inventoryText by remember { mutableStateOf(char.equipment) }
        AlertDialog(
            onDismissRequest = { showEditInventoryDialog = false },
            title = {
                Text("Modifica Inventario", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, color = DndCrimson)
            },
            text = {
                OutlinedTextField(
                    value = inventoryText,
                    onValueChange = { inventoryText = it },
                    label = { Text("Equipaggiamento ed Oggetti") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEditInventoryDialog = false
                        viewModel.updateEquipment(inventoryText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DndCrimson)
                ) {
                    Text("Salva", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditInventoryDialog = false }) { Text("Annulla", color = InkTextMuted) }
            }
        )
    }
}

@Composable
fun DndCoinRow(label: String, amount: Int, badgeColor: Color, onAmountChange: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(badgeColor)
                .border(1.dp, DndLeather, CircleShape)
        )
        Text(
            text = "  $label",
            modifier = Modifier.weight(1f),
            fontFamily = FontFamily.Serif,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = InkTextPrimary
        )

        IconButton(
            onClick = { onAmountChange((amount - 1).coerceAtLeast(0)) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(Icons.Default.Remove, contentDescription = "Diminuisci", tint = DndCrimsonDark, modifier = Modifier.size(16.dp))
        }

        Text(
            text = "$amount",
            fontSize = 17.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            color = InkTextPrimary,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(
            onClick = { onAmountChange(amount + 1) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Aumenta", tint = DndGold, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun DndAttunementSlot(slotIndex: Int, itemName: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Slot $slotIndex:",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = DndLeather,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = itemName.ifBlank { "— Vuoto (Tocca per impostare) —" },
            fontWeight = if (itemName.isNotBlank()) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp,
            color = if (itemName.isNotBlank()) DndCrimsonDark else InkTextMuted,
            modifier = Modifier.weight(1f)
        )
        Icon(Icons.Default.Edit, "Modifica", tint = DndGoldDark.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
    }
}
