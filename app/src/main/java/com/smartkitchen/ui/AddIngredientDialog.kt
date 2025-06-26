package com.smartkitchen.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smartkitchen.model.Category
import com.smartkitchen.model.Ingredient

@Composable
fun AddIngredientDialog(onDismiss: () -> Unit, onAdd: (Ingredient) -> Unit) {
    var name by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("1") }
    var unit by remember { mutableStateOf("pieces") }
    var category by remember { mutableStateOf(Category.VEGETABLES) }
    val units = listOf("pieces", "g", "kg", "ml", "l")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Ingredient") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ingredient Name") },
                    placeholder = { Text("e.g., Tomatoes") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    DropdownMenuUnit(selectedUnit = unit, onUnitSelected = { unit = it })
                }
                Text("Category")
                CategorySelector(selected = category, onSelect = { category = it })
            }
        },
        confirmButton = {
            Button(onClick = {
                onAdd(Ingredient(
                    name = name,
                    quantity = quantity.toIntOrNull() ?: 1,
                    unit = unit,
                    category = category.name
                ))
                onDismiss()
            }) {
                Text("Add Ingredient")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun DropdownMenuUnit(selectedUnit: String, onUnitSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val units = listOf("pieces", "g", "kg", "ml", "l")

    Box {
        OutlinedTextField(
            value = selectedUnit,
            onValueChange = {},
            label = { Text("Unit") },
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
                .border(1.dp, Color.Gray),
            readOnly = true
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            units.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onUnitSelected(it)
                    expanded = false
                })
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategorySelector(selected: Category, onSelect: (Category) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Category.values().forEach { cat ->
            FilterChip(
                selected = selected == cat,
                onClick = { onSelect(cat) },
                label = { Text(cat.name.lowercase().replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
