package com.example.levelupmobile.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupmobile.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    navController: NavController,
    currentRoute: String?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showOverlay by viewModel.showAddedToCartOverlay.collectAsStateWithLifecycle()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    //estado local para saber si estamos editando
    var isEditing by remember(uiState.isSearching) { mutableStateOf(!uiState.isSearching) }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = uiState.query,
                selection = TextRange(uiState.query.length)
            )
        )
    }
    var isNavigatingBack by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.query) {
        if (uiState.query != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(
                text = uiState.query,
                selection = TextRange(uiState.query.length)
            )
        }
    }

    LaunchedEffect(isEditing) {
        if (isEditing) {
            textFieldValue = textFieldValue.copy(selection = TextRange(textFieldValue.text.length))
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF008AC2),
                        navigationIconContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                            if (isEditing && uiState.isSearching) {
                                isEditing = false
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            } else {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onNavigateBack()
                            }
                        }) {
                            Icon(Icons.Default.ArrowBack, "Volver")
                        }
                    },
                    title = {
                        if (isEditing) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(end = 16.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    BasicTextField(
                                        value = textFieldValue,
                                        onValueChange = {
                                            textFieldValue = it
                                            viewModel.onQueryChanged(it.text)
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .focusRequester(focusRequester),
                                        textStyle = TextStyle(
                                            color = Color.Black,
                                            fontSize = 14.sp
                                        ),
                                        cursorBrush = SolidColor(Color(0xFF008AC2)), // Cursor azul
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(
                                            onSearch = {
                                                viewModel.onSearch()
                                                keyboardController?.hide()
                                                focusManager.clearFocus()
                                                isEditing = false
                                            }
                                        ),
                                        decorationBox = { innerTextField ->
                                            Box(contentAlignment = Alignment.CenterStart) {
                                                if (textFieldValue.text.isEmpty()) {
                                                    Text(
                                                        "Busca los mejores productos para gamers :)",
                                                        color = Color.Gray,
                                                        fontSize = 14.sp,
                                                        maxLines = 1
                                                    )
                                                }
                                                innerTextField()
                                            }
                                        }
                                    )

                                    if (textFieldValue.text.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                viewModel.onClearSearch()
                                                focusRequester.requestFocus()
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Borrar",
                                                tint = Color.Gray
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            FakeSearchBar(
                                query = uiState.query,
                                onClick = {
                                    viewModel.onEnterEditMode()
                                    isEditing = true
                                }
                            )
                        }
                    }
                )
            },
            bottomBar = {
                HomeBottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                if (uiState.isSearching) {
                    //resultados
                    if (uiState.isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF008AC2))
                        }
                    } else if (uiState.searchResults.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No se encontraron productos :(", color = Color.Gray)
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(uiState.searchResults) { product ->
                                ProductCard(
                                    product = product,
                                    onAddToCartClick = { viewModel.onAddToCartClicked(product) }
                                )
                            }
                        }
                    }
                } else {
                    // --- HISTORIAL ---
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(uiState.history) { historyItem ->
                            HistoryItemRow(
                                text = historyItem,
                                onClick = {
                                    viewModel.onHistoryItemClicked(historyItem)
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    isEditing = false
                                },
                                onDelete = { viewModel.onDeleteHistoryItem(historyItem) }
                            )
                        }
                    }
                }
            }
        }

        if (showOverlay) {
            AddedToCartOverlay()
        }
    }
}

@Composable
fun HistoryItemRow(text: String, onClick: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.History, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, "Borrar", tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
    }
}