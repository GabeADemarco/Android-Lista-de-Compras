package com.mislistas.app.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mislistas.app.data.ListItemEntity
import com.mislistas.app.data.ListWithItems
import com.mislistas.app.data.ShoppingListEntity
import com.mislistas.app.ui.viewmodel.MainViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val lists by viewModel.listsWithItems.collectAsState()
    var dialogState by remember { mutableStateOf<DialogState?>(null) }
    var actionTarget by remember { mutableStateOf<ActionTarget?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Listas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogState = DialogState.NewList },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva lista", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
    ) { padding ->
        if (lists.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "No tenés listas todavía",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tocá + para crear la primera",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(lists, key = { it.list.id }) { listWithItems ->
                    ListCard(
                        listWithItems = listWithItems,
                        onToggleExpanded = { viewModel.toggleListExpanded(it) },
                        onListLongPress = { actionTarget = ActionTarget.List(it) },
                        onItemClick = { viewModel.toggleItemDone(it) },
                        onItemLongPress = { actionTarget = ActionTarget.Item(it) },
                        onAddItem = { dialogState = DialogState.NewItem(it.list.id) },
                    )
                }
            }
        }
    }

    dialogState?.let { state ->
        when (state) {
            is DialogState.NewList -> NameDialog(
                title = "Nueva lista",
                confirmLabel = "Crear",
                initialName = "",
                onDismiss = { dialogState = null },
                onConfirm = { name ->
                    viewModel.addList(name)
                    dialogState = null
                },
            )

            is DialogState.EditList -> NameDialog(
                title = "Editar lista",
                confirmLabel = "Guardar",
                initialName = state.list.name,
                onDismiss = { dialogState = null },
                onConfirm = { name ->
                    viewModel.updateList(state.list.copy(name = name.trim()))
                    dialogState = null
                },
            )

            is DialogState.NewItem -> ItemDialog(
                title = "Nuevo artículo",
                confirmLabel = "Agregar",
                initialName = "",
                initialQuantity = "",
                initialValue = "",
                onDismiss = { dialogState = null },
                onConfirm = { name, quantity, value ->
                    viewModel.addItem(state.listId, name, quantity, value)
                    dialogState = null
                },
            )

            is DialogState.EditItem -> ItemDialog(
                title = "Editar artículo",
                confirmLabel = "Guardar",
                initialName = state.item.name,
                initialQuantity = state.item.quantity.orEmpty(),
                initialValue = state.item.value?.let { formatValueInput(it) }.orEmpty(),
                onDismiss = { dialogState = null },
                onConfirm = { name, quantity, value ->
                    viewModel.updateItem(
                        state.item.copy(
                            name = name.trim(),
                            quantity = quantity?.trim()?.takeIf { it.isNotEmpty() },
                            value = value,
                        ),
                    )
                    dialogState = null
                },
            )
        }
    }

    actionTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { actionTarget = null },
            title = {
                Text(
                    when (target) {
                        is ActionTarget.List -> "Lista"
                        is ActionTarget.Item -> "Artículo"
                    },
                )
            },
            text = {
                Text(
                    when (target) {
                        is ActionTarget.List -> target.list.name
                        is ActionTarget.Item -> target.item.name
                    },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        when (target) {
                            is ActionTarget.List -> dialogState = DialogState.EditList(target.list)
                            is ActionTarget.Item -> dialogState = DialogState.EditItem(target.item)
                        }
                        actionTarget = null
                    },
                ) {
                    Text("Editar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        when (target) {
                            is ActionTarget.List -> viewModel.deleteList(target.list.id)
                            is ActionTarget.Item -> viewModel.deleteItem(target.item.id)
                        }
                        actionTarget = null
                    },
                ) {
                    Text("Eliminar")
                }
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ListCard(
    listWithItems: ListWithItems,
    onToggleExpanded: (ShoppingListEntity) -> Unit,
    onListLongPress: (ShoppingListEntity) -> Unit,
    onItemClick: (ListItemEntity) -> Unit,
    onItemLongPress: (ListItemEntity) -> Unit,
    onAddItem: () -> Unit,
) {
    val list = listWithItems.list
    val sortedItems = listWithItems.items.sortedWith(
        compareBy<ListItemEntity> { it.isDone }.thenBy { it.sortOrder }.thenBy { it.id },
    )
    val totalValue = sortedItems
        .filter { !it.isDone && it.value != null }
        .sumOf { it.value ?: 0.0 }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = { onToggleExpanded(list) },
                        onLongClick = { onListLongPress(list) },
                    )
                    .padding(horizontal = 12.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = list.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val pending = sortedItems.count { !it.isDone }
                    if (sortedItems.isNotEmpty()) {
                        Text(
                            text = if (pending == 0) "Todo listo" else "$pending pendiente(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Icon(
                    imageVector = if (list.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (list.isExpanded) "Contraer" else "Desplegar",
                )
            }

            if (list.isExpanded) {
                sortedItems.forEach { item ->
                    ItemRow(
                        item = item,
                        onClick = { onItemClick(item) },
                        onLongPress = { onItemLongPress(item) },
                    )
                }

                if (totalValue > 0.0) {
                    Text(
                        text = "Total pendiente: ${formatCurrency(totalValue)}",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                TextButton(
                    onClick = onAddItem,
                    modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Agregar artículo")
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemRow(
    item: ListItemEntity,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
    val isDone = item.isDone
    val alpha = if (isDone) 0.45f else 1f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongPress)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (isDone) TextDecoration.LineThrough else TextDecoration.None,
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            val details = buildList {
                item.quantity?.let { add("Cant: $it") }
                item.value?.let { add(formatCurrency(it)) }
            }
            if (details.isNotEmpty()) {
                Text(
                    text = details.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                )
            }
        }
    }
}

@Composable
private fun NameDialog(
    title: String,
    confirmLabel: String,
    initialName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    val isValid = name.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }, enabled = isValid) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

@Composable
private fun ItemDialog(
    title: String,
    confirmLabel: String,
    initialName: String,
    initialQuantity: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (name: String, quantity: String?, value: Double?) -> Unit,
) {
    var name by remember { mutableStateOf(initialName) }
    var quantity by remember { mutableStateOf(initialQuantity) }
    var valueText by remember { mutableStateOf(initialValue) }
    val isValid = name.trim().isNotEmpty()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Artículo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Cantidad (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = valueText,
                    onValueChange = { valueText = it },
                    label = { Text("Valor (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name, quantity, parseValue(valueText)) },
                enabled = isValid,
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        },
    )
}

private sealed interface DialogState {
    data object NewList : DialogState
    data class EditList(val list: ShoppingListEntity) : DialogState
    data class NewItem(val listId: Long) : DialogState
    data class EditItem(val item: ListItemEntity) : DialogState
}

private sealed interface ActionTarget {
    data class List(val list: ShoppingListEntity) : ActionTarget
    data class Item(val item: ListItemEntity) : ActionTarget
}

private fun parseValue(text: String): Double? {
    val trimmed = text.trim()
    if (trimmed.isEmpty()) return null
    val normalized = trimmed.replace(',', '.')
    return normalized.toDoubleOrNull()
}

private fun formatValueInput(value: Double): String {
    return if (value % 1.0 == 0.0) value.toLong().toString() else value.toString()
}

private fun formatCurrency(value: Double): String {
    val formatter = NumberFormat.getNumberInstance(Locale("es", "AR"))
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 0
    return "$${formatter.format(value)}"
}
