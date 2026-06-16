package com.example.eas.ui.screen

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eas.R
import com.example.eas.data.entity.CartItem
import com.example.eas.data.entity.MembershipSettings
import com.example.eas.data.entity.Order
import com.example.eas.data.entity.OrderStatus
import com.example.eas.data.entity.OrderType
import com.example.eas.data.entity.PaymentMethod
import com.example.eas.data.entity.Product
import com.example.eas.data.entity.RewardEntity
import com.example.eas.data.entity.User
import com.example.eas.data.entity.UserRole
import com.example.eas.ui.theme.Brown
import com.example.eas.ui.theme.Cream
import com.example.eas.ui.theme.DarkGreen
import com.example.eas.ui.theme.GoldAccent
import com.example.eas.ui.theme.LightCream
import com.example.eas.ui.theme.LightGreen
import com.example.eas.ui.theme.MediumGreen
import com.example.eas.ui.theme.PlatinumAccent
import com.example.eas.ui.theme.SilverAccent
import com.example.eas.ui.viewmodel.AppEvent
import com.example.eas.ui.viewmodel.CoffeeShopViewModel
import com.example.eas.util.Formatter
import com.example.eas.util.QrCodeGenerator
import kotlinx.coroutines.delay


@Composable
fun CoffeeAppSplashScreen(
    viewModel: CoffeeShopViewModel,
    onRouteReady: (Boolean) -> Unit
) {
    val bootstrapped by viewModel.bootstrapped.collectAsStateWithLifecycle()
    val user by viewModel.currentUser.collectAsStateWithLifecycle()

    val infiniteTransition = rememberInfiniteTransition(label = "splash_pulse")
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "logo_scale"
    )

    LaunchedEffect(bootstrapped, user) {
        if (bootstrapped) {
            delay(1400)
            onRouteReady(user?.role == UserRole.ADMIN.name)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(DarkGreen, MediumGreen))),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(148.dp)
                    .scale(logoScale)
                    .background(Cream.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_logo),
                    contentDescription = "Coffee Bliss",
                    modifier = Modifier.size(116.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "Coffee Bliss",
                    style = MaterialTheme.typography.displaySmall,
                    color = Cream,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Kopi premium, reward nyata",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Cream.copy(alpha = 0.65f)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    viewModel: CoffeeShopViewModel,
    onAuthenticated: (Boolean) -> Unit
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var isRegister by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let { onAuthenticated(it.role == UserRole.ADMIN.name) }
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is AppEvent.Message) snackbarHostState.showSnackbar(event.text)
        }
    }

    if (showForgotDialog) {
        AlertDialog(
            onDismissRequest = { showForgotDialog = false },
            title = { Text("Lupa Password") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        "Masukkan email akun kamu. Admin akan mereset password secara manual.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    OutlinedTextField(
                        value = forgotEmail, onValueChange = { forgotEmail = it },
                        label = { Text("Email") }, modifier = Modifier.fillMaxWidth(),
                        singleLine = true, shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showForgotDialog = false
                    forgotEmail = ""
                    viewModel.sendForgotPasswordNotice()
                }) { Text("Kirim Permintaan") }
            },
            dismissButton = { TextButton(onClick = { showForgotDialog = false }) { Text("Batal") } }
        )
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(DarkGreen, MediumGreen)))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.38f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(Cream.copy(alpha = 0.14f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash_logo),
                        contentDescription = "Coffee Bliss",
                        modifier = Modifier.size(68.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text(
                    "Coffee Bliss",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Cream,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Pesan · Kumpul Poin · Tukar Reward",
                    style = MaterialTheme.typography.bodySmall,
                    color = Cream.copy(alpha = 0.65f)
                )
            }

            Surface(
                modifier = Modifier.fillMaxWidth().weight(0.62f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = LightCream
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(4.dp)) {
                                listOf(false to "Masuk", true to "Daftar").forEach { (isReg, label) ->
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(11.dp),
                                        color = if (isRegister == isReg) DarkGreen else Color.Transparent,
                                        onClick = { isRegister = isReg }
                                    ) {
                                        Text(
                                            label,
                                            color = if (isRegister == isReg) Cream
                                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.labelLarge,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(vertical = 13.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (isRegister) {
                        item {
                            OutlinedTextField(
                                value = name, onValueChange = { name = it },
                                label = { Text("Nama Lengkap") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true, shape = RoundedCornerShape(14.dp)
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = phone, onValueChange = { phone = it },
                                label = { Text("No HP") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true, shape = RoundedCornerShape(14.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )
                        }
                    }
                    item {
                        OutlinedTextField(
                            value = email, onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true, shape = RoundedCornerShape(14.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = password, onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true, shape = RoundedCornerShape(14.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = if (passwordVisible) "Sembunyikan password" else "Tampilkan password"
                                    )
                                }
                            }
                        )
                    }
                    if (!isRegister) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                                TextButton(onClick = { showForgotDialog = true }) {
                                    Text(
                                        "Lupa password?",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = DarkGreen
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = {
                                if (isRegister) viewModel.register(name, email, phone, password)
                                else viewModel.login(email, password)
                            },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                if (isRegister) "Buat Akun" else "Masuk",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                    if (!isRegister) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = DarkGreen.copy(alpha = 0.06f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        "Akun demo",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkGreen.copy(alpha = 0.55f)
                                    )
                                    Text(
                                        "Customer  ·  user@cb.com  /  1234",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                                    )
                                    Text(
                                        "Admin      ·  admin@cb.com  /  admin",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomeScreen(
    viewModel: CoffeeShopViewModel,
    onProducts: () -> Unit,
    onProductDetail: (Int) -> Unit,
    onCart: () -> Unit,
    onRewards: () -> Unit,
    onHistory: () -> Unit,
    onProfile: () -> Unit
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val featured by viewModel.featuredProducts.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()

    CustomerScaffold(
        title = "Coffee Bliss",
        selected = "Beranda",
        onHome = {},
        onProducts = onProducts,
        onCart = onCart,
        onHistory = onHistory,
        onProfile = onProfile
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item { HeroCard(user = user, onRewards = onRewards) }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SectionHeader("Favorit", "Lihat semua", onProducts)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(end = 4.dp)
                    ) {
                        items(featured.ifEmpty { products.take(6) }) { product ->
                            ProductCard(product = product, onClick = { onProductDetail(product.id) })
                        }
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "Kategori",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DarkGreen
                    )
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(products.map { it.category }.distinct()) { cat ->
                            AssistChip(onClick = onProducts, label = { Text(cat) })
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: CoffeeShopViewModel,
    onBack: () -> Unit,
    onProductDetail: (Int) -> Unit,
    onCart: () -> Unit
) {
    val allProducts by viewModel.products.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Semua") }
    val categories = listOf("Semua") + allProducts.map { it.category }.distinct()
    val filtered = allProducts.filter {
        (category == "Semua" || it.category == category) &&
                it.name.contains(query, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu Kopi") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = onCart) {
                        Icon(Icons.Filled.ShoppingCart, "Keranjang")
                    }
                },
                colors = appBarColors()
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Cari produk…") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    leadingIcon = { Icon(Icons.Filled.Coffee, null, tint = DarkGreen.copy(alpha = 0.6f)) }
                )
            }
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(end = 4.dp)
                ) {
                    items(categories) { item ->
                        FilterChip(
                            selected = category == item,
                            onClick = { category = item },
                            label = { Text(item) }
                        )
                    }
                }
            }
            if (filtered.isEmpty()) {
                item {
                    EmptyState(
                        icon = Icons.Filled.Coffee,
                        title = "Produk tidak ditemukan",
                        body = "Coba ubah filter atau kata kunci pencarian."
                    )
                }
            }
            items(filtered) { product ->
                ProductWideCard(product = product, onClick = { onProductDetail(product.id) })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    viewModel: CoffeeShopViewModel,
    productId: Int,
    onBack: () -> Unit,
    onCart: () -> Unit
) {
    val product by viewModel.observeProduct(productId).collectAsStateWithLifecycle(initialValue = null)
    val snackbarHostState = remember { SnackbarHostState() }
    var quantity by remember { mutableIntStateOf(1) }
    var orderType by remember { mutableStateOf(OrderType.TAKE_AWAY) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is AppEvent.Message) snackbarHostState.showSnackbar(event.text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.35f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onCart) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.Black.copy(alpha = 0.35f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.ShoppingCart, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            product?.let { item ->
                Surface(shadowElevation = 12.dp, color = LightCream) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        QuantityRow(
                            quantity = quantity,
                            onMinus = { if (quantity > 1) quantity-- },
                            onPlus = { quantity++ }
                        )
                        Button(
                            onClick = { viewModel.addToCart(item.id, quantity, orderType, null) },
                            enabled = item.isAvailable,
                            modifier = Modifier.weight(1f).height(52.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Filled.AddShoppingCart, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(if (item.isAvailable) "Tambah ke Keranjang" else "Tidak Tersedia")
                        }
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        product?.let { item ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(LightCream),
                contentPadding = padding
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                        ProductImage(
                            imageKey = item.imageKey,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, LightCream)
                                    )
                                )
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                item.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = DarkGreen,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Brown.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    Formatter.formatRupiah(item.price),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Brown,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = DarkGreen.copy(alpha = 0.1f)
                        ) {
                            Text(
                                item.category,
                                color = DarkGreen,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
                            )
                        }
                        Spacer(Modifier.height(2.dp))
                        Text(
                            item.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            lineHeight = 22.sp
                        )
                    }
                }
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Pilihan Order",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            FilterChip(
                                selected = orderType == OrderType.TAKE_AWAY,
                                onClick = { orderType = OrderType.TAKE_AWAY },
                                label = { Text("Take Away") }
                            )
                            FilterChip(
                                selected = orderType == OrderType.DINE_IN,
                                onClick = { orderType = OrderType.DINE_IN },
                                label = { Text("Dine In") }
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            EmptyState(Icons.Filled.LocalCafe, "Produk tidak ditemukan", "Produk ini sudah tidak tersedia.")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CoffeeShopViewModel,
    onBack: () -> Unit,
    onOrderCreated: (Int) -> Unit,
    onProducts: () -> Unit = {}
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val cartItems by (user?.let { viewModel.observeCart(it.id) }
        ?: kotlinx.coroutines.flow.flowOf(emptyList()))
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val products by viewModel.products.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var orderType by remember { mutableStateOf(OrderType.TAKE_AWAY) }
    var paymentMethod by remember { mutableStateOf(PaymentMethod.QRIS_DEMO) }
    var tableNumber by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val productMap = products.associateBy { it.id }
    val subtotal = cartItems.sumOf { (productMap[it.productId]?.price ?: 0L) * it.quantity }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AppEvent.Message -> snackbarHostState.showSnackbar(event.text)
                is AppEvent.OrderCreated -> onOrderCreated(event.orderId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Keranjang") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (cartItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightCream)
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    EmptyState(
                        icon = Icons.Filled.ShoppingCart,
                        title = "Keranjang masih kosong",
                        body = "Pilih kopi favorit dari halaman menu."
                    )
                    Button(onClick = onProducts, shape = RoundedCornerShape(14.dp)) {
                        Icon(Icons.Filled.Coffee, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Lihat Menu")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightCream)
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                item {
                    Text(
                        "${cartItems.size} item",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                    )
                }
                items(cartItems) { item ->
                    val product = productMap[item.productId]
                    CartRow(
                        item = item,
                        product = product,
                        onMinus = { viewModel.updateCartQuantity(item, item.quantity - 1) },
                        onPlus = { viewModel.updateCartQuantity(item, item.quantity + 1) },
                        onRemove = { viewModel.removeCartItem(item.id) }
                    )
                }
                item {
                    CheckoutCard(
                        subtotal = subtotal,
                        orderType = orderType,
                        onOrderTypeChange = { orderType = it },
                        paymentMethod = paymentMethod,
                        onPaymentMethodChange = { paymentMethod = it },
                        tableNumber = tableNumber,
                        onTableNumberChange = { tableNumber = it },
                        notes = notes,
                        onNotesChange = { notes = it },
                        onPay = { viewModel.checkout(orderType, paymentMethod, tableNumber, notes) }
                    )
                }
            }
        }
    }
}


@Composable
fun PaymentResultScreen(
    viewModel: CoffeeShopViewModel,
    orderId: Int,
    onHistory: () -> Unit,
    onHome: () -> Unit
) {
    val order by viewModel.observeOrder(orderId).collectAsStateWithLifecycle(initialValue = null)

    Box(modifier = Modifier.fillMaxSize().background(LightCream)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(listOf(DarkGreen, DarkGreen.copy(alpha = 0f)))
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Cream.copy(alpha = 0.18f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = DarkGreen,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
            Spacer(Modifier.height(28.dp))
            Text(
                "Pembayaran Berhasil!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = DarkGreen,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Order ${order?.orderNumber ?: ""}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )
            Spacer(Modifier.height(24.dp))
            ElevatedCard(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = DarkGreen),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(GoldAccent.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Star, null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text(
                            "Poin Masuk",
                            color = Cream.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "+${order?.pointsEarned ?: 0} poin",
                            color = Cream,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Spacer(Modifier.height(36.dp))
            Button(
                onClick = onHistory,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) { Text("Lihat Riwayat Order", style = MaterialTheme.typography.titleSmall) }
            Spacer(Modifier.height(10.dp))
            TextButton(onClick = onHome) { Text("Kembali ke Beranda") }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    viewModel: CoffeeShopViewModel,
    onBack: () -> Unit,
    onOrderDetail: (Int) -> Unit
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val orders by (user?.let { viewModel.observeCustomerOrders(it.id) }
        ?: kotlinx.coroutines.flow.flowOf(emptyList()))
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Order") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        }
    ) { padding ->
        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightCream)
                    .padding(padding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(
                    icon = Icons.AutoMirrored.Filled.ReceiptLong,
                    title = "Belum ada order",
                    body = "Order yang sudah dibayar akan muncul di sini."
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightCream)
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(orders) { order ->
                    OrderCard(order = order, onClick = { onOrderDetail(order.id) })
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    viewModel: CoffeeShopViewModel,
    orderId: Int,
    onBack: () -> Unit
) {
    val order by viewModel.observeOrder(orderId).collectAsStateWithLifecycle(initialValue = null)
    val items by viewModel.observeOrderItems(orderId).collectAsStateWithLifecycle(initialValue = emptyList())
    val isReward = order?.orderType == OrderType.REWARD_REDEEM.name

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isReward) "Detail Reward" else "Detail Order") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            order?.let { current ->
                item {
                    ElevatedCard(shape = RoundedCornerShape(20.dp)) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    current.orderNumber,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkGreen,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                StatusPill(current.orderStatus)
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                            DetailRow("Tanggal", current.createdAt)
                            DetailRow("Tipe", current.orderType.displayEnum())
                            if (!current.tableNumber.isNullOrBlank())
                                DetailRow("Nomor Meja", current.tableNumber)
                            if (!current.notes.isNullOrBlank())
                                DetailRow("Catatan", current.notes)
                            DetailRow("Pembayaran", current.paymentMethod.replace('_', ' '))
                            DetailRow("Status Bayar", current.paymentStatus.displayEnum())
                        }
                    }
                }
                if (items.isNotEmpty()) {
                    item {
                        Text(
                            "Item Pesanan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                    }
                    items(items) { orderItem ->
                        ElevatedCard(shape = RoundedCornerShape(14.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(orderItem.productNameSnapshot, fontWeight = FontWeight.SemiBold)
                                    if (orderItem.productPriceSnapshot > 0L)
                                        Text(
                                            "${orderItem.quantity} × ${Formatter.formatRupiah(orderItem.productPriceSnapshot)}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        )
                                }
                                if (orderItem.lineTotal > 0L)
                                    Text(
                                        Formatter.formatRupiah(orderItem.lineTotal),
                                        color = Brown,
                                        fontWeight = FontWeight.Bold
                                    )
                            }
                        }
                    }
                }
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = DarkGreen)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (current.total > 0L) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Total Pembayaran", color = Cream.copy(alpha = 0.8f))
                                    Text(
                                        Formatter.formatRupiah(current.total),
                                        color = Cream,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    if (current.pointsEarned < 0) "Poin Digunakan" else "Poin Diperoleh",
                                    color = Cream.copy(alpha = 0.8f)
                                )
                                Text(
                                    if (current.pointsEarned < 0) "${-current.pointsEarned} poin"
                                    else "+${current.pointsEarned} poin",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRewardsScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val rewards by viewModel.rewards.collectAsStateWithLifecycle()
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var confirmReward by remember { mutableStateOf<RewardEntity?>(null) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is AppEvent.Message) snackbarHostState.showSnackbar(event.text)
        }
    }

    confirmReward?.let { rw ->
        AlertDialog(
            onDismissRequest = { confirmReward = null },
            title = { Text("Tukar Reward?") },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProductImage(
                        rw.imageKey ?: "reward_espresso",
                        Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(14.dp))
                    )
                    Text(rw.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(
                        rw.description,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                        textAlign = TextAlign.Center
                    )
                    Surface(shape = RoundedCornerShape(50), color = DarkGreen.copy(alpha = 0.1f)) {
                        Text(
                            "${rw.pointCost} poin",
                            color = DarkGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                    Text(
                        "Poin kamu: ${user?.points ?: 0}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.redeemReward(rw.id)
                    confirmReward = null
                }) { Text("Ya, Tukar Sekarang") }
            },
            dismissButton = {
                TextButton(onClick = { confirmReward = null }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reward") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = DarkGreen),
                    elevation = CardDefaults.elevatedCardElevation(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(listOf(DarkGreen, MediumGreen))
                            )
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    "Poin Kamu",
                                    color = Cream.copy(alpha = 0.75f),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Row(
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "${user?.points ?: 0}",
                                        color = GoldAccent,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Text(
                                        "poin",
                                        color = Cream.copy(alpha = 0.7f),
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = tierColor(user?.points ?: 0).copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        Formatter.memberLevel(user?.points ?: 0),
                                        color = tierColor(user?.points ?: 0),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }
                            }
                            Icon(
                                Icons.Filled.CardGiftcard,
                                contentDescription = null,
                                tint = Cream.copy(alpha = 0.35f),
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    "Pilih Reward",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = DarkGreen
                )
            }
            items(rewards) { reward ->
                val canRedeem = reward.isAvailable && (user?.points ?: 0) >= reward.pointCost
                RewardCard(
                    reward = reward,
                    canRedeem = canRedeem,
                    onRedeem = { confirmReward = reward }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerProfileScreen(
    viewModel: CoffeeShopViewModel,
    onBack: () -> Unit,
    onMemberCard: () -> Unit,
    onRewards: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var editName by remember(user?.id) { mutableStateOf(user?.name.orEmpty()) }
    var editEmail by remember(user?.id) { mutableStateOf(user?.email.orEmpty()) }
    var editPhone by remember(user?.id) { mutableStateOf(user?.phone.orEmpty()) }
    var editPassword by remember { mutableStateOf("") }
    var editPasswordVisible by remember { mutableStateOf(false) }
    var showEditCard by remember { mutableStateOf(false) }
    var showLogoutConfirm by remember { mutableStateOf(false) }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Keluar?") },
            text = { Text("Kamu akan keluar dari akun ${user?.name.orEmpty()}.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.logout(); onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Ya, Keluar") }
            },
            dismissButton = { TextButton(onClick = { showLogoutConfirm = false }) { Text("Batal") } }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { if (it is AppEvent.Message) snackbarHostState.showSnackbar(it.text) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(DarkGreen, MediumGreen)))
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .background(Cream.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = null,
                                tint = Cream,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                        Text(
                            user?.name.orEmpty(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Cream
                        )
                        Text(
                            user?.email.orEmpty(),
                            color = Cream.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            "ID: ${Formatter.formatMemberId(user?.id ?: 0)}",
                            color = Cream.copy(alpha = 0.55f),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${user?.points ?: 0}",
                                    color = GoldAccent,
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text("Poin", color = Cream.copy(alpha = 0.65f), style = MaterialTheme.typography.bodySmall)
                            }
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(36.dp)
                                    .background(Cream.copy(alpha = 0.2f))
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    Formatter.memberLevel(user?.points ?: 0),
                                    color = tierColor(user?.points ?: 0),
                                    fontWeight = FontWeight.ExtraBold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text("Tier", color = Cream.copy(alpha = 0.65f), style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        val progress = tierProgress(user?.points ?: 0)
                        val nextTier = nextTierPoints(user?.points ?: 0)
                        if (nextTier != null) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(Cream.copy(alpha = 0.2f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                                            .height(6.dp)
                                            .background(GoldAccent)
                                    )
                                }
                                Text(
                                    "${user?.points ?: 0} / $nextTier poin menuju ${Formatter.memberLevel(nextTier)}",
                                    color = Cream.copy(alpha = 0.6f),
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(4.dp)) }
            item { ProfileNavCard(Icons.Filled.Star, "Kartu Member QR", "Lihat kartu dan kode QR kamu", onMemberCard) }
            item { ProfileNavCard(Icons.Filled.CardGiftcard, "Reward", "Tukar poin jadi minuman gratis", onRewards) }
            item { ProfileNavCard(Icons.AutoMirrored.Filled.ReceiptLong, "Riwayat Order", "Lihat semua transaksi kamu", onHistory) }
            item { Spacer(Modifier.height(4.dp)) }
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { showEditCard = !showEditCard },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(modifier = Modifier.size(38.dp).background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Filled.Person, null, tint = DarkGreen, modifier = Modifier.size(20.dp))
                                }
                                Text("Edit Profil", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                            }
                            Icon(
                                if (showEditCard) Icons.Filled.Remove else Icons.Filled.Add,
                                null, tint = DarkGreen.copy(alpha = 0.5f), modifier = Modifier.size(20.dp)
                            )
                        }
                        if (showEditCard) {
                            OutlinedTextField(value = editName, onValueChange = { editName = it }, label = { Text("Nama") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                            OutlinedTextField(value = editEmail, onValueChange = { editEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
                            OutlinedTextField(value = editPhone, onValueChange = { editPhone = it }, label = { Text("No HP") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))
                            OutlinedTextField(
                                value = editPassword, onValueChange = { editPassword = it },
                                label = { Text("Password baru (kosongkan jika tidak diubah)") },
                                modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp),
                                visualTransformation = if (editPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { editPasswordVisible = !editPasswordVisible }) {
                                        Icon(if (editPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, null)
                                    }
                                }
                            )
                            Button(
                                onClick = { viewModel.updateProfile(editName, editEmail, editPhone, editPassword.ifBlank { null }); editPassword = "" },
                                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Filled.Save, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Simpan Profil")
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                Button(
                    onClick = { showLogoutConfirm = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Keluar dari Akun", style = MaterialTheme.typography.titleSmall)
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerMemberCardScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val qrBitmap = remember(user?.id, user?.points) {
        val payload = "${Formatter.formatMemberId(user?.id ?: 0)}|${user?.points ?: 0}"
        QrCodeGenerator.generateQrBitmap(payload)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kartu Member") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali")
                    }
                },
                colors = appBarColors()
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightCream)
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(DarkGreen, MediumGreen, Color(0xFF1A3020))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "COFFEE BLISS",
                                        color = Cream,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.titleMedium,
                                        letterSpacing = 2.sp
                                    )
                                    Text(
                                        "MEMBER CARD",
                                        color = Cream.copy(alpha = 0.55f),
                                        style = MaterialTheme.typography.labelSmall,
                                        letterSpacing = 1.5.sp
                                    )
                                }
                                Icon(
                                    Icons.Filled.LocalCafe,
                                    contentDescription = null,
                                    tint = GoldAccent,
                                    modifier = Modifier.size(34.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(188.dp)
                                    .background(Color.White, RoundedCornerShape(18.dp))
                                    .padding(12.dp)
                            ) {
                                Image(
                                    bitmap = qrBitmap.asImageBitmap(),
                                    contentDescription = "QR Member",
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    user?.name.orEmpty(),
                                    color = Cream,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "ID: ${Formatter.formatMemberId(user?.id ?: 0)}",
                                    color = Cream.copy(alpha = 0.65f),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            HorizontalDivider(color = Cream.copy(alpha = 0.18f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "POIN",
                                        color = Cream.copy(alpha = 0.55f),
                                        style = MaterialTheme.typography.labelSmall,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        "${user?.points ?: 0}",
                                        color = GoldAccent,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(40.dp)
                                        .background(Cream.copy(alpha = 0.2f))
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        "TIER",
                                        color = Cream.copy(alpha = 0.55f),
                                        style = MaterialTheme.typography.labelSmall,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        Formatter.memberLevel(user?.points ?: 0).uppercase(),
                                        color = GoldAccent,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    "Tunjukkan QR ini saat transaksi untuk tukar reward",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: CoffeeShopViewModel,
    onProducts: () -> Unit,
    onUsers: () -> Unit,
    onOrders: () -> Unit,
    onRewardsSettings: () -> Unit,
    onProfile: () -> Unit,
    onLogout: () -> Unit
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    var showLogoutConfirm by remember { mutableStateOf(false) }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Keluar dari Admin Panel?") },
            text = { Text("Sesi admin akan diakhiri.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.logout(); onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Ya, Keluar") }
            },
            dismissButton = { TextButton(onClick = { showLogoutConfirm = false }) { Text("Batal") } }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                actions = {
                    IconButton(onClick = onProfile) { Icon(Icons.Filled.Person, "Profil") }
                    IconButton(onClick = { showLogoutConfirm = true }) { Icon(Icons.AutoMirrored.Filled.Logout, "Logout") }
                },
                colors = appBarColors()
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().background(LightCream).padding(padding),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(Brush.verticalGradient(listOf(DarkGreen, MediumGreen)))
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Selamat datang,", color = Cream.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
                        Text(user?.name ?: "Admin", color = Cream, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(user?.email ?: "", color = Cream.copy(alpha = 0.55f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ringkasan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkGreen)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MetricCard("Produk Aktif", products.count { it.isAvailable }.toString(), Icons.Filled.Coffee, Modifier.weight(1f))
                        MetricCard("Total User", users.size.toString(), Icons.Filled.People, Modifier.weight(1f))
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        MetricCard("Total Order", orders.size.toString(), Icons.AutoMirrored.Filled.ReceiptLong, Modifier.weight(1f))
                        MetricCard("Sudah Bayar", orders.count { it.paymentStatus == "PAID" }.toString(), Icons.Filled.Payment, Modifier.weight(1f))
                    }
                }
            }
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Kelola", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkGreen)
                    AdminNavButton(Icons.Filled.Store, "Produk", "Tambah, edit, nonaktifkan produk", onProducts)
                    AdminNavButton(Icons.Filled.People, "User & Poin", "Kelola akun dan adjust poin", onUsers)
                    AdminNavButton(Icons.AutoMirrored.Filled.ReceiptLong, "Order", "Pantau dan update status order", onOrders)
                    AdminNavButton(Icons.Filled.Settings, "Reward & Membership", "Atur reward dan tier membership", onRewardsSettings)
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProductsScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val products by viewModel.products.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { viewModel.events.collect { if (it is AppEvent.Message) snackbarHostState.showSnackbar(it.text) } }
    Scaffold(topBar = { TopAppBarWithBack("Kelola Produk", onBack) }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(LightCream).padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)) {
            items(products) { product -> ProductAdminRow(product = product, onSave = viewModel::saveProduct, onDelete = { viewModel.deleteProduct(product.id) }) }
            item { FloatingAddProductCard { viewModel.saveProduct(it) } }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val users by viewModel.users.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { viewModel.events.collect { if (it is AppEvent.Message) snackbarHostState.showSnackbar(it.text) } }
    Scaffold(topBar = { TopAppBarWithBack("Kelola User", onBack) }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(LightCream).padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)) {
            if (users.isEmpty()) item { EmptyState(Icons.Filled.People, "Belum ada user", "User yang terdaftar akan muncul di sini.") }
            items(users) { user -> UserAdminRow(user = user, onAdjust = { delta, reason -> viewModel.adjustUserPoints(user.id, delta, reason) }, onToggle = { viewModel.setUserActive(user.id, !user.isActive) }) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    Scaffold(topBar = { TopAppBarWithBack("Kelola Order", onBack) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(LightCream).padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)) {
            if (orders.isEmpty()) item { EmptyState(Icons.AutoMirrored.Filled.ReceiptLong, "Belum ada order", "Order dari pelanggan akan muncul di sini.") }
            items(orders) { order ->
                ElevatedCard(shape = RoundedCornerShape(18.dp)) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(order.orderNumber, fontWeight = FontWeight.Bold, color = DarkGreen, style = MaterialTheme.typography.titleSmall)
                            StatusPill(order.orderStatus)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(order.orderType.displayEnum(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text(order.createdAt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
                            }
                            if (order.total > 0L) Text(Formatter.formatRupiah(order.total), fontWeight = FontWeight.Bold, color = Brown)
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
                        Text("Ubah Status", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(OrderStatus.entries) { status ->
                                FilterChip(selected = order.orderStatus == status.name, onClick = { viewModel.updateOrderStatus(order.id, status) }, label = { Text(status.name.displayEnum(), style = MaterialTheme.typography.labelSmall) })
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRewardsSettingsScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit) {
    val rewards by viewModel.rewards.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) { viewModel.events.collect { if (it is AppEvent.Message) snackbarHostState.showSnackbar(it.text) } }
    Scaffold(topBar = { TopAppBarWithBack("Reward & Membership", onBack) }, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(LightCream).padding(padding), verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)) {
            item { settings?.let { MembershipSettingsCard(it, onSave = viewModel::saveSettings) } }
            item { Text("Katalog Reward", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkGreen) }
            items(rewards) { reward -> RewardAdminRow(reward = reward, onSave = viewModel::saveReward) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(viewModel: CoffeeShopViewModel, onBack: () -> Unit, onLogout: () -> Unit) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    var showLogoutConfirm by remember { mutableStateOf(false) }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            title = { Text("Keluar dari Admin Panel?") },
            text = { Text("Sesi admin akan diakhiri. Kamu harus login kembali untuk mengakses panel admin.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.logout(); onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Ya, Keluar") }
            },
            dismissButton = { TextButton(onClick = { showLogoutConfirm = false }) { Text("Batal") } }
        )
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profil Admin") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") } }, colors = appBarColors()) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().background(LightCream).padding(padding), verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(24.dp)) {
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(DarkGreen, MediumGreen)), RoundedCornerShape(20.dp)).padding(24.dp)) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(modifier = Modifier.size(64.dp).background(Cream.copy(alpha = 0.15f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Person, null, tint = Cream, modifier = Modifier.size(38.dp))
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(user?.name ?: "Admin", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Cream)
                        Text(user?.email ?: "", style = MaterialTheme.typography.bodyMedium, color = Cream.copy(alpha = 0.72f))
                        Surface(shape = RoundedCornerShape(50), color = Cream.copy(alpha = 0.15f)) {
                            Text("Role: ${user?.role ?: "ADMIN"}", color = Cream, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp))
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = { showLogoutConfirm = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(10.dp))
                    Text("Keluar dari Admin Panel", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}


@Composable
private fun HeroCard(user: User?, onRewards: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(DarkGreen, MediumGreen))).padding(horizontal = 20.dp, vertical = 28.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Halo, ${user?.name?.split(" ")?.firstOrNull() ?: "Coffee Lover"}!", style = MaterialTheme.typography.titleLarge, color = Cream, fontWeight = FontWeight.Bold)
            Text("Mau minum apa hari ini?", style = MaterialTheme.typography.bodyMedium, color = Cream.copy(alpha = 0.7f))
            Spacer(Modifier.height(14.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = Cream.copy(alpha = 0.11f), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(38.dp).background(GoldAccent.copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.Star, null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("${user?.points ?: 0} poin", color = Cream, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                            Text(Formatter.memberLevel(user?.points ?: 0), color = Cream.copy(alpha = 0.65f), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Surface(shape = RoundedCornerShape(10.dp), color = Brown, onClick = onRewards) {
                        Text("Tukar Reward", color = Cream, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.width(180.dp).clickable(onClick = onClick), shape = RoundedCornerShape(20.dp), elevation = CardDefaults.elevatedCardElevation(4.dp)) {
        Column {
            Box {
                ProductImage(imageKey = product.imageKey, modifier = Modifier.fillMaxWidth().height(136.dp))
                if (product.isFeatured) {
                    Surface(shape = RoundedCornerShape(bottomEnd = 10.dp), color = GoldAccent, modifier = Modifier.align(Alignment.TopStart)) {
                        Text("Favorit", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
                if (!product.isAvailable) {
                    Box(modifier = Modifier.fillMaxWidth().height(136.dp).background(Color.Black.copy(alpha = 0.45f)), contentAlignment = Alignment.Center) {
                        Text("Habis", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.titleSmall)
                Text(product.category, style = MaterialTheme.typography.bodySmall, color = DarkGreen.copy(alpha = 0.7f), maxLines = 1)
                Text(Formatter.formatRupiah(product.price), color = Brown, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun ProductWideCard(product: Product, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp), elevation = CardDefaults.elevatedCardElevation(3.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box {
                ProductImage(product.imageKey, Modifier.size(100.dp).clip(RoundedCornerShape(14.dp)))
                if (!product.isAvailable) {
                    Box(modifier = Modifier.size(100.dp).clip(RoundedCornerShape(14.dp)).background(Color.Black.copy(alpha = 0.45f)), contentAlignment = Alignment.Center) {
                        Text("Habis", color = Color.White, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(product.category, style = MaterialTheme.typography.bodySmall, color = DarkGreen, fontWeight = FontWeight.Medium)
                Text(product.description, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text(Formatter.formatRupiah(product.price), color = Brown, fontWeight = FontWeight.ExtraBold, style = MaterialTheme.typography.labelLarge)
            }
            Icon(Icons.Filled.AddShoppingCart, null, tint = DarkGreen, modifier = Modifier.size(22.dp))
        }
    }
}

@Composable
private fun RewardCard(reward: RewardEntity, canRedeem: Boolean, onRedeem: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.elevatedCardElevation(3.dp)) {
        Column {
            Box {
                ProductImage(reward.imageKey ?: "reward_espresso", Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)))
                Surface(shape = RoundedCornerShape(bottomStart = 12.dp), color = if (canRedeem) DarkGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f), modifier = Modifier.align(Alignment.TopEnd)) {
                    Text("${reward.pointCost} poin", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(reward.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(reward.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                Button(onClick = onRedeem, enabled = canRedeem, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Filled.CardGiftcard, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(8.dp))
                    Text(if (canRedeem) "Tukar Reward" else "Poin Tidak Cukup")
                }
            }
        }
    }
}

@Composable
private fun ProductImage(imageKey: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = remember(imageKey) { context.resources.getIdentifier(imageKey, "drawable", context.packageName) }
    if (resId != 0) {
        Image(painter = painterResource(resId), contentDescription = imageKey, modifier = modifier, contentScale = ContentScale.Crop)
    } else {
        Box(modifier = modifier.background(Cream), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.LocalCafe, null, tint = Brown, modifier = Modifier.size(44.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CustomerScaffold(
    title: String, selected: String,
    onHome: () -> Unit, onProducts: () -> Unit, onCart: () -> Unit,
    onHistory: () -> Unit, onProfile: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor = DarkGreen, selectedTextColor = DarkGreen,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
        indicatorColor = DarkGreen.copy(alpha = 0.12f)
    )
    Scaffold(
        topBar = { TopAppBarSmall(title) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(selected == "Beranda", onHome, { Icon(Icons.Filled.Home, "Beranda") }, label = { Text("Beranda") }, colors = navColors)
                NavigationBarItem(selected == "Menu", onProducts, { Icon(Icons.Filled.Coffee, "Menu") }, label = { Text("Menu") }, colors = navColors)
                NavigationBarItem(selected == "Keranjang", onCart, { Icon(Icons.Filled.ShoppingCart, "Keranjang") }, label = { Text("Keranjang") }, colors = navColors)
                NavigationBarItem(selected == "Riwayat", onHistory, { Icon(Icons.AutoMirrored.Filled.List, "Riwayat") }, label = { Text("Riwayat") }, colors = navColors)
                NavigationBarItem(selected == "Profil", onProfile, { Icon(Icons.Filled.Person, "Profil") }, label = { Text("Profil") }, colors = navColors)
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarSmall(title: String) = TopAppBar(title = { Text(title) }, colors = appBarColors())

@Composable
private fun SectionHeader(title: String, action: String?, onAction: (() -> Unit)?) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = DarkGreen)
        if (action != null && onAction != null) TextButton(onClick = onAction) { Text(action) }
    }
}

@Composable
private fun QuantityRow(quantity: Int, onMinus: () -> Unit, onPlus: () -> Unit) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
            IconButton(onClick = onMinus, modifier = Modifier.size(36.dp)) { Icon(Icons.Filled.Remove, "Kurangi", modifier = Modifier.size(18.dp)) }
            Text(quantity.toString(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.width(28.dp), textAlign = TextAlign.Center)
            IconButton(onClick = onPlus, modifier = Modifier.size(36.dp)) { Icon(Icons.Filled.Add, "Tambah", modifier = Modifier.size(18.dp)) }
        }
    }
}

@Composable
private fun CartRow(item: CartItem, product: Product?, onMinus: () -> Unit, onPlus: () -> Unit, onRemove: () -> Unit) {
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (product != null) ProductImage(product.imageKey, Modifier.size(76.dp).clip(RoundedCornerShape(12.dp)))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(product?.name ?: "Produk", fontWeight = FontWeight.SemiBold)
                Text(Formatter.formatRupiah((product?.price ?: 0L) * item.quantity), color = Brown, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                QuantityRow(item.quantity, onMinus, onPlus)
            }
            IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
private fun CheckoutCard(
    subtotal: Long, orderType: OrderType, onOrderTypeChange: (OrderType) -> Unit,
    paymentMethod: PaymentMethod, onPaymentMethodChange: (PaymentMethod) -> Unit,
    tableNumber: String, onTableNumberChange: (String) -> Unit,
    notes: String, onNotesChange: (String) -> Unit, onPay: () -> Unit
) {
    ElevatedCard(shape = RoundedCornerShape(22.dp), elevation = CardDefaults.elevatedCardElevation(6.dp)) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Ringkasan Pesanan", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = DarkGreen)
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Text("Tipe Order", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = orderType == OrderType.TAKE_AWAY, onClick = { onOrderTypeChange(OrderType.TAKE_AWAY) }, label = { Text("Take Away") })
                FilterChip(selected = orderType == OrderType.DINE_IN, onClick = { onOrderTypeChange(OrderType.DINE_IN) }, label = { Text("Dine In") })
            }
            if (orderType == OrderType.DINE_IN) {
                OutlinedTextField(value = tableNumber, onValueChange = onTableNumberChange, label = { Text("Nomor Meja") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
            }
            OutlinedTextField(value = notes, onValueChange = onNotesChange, label = { Text("Catatan (opsional)") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            Text("Metode Bayar", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PaymentMethod.entries) { method ->
                    FilterChip(selected = paymentMethod == method, onClick = { onPaymentMethodChange(method) }, label = { Text(method.name.replace('_', ' ')) })
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(Formatter.formatRupiah(subtotal), fontWeight = FontWeight.ExtraBold, color = Brown, style = MaterialTheme.typography.titleMedium)
            }
            Button(onClick = onPay, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
                Icon(Icons.Filled.Payment, null, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp))
                Text("Bayar Sekarang", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}

@Composable
private fun OrderCard(order: Order, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(46.dp).background(DarkGreen.copy(alpha = 0.08f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.ReceiptLong, null, tint = DarkGreen, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(order.orderNumber, fontWeight = FontWeight.Bold, color = DarkGreen)
                Text("${order.orderType.displayEnum()} · ${order.orderStatus.displayEnum()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f))
                Text(order.createdAt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (order.total > 0L) Text(Formatter.formatRupiah(order.total), color = Brown, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text(if (order.pointsEarned < 0) "-${-order.pointsEarned} poin" else "+${order.pointsEarned} poin", color = if (order.pointsEarned < 0) Brown else DarkGreen, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, emphasized: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
        Text(label, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), modifier = Modifier.weight(0.9f), style = MaterialTheme.typography.bodyMedium)
        Text(value, fontWeight = if (emphasized) FontWeight.Bold else FontWeight.Normal, color = if (emphasized) DarkGreen else MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1.1f), maxLines = 3, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusPill(status: String) {
    val bg = when (status) { OrderStatus.COMPLETED.name -> DarkGreen.copy(alpha = 0.13f); OrderStatus.CANCELLED.name -> MaterialTheme.colorScheme.error.copy(alpha = 0.13f); else -> Brown.copy(alpha = 0.13f) }
    val fg = when (status) { OrderStatus.CANCELLED.name -> MaterialTheme.colorScheme.error; else -> DarkGreen }
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Text(status.displayEnum(), color = fg, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
    }
}

private fun String.displayEnum(): String =
    lowercase().split("_").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

@Composable
private fun EmptyState(icon: ImageVector, title: String, body: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Box(modifier = Modifier.size(80.dp).background(DarkGreen.copy(alpha = 0.08f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = DarkGreen.copy(alpha = 0.5f), modifier = Modifier.size(42.dp))
        }
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(body, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun ProfileNavCard(icon: ImageVector, label: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable(onClick = onClick), shape = RoundedCornerShape(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(modifier = Modifier.size(44.dp).background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.52f))
            }
            Icon(Icons.AutoMirrored.Filled.List, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f), modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun MetricCard(label: String, value: String, icon: ImageVector, modifier: Modifier) {
    ElevatedCard(modifier = modifier.height(120.dp), shape = RoundedCornerShape(18.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier.size(36.dp).background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = DarkGreen)
                Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
    }
}

@Composable
private fun AdminNavButton(icon: ImageVector, label: String, subtitle: String, onClick: () -> Unit) {
    ElevatedCard(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(18.dp)) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Box(modifier = Modifier.size(44.dp).background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = DarkGreen, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(label, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
            }
            Icon(Icons.AutoMirrored.Filled.List, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.22f), modifier = Modifier.size(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarWithBack(title: String, onBack: () -> Unit) {
    TopAppBar(title = { Text(title) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Kembali") } }, colors = appBarColors())
}

@Composable
private fun ProductAdminRow(product: Product, onSave: (Product) -> Unit, onDelete: () -> Unit) {
    var price by remember(product.id) { mutableStateOf(product.price.toString()) }
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProductImage(product.imageKey, Modifier.size(64.dp).clip(RoundedCornerShape(12.dp)))
                Column(modifier = Modifier.weight(1f)) {
                    Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                    Text(product.category, style = MaterialTheme.typography.bodySmall, color = DarkGreen)
                }
                IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, "Hapus", tint = MaterialTheme.colorScheme.error) }
            }
            OutlinedTextField(value = price, onValueChange = { price = it.filter(Char::isDigit) }, label = { Text("Harga") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), shape = RoundedCornerShape(12.dp), singleLine = true)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                FilterChip(selected = product.isAvailable, onClick = { onSave(product.copy(isAvailable = !product.isAvailable)) }, label = { Text(if (product.isAvailable) "Tersedia" else "Nonaktif") }, modifier = Modifier.weight(1f))
                FilterChip(selected = product.isFeatured, onClick = { onSave(product.copy(isFeatured = !product.isFeatured)) }, label = { Text("Unggulan") }, modifier = Modifier.weight(1f))
            }
            Button(onClick = { onSave(product.copy(price = price.toLongOrNull() ?: product.price)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Filled.Save, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Simpan Perubahan")
            }
        }
    }
}

@Composable
private fun FloatingAddProductCard(onSave: (Product) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    ElevatedCard(modifier = Modifier.fillMaxWidth().clickable { showDialog = true }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.elevatedCardColors(containerColor = DarkGreen.copy(alpha = 0.07f))) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.size(36.dp).background(DarkGreen, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Filled.Add, null, tint = Cream, modifier = Modifier.size(20.dp))
            }
            Text("Tambah Produk Baru", fontWeight = FontWeight.Bold, color = DarkGreen)
        }
    }
    if (showDialog) ProductDialog(onDismiss = { showDialog = false }, onSave = { onSave(it); showDialog = false })
}

@Composable
private fun ProductDialog(onDismiss: () -> Unit, onSave: (Product) -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Hot Coffee") }
    val categories = listOf("Hot Coffee", "Milk Coffee", "Signature", "Non Coffee", "Cold Coffee", "Frappe", "Pastry")
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Produk Baru") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama Produk") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                OutlinedTextField(value = price, onValueChange = { price = it.filter(Char::isDigit) }, label = { Text("Harga") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), singleLine = true, shape = RoundedCornerShape(12.dp))
                Text("Kategori", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat -> FilterChip(selected = category == cat, onClick = { category = cat }, label = { Text(cat) }) }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onSave(Product(name = name.ifBlank { "Produk Baru" }, description = description.ifBlank { "Produk baru Coffee Bliss." }, category = category, price = price.toLongOrNull() ?: 0L, imageKey = "product_cafe_latte", updatedAt = "Baru")) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

@Composable
private fun UserAdminRow(user: User, onAdjust: (Int, String) -> Unit, onToggle: () -> Unit) {
    var delta by remember(user.id) { mutableStateOf("") }
    var reason by remember(user.id) { mutableStateOf("") }
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.size(44.dp).background(DarkGreen.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Person, null, tint = DarkGreen, modifier = Modifier.size(24.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(user.name, fontWeight = FontWeight.Bold)
                    Text(user.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Surface(shape = RoundedCornerShape(50), color = if (user.isActive) DarkGreen.copy(alpha = 0.1f) else MaterialTheme.colorScheme.error.copy(alpha = 0.1f)) {
                    Text(if (user.isActive) "Aktif" else "Nonaktif", color = if (user.isActive) DarkGreen else MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }
            Text("${user.role} · ${user.points} poin", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
            OutlinedTextField(value = delta, onValueChange = { delta = it.filter { c -> c.isDigit() || c == '-' } }, label = { Text("Tambah / kurangi poin") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = reason, onValueChange = { reason = it }, label = { Text("Alasan") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onAdjust(delta.toIntOrNull() ?: 0, reason) }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Sesuaikan Poin") }
                OutlinedButton(onClick = onToggle, enabled = user.role != UserRole.ADMIN.name, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text(if (user.isActive) "Nonaktifkan" else "Aktifkan") }
            }
        }
    }
}

@Composable
private fun RewardAdminRow(reward: RewardEntity, onSave: (RewardEntity) -> Unit) {
    var cost by remember(reward.id) { mutableStateOf(reward.pointCost.toString()) }
    ElevatedCard(shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ProductImage(reward.imageKey ?: "product_espresso", Modifier.size(68.dp).clip(RoundedCornerShape(12.dp)))
                Column(modifier = Modifier.weight(1f)) {
                    Text(reward.name, fontWeight = FontWeight.Bold)
                    Text(reward.description, maxLines = 2, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f))
                }
            }
            OutlinedTextField(value = cost, onValueChange = { cost = it.filter(Char::isDigit) }, label = { Text("Biaya poin") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Button(onClick = { onSave(reward.copy(pointCost = cost.toIntOrNull() ?: reward.pointCost)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Filled.Save, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Simpan Reward")
            }
        }
    }
}

@Composable
private fun MembershipSettingsCard(settings: MembershipSettings, onSave: (MembershipSettings) -> Unit) {
    var rupiahPerPoint by remember(settings.id) { mutableStateOf(settings.rupiahPerPoint.toString()) }
    var gold by remember(settings.id) { mutableStateOf(settings.goldMin.toString()) }
    var platinum by remember(settings.id) { mutableStateOf(settings.platinumMin.toString()) }
    ElevatedCard(shape = RoundedCornerShape(18.dp)) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(modifier = Modifier.size(40.dp).background(DarkGreen.copy(alpha = 0.1f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Star, null, tint = DarkGreen, modifier = Modifier.size(22.dp))
                }
                Text("Pengaturan Membership", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
            OutlinedTextField(value = rupiahPerPoint, onValueChange = { rupiahPerPoint = it.filter(Char::isDigit) }, label = { Text("Rupiah per poin") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = gold, onValueChange = { gold = it.filter(Char::isDigit) }, label = { Text("Gold minimum (poin)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = platinum, onValueChange = { platinum = it.filter(Char::isDigit) }, label = { Text("Platinum minimum (poin)") }, modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(12.dp), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            Button(onClick = { onSave(settings.copy(rupiahPerPoint = rupiahPerPoint.toLongOrNull() ?: 10_000L, goldMin = gold.toIntOrNull() ?: 100, platinumMin = platinum.toIntOrNull() ?: 250)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Text("Simpan Membership")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun appBarColors() = TopAppBarDefaults.topAppBarColors(
    containerColor = DarkGreen, titleContentColor = Color.White,
    navigationIconContentColor = Color.White, actionIconContentColor = Color.White
)

private fun tierColor(points: Int): Color = when {
    points >= 250 -> PlatinumAccent
    points >= 100 -> GoldAccent
    else -> SilverAccent
}

private fun tierProgress(points: Int): Float = when {
    points >= 250 -> 1f
    points >= 100 -> (points - 100) / 150f
    else -> points / 100f
}

private fun nextTierPoints(points: Int): Int? = when {
    points >= 250 -> null
    points >= 100 -> 250
    else -> 100
}
