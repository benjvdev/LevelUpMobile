package com.example.levelupmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            HomeTopBar(
                onMenuClick = { /* TODO: Abrir Drawer */ },
                onSearchClick = { /* TODO: Navegar a pantalla de búsqueda */ }
            )
        },
        bottomBar = {
            HomeBottomBar()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Contenido de Home (aquí irá el catálogo)")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    TopAppBar(
        // 2. Colores
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF008AC2),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menú"
                )
            }
        },
        title = {
            FakeSearchBar(onClick = onSearchClick)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FakeSearchBar(
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(end = 16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Busca los mejores productos para gamers :)",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
@Composable
fun HomeBottomBar(
    // (Luego aquí añadiremos el NavController para la navegación real)
) {
    // 1. Lista de nuestros items de navegación
    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Buscar", "search", Icons.Default.Search),
        BottomNavItem("Carrito", "cart", Icons.Default.ShoppingCart),
        BottomNavItem("Perfil", "profile", Icons.Default.Person)
    )

    // 2. Estado para saber cuál item está seleccionado (visual)
    // Usamos rememberSaveable para que sobreviva a rotaciones
    var selectedRoute by rememberSaveable { mutableStateOf("home") }

    // 3. La barra de navegación
    NavigationBar {
        // 4. Iteramos sobre cada item
        items.forEach { item ->
            NavigationBarItem(
                // 5. Estado de selección
                selected = (selectedRoute == item.route),

                // 6. Acción (por ahora solo cambia el estado visual)
                onClick = {
                    selectedRoute = item.route
                    // TODO: Aquí irá la navegación: navController.navigate(item.route)
                },
                // 7. El ícono
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                // 8. El texto debajo del ícono
                label = { Text(item.label) }
            )
        }
    }
}
private data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)