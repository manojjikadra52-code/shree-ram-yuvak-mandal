package com.example

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import com.example.data.*
import com.example.ui.MandalViewModel
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay

// High Density Design Theme Color Palette with Divine Shree Ram Saffron & Gold Theme
val DenseBackground = SaffronBackgroundWarm
val DenseSurface = Color.White
val DensePrimary = SaffronDeepOrange       // Holy rich saffron orange
val DenseOnPrimary = Color.White
val DenseContainer = SaffronLightOrange      // Warm soft cream
val DenseBorder = SaffronBorderColor        // Soft saffron border/divider
val DenseActiveContainer = SaffronBorderColor // Active navigation highlight
val DenseActiveOnContainer = SaffronSecondaryText
val DenseLabelText = SaffronDarkSlate
val DenseTitleText = SaffronTitleText

val DenseCardFundBg = SaffronLightOrange    // Saffron background
val DenseFundText = SaffronSecondaryText

val DenseIncomeText = Color(0xFF2E7D32)     // Pure green income (Aavak)
val DenseExpenseText = Color(0xFFC62828)    // Deep vermillion/crimson expense (Javak)

// Compatibility mappings for existing views
val SaffronPrimary = DensePrimary
val SaffronSecondary = DenseActiveContainer
val SaffronBackground = DenseBackground
val SaffronSurface = DenseSurface
val SaffronAccent = DenseExpenseText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MandalApp()
            }
        }
    }
}

@Composable
fun LoginScreen(viewModel: MandalViewModel) {
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFF9800).copy(alpha = 0.30f),
                        Color(0xFFFFF3E0).copy(alpha = 0.20f),
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Celestial Shree Ram Bow & Arrow (Dhanush and Baan) Spiritual Watermark Logo in the upper empty space
        Canvas(modifier = Modifier.fillMaxSize()) {
            val drawScopeSize = this.size
            val w = drawScopeSize.width
            val h = drawScopeSize.height
            val minDim = drawScopeSize.minDimension

            // Draw a soft spiritual radiant Suryadev sun in the upper region (above the card)
            drawCircle(
                color = Color(0xFFFF9800),
                radius = minDim * 0.22f,
                center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.20f),
                alpha = 0.20f
            )
            
            // Draw concentric soft spiritual aura rings
            for (i in 1..4) {
                drawCircle(
                    color = Color(0xFFE65100),
                    radius = minDim * (0.22f + i * 0.06f),
                    center = androidx.compose.ui.geometry.Offset(w * 0.5f, h * 0.20f),
                    alpha = 0.04f,
                    style = Stroke(width = 1.dp.toPx())
                )
            }

            // Draw dynamic diagonal Bow (Dhanush) of Shree Ram pointing elegantly right-upwards
            val bowPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.20f, h * 0.28f)
                cubicTo(
                    w * 0.15f, h * 0.10f,
                    w * 0.55f, h * 0.05f,
                    w * 0.75f, h * 0.15f
                )
            }
            
            drawPath(
                path = bowPath,
                color = Color(0xFFE65100),
                alpha = 0.28f,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = androidx.compose.ui.graphics.StrokeCap.Round
                )
            )

            // Draw Bow String (Pratyancha) - connecting the bow ends
            drawLine(
                color = Color(0xFFE65100),
                start = androidx.compose.ui.geometry.Offset(w * 0.20f, h * 0.28f),
                end = androidx.compose.ui.geometry.Offset(w * 0.75f, h * 0.15f),
                alpha = 0.22f,
                strokeWidth = 1.5.dp.toPx()
            )

            // Draw Arrow (Baan) resting on string pointing majestically right-upwards
            drawLine(
                color = Color(0xFFFF5722),
                start = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.32f),
                end = androidx.compose.ui.geometry.Offset(w * 0.82f, h * 0.11f),
                alpha = 0.35f,
                strokeWidth = 3.5.dp.toPx(),
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )

            // Arrow head point
            val arrowHead = androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.82f, h * 0.11f)
                lineTo(w * 0.76f, h * 0.10f)
                lineTo(w * 0.79f, h * 0.15f)
                close()
            }
            drawPath(
                path = arrowHead,
                color = Color(0xFFFF5722),
                alpha = 0.38f
            )

            // Arrow feathers (fletching)
            drawLine(
                color = Color(0xFFFF5722),
                start = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.32f),
                end = androidx.compose.ui.geometry.Offset(w * 0.32f, h * 0.32f),
                alpha = 0.25f,
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = Color(0xFFFF5722),
                start = androidx.compose.ui.geometry.Offset(w * 0.35f, h * 0.32f),
                end = androidx.compose.ui.geometry.Offset(w * 0.34f, h * 0.35f),
                alpha = 0.25f,
                strokeWidth = 2.dp.toPx()
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .widthIn(max = 420.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            border = BorderStroke(1.5.dp, Color(0xFFFFB74D).copy(alpha = 0.6f))
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Sacred Shree Ram Dhanush Watermark Canvas INSIDE the Card behind inputs
                Canvas(modifier = Modifier.matchParentSize()) {
                    val w = size.width
                    val h = size.height
                    
                    // Center centered subtle Bow curves
                    val bowPathCard = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.18f, h * 0.35f)
                        cubicTo(
                            w * 0.82f, h * 0.38f,
                            w * 0.82f, h * 0.72f,
                            w * 0.18f, h * 0.75f
                        )
                    }
                    drawPath(
                        path = bowPathCard,
                        color = Color(0xFFE65100),
                        alpha = 0.11f,
                        style = Stroke(
                            width = 3.dp.toPx(),
                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                        )
                    )
                    
                    // Bow string (Pratyancha) inside card
                    drawLine(
                        color = Color(0xFFE65100),
                        start = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.35f),
                        end = androidx.compose.ui.geometry.Offset(w * 0.18f, h * 0.75f),
                        alpha = 0.08f,
                        strokeWidth = 1.dp.toPx()
                    )
                    
                    // Arrow (Baan) inside card
                    drawLine(
                        color = Color(0xFFFF5722),
                        start = androidx.compose.ui.geometry.Offset(w * 0.12f, h * 0.55f),
                        end = androidx.compose.ui.geometry.Offset(w * 0.86f, h * 0.55f),
                        alpha = 0.15f,
                        strokeWidth = 2.5.dp.toPx(),
                        cap = androidx.compose.ui.graphics.StrokeCap.Round
                    )
                    
                    // Arrow head inside card
                    val arrowHeadCard = androidx.compose.ui.graphics.Path().apply {
                        moveTo(w * 0.86f, h * 0.55f)
                        lineTo(w * 0.78f, h * 0.51f)
                        lineTo(w * 0.78f, h * 0.59f)
                        close()
                    }
                    drawPath(
                        path = arrowHeadCard,
                        color = Color(0xFFFF5722),
                        alpha = 0.18f
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(28.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Elegant Glowing Saffron Emblem Circle
                    Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color(0xFFFFF3E0), CircleShape)
                        .border(BorderStroke(1.5.dp, Color(0xFFFF9800).copy(alpha = 0.4f)), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🕉️", fontSize = 32.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "શ્રી રામ યુવક મંડળ",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp,
                        color = Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Shree Ram Yuvak Mandal",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFFF57C00),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "Mandal Management System (Hisab)",
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }

                Divider(color = Color(0xFFFFE0B2), thickness = 1.dp)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "લૉગિન વિગતો દાખલ કરો (Enter Details)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )

                    OutlinedTextField(
                        value = loginId,
                        onValueChange = {
                            loginId = it
                            showError = false
                        },
                        label = { Text("આઈડી (ID)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFFFF9800)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_id_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color(0xFFFF9800)
                        )
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            showError = false
                        },
                        label = { Text("પાસવર્ડ (Password)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFFFF9800)
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_password_input"),
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedBorderColor = Color(0xFFFF9800),
                            unfocusedBorderColor = Color.LightGray,
                            focusedLabelColor = Color(0xFFFF9800)
                        )
                    )
                }

                if (showError) {
                    Text(
                        text = "ખોટો આઈડી અથવા પાસવર્ડ! (Invalid ID or Password)",
                        color = Color(0xFFC62828),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }

                Button(
                    onClick = {
                        val success = viewModel.login(loginId.trim(), password.trim())
                        if (!success) {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = "લોગિન (Login)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "MANOJ JIKADRA",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp,
                    color = Color(0xFFE65100),
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}
}

@Composable
fun MandalApp(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel: MandalViewModel = viewModel()

    if (!viewModel.isLoggedIn) {
        LoginScreen(viewModel = viewModel)
        return
    }
    
    val members by viewModel.members.collectAsStateWithLifecycle()
    val payments by viewModel.payments.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()

    var currentTab by remember { mutableIntStateOf(0) }
    var ledgerTab by remember { mutableIntStateOf(0) }

    // Dialog control states
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showAddPaymentDialog by remember { mutableStateOf(false) }
    var selectedMemberForDetail by remember { mutableStateOf<Member?>(null) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showProfileDialog by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = DenseContainer,
                modifier = Modifier.width(300.dp)
            ) {
                // Header Section - Lord Shree Ram Dhanush banner styled nicely
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFFF9800), Color(0xFFE65100))
                            )
                        )
                        .padding(vertical = 24.dp, horizontal = 16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.White.copy(alpha = 0.25f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🕉️", fontSize = 24.sp)
                            }
                            Column {
                                Text(
                                    text = "શ્રી રામ યુવક મંડળ",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Shree Ram Yuvak Mandal",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                        
                        HorizontalDivider(color = Color.White.copy(alpha = 0.3f), thickness = 1.dp)

                        val userName = if (viewModel.isAdmin) "admin" else "user (123)"
                        val userRoleLabel = if (viewModel.isAdmin) "એડમિનિસ્ટ્રેટર (Administrator)" else "સામાન્ય સભ્ય (Member)"
                        Column {
                            Text(
                                text = "સક્રિય લોગિન આઈડી: $userName",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "રોલ: $userRoleLabel",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Drawer items
                // 1. Profile item
                NavigationDrawerItem(
                    label = { Text("સભ્ય પ્રોફાઇલ (Profile)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        showProfileDialog = true
                    },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color(0xFFE65100)) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black,
                        unselectedIconColor = Color(0xFFE65100)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).testTag("drawer_profile_item")
                )

                // 2. Members Directory item
                NavigationDrawerItem(
                    label = { Text("સભ્યોની યાદી (Members Directory)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    selected = currentTab == 1,
                    onClick = {
                        scope.launch { drawerState.close() }
                        currentTab = 1
                    },
                    icon = { Icon(Icons.Default.People, contentDescription = "Members Directory", tint = SaffronDeepOrange) },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = SaffronBorderColor,
                        selectedTextColor = SaffronSecondaryText,
                        selectedIconColor = SaffronDeepOrange,
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color.Black,
                        unselectedIconColor = SaffronDeepOrange
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).testTag("drawer_members_item")
                )

                // 3. Log out item
                NavigationDrawerItem(
                    label = { Text("લોગ આઉટ (Log out)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.logout()
                    },
                    icon = { Icon(Icons.Default.Logout, contentDescription = "Log out", tint = Color(0xFFC62828)) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        unselectedTextColor = Color(0xFFC62828),
                        unselectedIconColor = Color(0xFFC62828)
                    ),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp).testTag("drawer_logout_item")
                )

                Spacer(modifier = Modifier.weight(1f))

                // Footer with nice watermark info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "તમે ધન્ય છો કે શ્રી રામ સેવા કરી રહ્યા છો 🙏",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "MANOJ JIKADRA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "v1.2.0 | High Density UI",
                        fontSize = 8.5.sp,
                        color = Color.LightGray
                    )
                }
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                MandalTopAppBar(currentTab = currentTab, viewModel = viewModel, onMenuClick = {
                    scope.launch { drawerState.open() }
                })
            },
            bottomBar = {
                MandalBottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it },
                    viewModel = viewModel
                )
            },
            floatingActionButton = {
                if (viewModel.isAdmin) {
                    MandalFAB(
                        currentTab = currentTab,
                        ledgerTab = ledgerTab,
                        onAddMember = { showAddMemberDialog = true },
                        onAddExpense = { showAddExpenseDialog = true },
                        onAddPayment = { showAddPaymentDialog = true }
                    )
                }
            },
            containerColor = SaffronBackground
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Screen switching with slide animations
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = spring()) togetherWith fadeOut(animationSpec = spring())
                    },
                    label = "ScreenTransition"
                ) { targetTab ->
                    when (targetTab) {
                        0 -> DashboardScreen(
                            members = members,
                            payments = payments,
                            expenses = expenses,
                            viewModel = viewModel,
                            onMemberClick = { selectedMemberForDetail = it },
                            onTabSelected = { currentTab = it },
                            onAddPaymentClick = { 
                                if (viewModel.isAdmin) {
                                    showAddPaymentDialog = true
                                } else {
                                    Toast.makeText(context, "પરવાનગી નથી! ફક્ત એડમિન આ ફેરફાર કરી શકે છે.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onAddExpenseClick = { 
                                if (viewModel.isAdmin) {
                                    showAddExpenseDialog = true
                                } else {
                                    Toast.makeText(context, "પરવાનગી નથી! ફક્ત એડમિન આ ફેરફાર કરી શકે છે.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                        1 -> MembersScreen(
                            members = members,
                            payments = payments,
                            viewModel = viewModel,
                            onMemberClick = { selectedMemberForDetail = it }
                        )
                        2 -> CashBookScreen(
                            payments = payments,
                            expenses = expenses,
                            members = members,
                            viewModel = viewModel,
                            ledgerTab = ledgerTab,
                            onLedgerTabSelected = { ledgerTab = it }
                        )
                        3 -> ReportsScreen(
                            members = members,
                            payments = payments,
                            expenses = expenses,
                            viewModel = viewModel
                        )
                        4 -> NotificationsScreen(
                            viewModel = viewModel
                        )
                    }
                }

                // --- ALL ACTION DIALOGS ---
                if (showAddMemberDialog) {
                    AddMemberDialog(
                        viewModel = viewModel,
                        onDismiss = { showAddMemberDialog = false },
                        onSuccess = {
                            showAddMemberDialog = false
                            Toast.makeText(context, "Mandal Member Added Successfully!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                if (showAddExpenseDialog) {
                    AddExpenseDialog(
                        viewModel = viewModel,
                        onDismiss = { showAddExpenseDialog = false },
                        onSuccess = {
                            showAddExpenseDialog = false
                            Toast.makeText(context, "Mandal Expense (Javak) Recorded!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                if (showAddPaymentDialog) {
                    AddPaymentDialog(
                        viewModel = viewModel,
                        members = members,
                        onDismiss = { showAddPaymentDialog = false },
                        onSuccess = {
                            showAddPaymentDialog = false
                            Toast.makeText(context, "Payment (Aavak) Received & Settled!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                selectedMemberForDetail?.let { member ->
                    MemberDetailDialog(
                        member = member,
                        payments = payments.filter { it.memberId == member.id },
                        viewModel = viewModel,
                        onDismiss = { selectedMemberForDetail = null }
                    )
                }

                if (showProfileDialog) {
                    UserProfileDialog(
                        viewModel = viewModel,
                        onDismiss = { showProfileDialog = false }
                    )
                }
            }
        }
    }
}

// Detailed User Profile dialog showing current login status and permissions
@Composable
fun UserProfileDialog(viewModel: MandalViewModel, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = DenseSurface),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.5.dp, Color(0xFFFFB74D).copy(alpha = 0.8f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("user_profile_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header logo & title
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFFFF3E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🕉️", fontSize = 32.sp)
                }

                Text(
                    text = "સભ્ય પ્રોફાઇલ (Profile Details)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE65100),
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(color = Color(0xFFFFE0B2), thickness = 1.dp)

                // Info Rows
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileItemRow(
                        label = "લોગિન આઈડી (Login ID):",
                        value = if (viewModel.isAdmin) "admin" else "123"
                    )
                    ProfileItemRow(
                        label = "મંડળ રોલ (Mandal Role):",
                        value = if (viewModel.isAdmin) "એડમિનિસ્ટ્રેટર (Admin)" else "સામાન્ય સભ્ય (User Only)"
                    )
                    ProfileItemRow(
                        label = "એકાઉન્ટ સ્ટેટસ (Status):",
                        value = "સક્રિય (Active) ✅"
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                
                // Privileges list
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "પરમિશન લિસ્ટ (Privileges):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFFE65100)
                        )
                        val privileges = if (viewModel.isAdmin) {
                            listOf(
                                "• સભ્યો ઉમેરવા (Add Members)",
                                "• જાવક ખર્ચ દાખલ કરવો (Record Expenses)",
                                "• સભ્યોની આવક સેટલ કરવી (Settle Payments)",
                                "• અગત્યના સંદેશાઓ મોકલવા (Broadcast Announcements)",
                                "• પીડીએફ રીપોર્ટ સેવ કરવો (Generate PDF Reports)"
                            )
                        } else {
                            listOf(
                                "• સભ્યોની લિસ્ટ જોવી (View Members list)",
                                "• જાવક અને આવક હિસાબ જોવો (View Hisab Cash Book)",
                                "• મંડળના અગત્યના સંદેશાઓ વાંચવા (Read Announcements)"
                            )
                        }
                        privileges.forEach { priv ->
                            Text(
                                text = priv,
                                fontSize = 11.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Text("બંધ કરો (Close)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileItemRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

// Custom Styled App Bar in High Density Theme
@Composable
fun MandalTopAppBar(currentTab: Int, viewModel: MandalViewModel, onMenuClick: () -> Unit) {
    val subtitleText = when (currentTab) {
        0 -> "Mandal Management System"
        1 -> "Members Directory"
        2 -> "Cash Book (Hisab)"
        3 -> "Monthly Accounts PDF Report"
        4 -> "Mandal Notifications (સંદેશા)"
        else -> "Mandal Management System"
    }

    Surface(
        color = DenseBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Navigation Icon to trigger sidebar menu
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("on_menu_click_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation Drawer",
                        tint = Color.Black
                    )
                }

                // Circular Coin avatar matching the HTML mockup: bg-[#DEE1FF] text-xl 🕉️
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DenseCardFundBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🕉️",
                        fontSize = 18.sp
                    )
                }

                Column {
                    Text(
                        text = "Shree Ram Yuvak Mandal",
                        color = DenseTitleText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 17.sp
                    )
                    Text(
                        text = subtitleText,
                        color = DenseLabelText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// Custom Bottom Navigation Bar complying with M3 specs
@Composable
fun MandalBottomNavBar(currentTab: Int, onTabSelected: (Int) -> Unit, viewModel: MandalViewModel) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val lastReadTime = viewModel.lastReadNotificationTime
    val unreadCount = remember(notifications, lastReadTime) {
        notifications.count { it.date > lastReadTime }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F0F4))
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = Color(0xFFE1E2EC), thickness = 1.dp)
        NavigationBar(
            tonalElevation = 0.dp,
            containerColor = Color(0xFFF1F0F4)
        ) {
            NavigationBarItem(
                selected = currentTab == 0,
                onClick = { onTabSelected(0) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == 0) Icons.Default.Dashboard else Icons.Outlined.Dashboard,
                        contentDescription = "Dashboard"
                    )
                },
                label = { Text("Dashboard", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001D36),
                    selectedTextColor = Color(0xFF001D36),
                    indicatorColor = Color(0xFFD0E4FF),
                    unselectedIconColor = Color(0xFF44474E),
                    unselectedTextColor = Color(0xFF44474E)
                ),
                modifier = Modifier.testTag("nav_tab_dashboard")
            )
            NavigationBarItem(
                selected = currentTab == 1,
                onClick = { onTabSelected(1) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == 1) Icons.Default.People else Icons.Outlined.People,
                        contentDescription = "Members"
                    )
                },
                label = { Text("Members", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001D36),
                    selectedTextColor = Color(0xFF001D36),
                    indicatorColor = Color(0xFFD0E4FF),
                    unselectedIconColor = Color(0xFF44474E),
                    unselectedTextColor = Color(0xFF44474E)
                ),
                modifier = Modifier.testTag("nav_tab_members")
            )
            NavigationBarItem(
                selected = currentTab == 2,
                onClick = { onTabSelected(2) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == 2) Icons.Default.ReceiptLong else Icons.Outlined.ReceiptLong,
                        contentDescription = "Hisab Ledger"
                    )
                },
                label = { Text("Hisab", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001D36),
                    selectedTextColor = Color(0xFF001D36),
                    indicatorColor = Color(0xFFD0E4FF),
                    unselectedIconColor = Color(0xFF44474E),
                    unselectedTextColor = Color(0xFF44474E)
                ),
                modifier = Modifier.testTag("nav_tab_hisab")
            )
            NavigationBarItem(
                selected = currentTab == 3,
                onClick = { onTabSelected(3) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == 3) Icons.Default.Assessment else Icons.Outlined.Assessment,
                        contentDescription = "Reports"
                    )
                },
                label = { Text("Reports", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001D36),
                    selectedTextColor = Color(0xFF001D36),
                    indicatorColor = Color(0xFFD0E4FF),
                    unselectedIconColor = Color(0xFF44474E),
                    unselectedTextColor = Color(0xFF44474E)
                ),
                modifier = Modifier.testTag("nav_tab_reports")
            )
            NavigationBarItem(
                selected = currentTab == 4,
                onClick = { onTabSelected(4) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (unreadCount > 0) {
                                Badge(
                                    containerColor = Color(0xFFC62828),
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = unreadCount.toString(),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentTab == 4) Icons.Default.Notifications else Icons.Outlined.Notifications,
                            contentDescription = "Notifications"
                        )
                    }
                },
                label = { Text("સંદેશા", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF001D36),
                    selectedTextColor = Color(0xFF001D36),
                    indicatorColor = Color(0xFFD0E4FF),
                    unselectedIconColor = Color(0xFF44474E),
                    unselectedTextColor = Color(0xFF44474E)
                ),
                modifier = Modifier.testTag("nav_tab_notifications")
            )
        }
    }
}

// Custom Dynamic Floating Action Button
@Composable
fun MandalFAB(
    currentTab: Int,
    ledgerTab: Int,
    onAddMember: () -> Unit,
    onAddExpense: () -> Unit,
    onAddPayment: () -> Unit
) {
    when (currentTab) {
        0 -> {
            // Dashboard: No Floating Action Button (moved to Hisab screen)
        }
        1 -> {
            // Members list: Create Member
            FloatingActionButton(
                onClick = onAddMember,
                containerColor = SaffronPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_member")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Member")
            }
        }
        2 -> {
            // Cash Book: Create Expense (Javak) or Payment (Aavak) depending on ledgerTab
            if (ledgerTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = onAddPayment,
                    icon = { Icon(Icons.Default.Payments, contentDescription = "Add Payment") },
                    text = { Text("Sabhya ni Aavak") },
                    containerColor = SaffronSecondary,
                    contentColor = Color.Black,
                    modifier = Modifier.testTag("fab_quick_payment")
                )
            } else {
                ExtendedFloatingActionButton(
                    onClick = onAddExpense,
                    icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Record Expense (Javak)") },
                    text = { Text("જાવક નોંધો (Add Expense)") },
                    containerColor = SaffronAccent,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_add_expense")
                )
            }
        }
    }
}

// ==================== SCREEN 1: DASHBOARD ====================
@Composable
fun DashboardScreen(
    members: List<Member>,
    payments: List<MemberPayment>,
    expenses: List<Expense>,
    viewModel: MandalViewModel,
    onMemberClick: (Member) -> Unit,
    onTabSelected: (Int) -> Unit = {},
    onAddPaymentClick: () -> Unit = {},
    onAddExpenseClick: () -> Unit = {}
) {
    val context = LocalContext.current
    // Current Local time is June 2026. Let's showcase June stats
    val cal = Calendar.getInstance()
    val curYear = cal.get(Calendar.YEAR)
    val curMonth = cal.get(Calendar.MONTH) + 1

    // Overall stats calculations
    val totalAavakAllTime = payments.sumOf { it.amount }
    val totalJavakAllTime = expenses.sumOf { it.amount }
    val availableBalance = totalAavakAllTime - totalJavakAllTime

    // Filter payments falling in current physical month
    val isTimestampInMonthYear: (Long, Int, Int) -> Boolean = { ts, m, y ->
        val itemCal = Calendar.getInstance()
        itemCal.timeInMillis = ts
        itemCal.get(Calendar.YEAR) == y && (itemCal.get(Calendar.MONTH) + 1) == m
    }

    val currentMonthPaymentsSum = payments.filter { isTimestampInMonthYear(it.paidDate, curMonth, curYear) }.sumOf { it.amount }
    val currentMonthExpensesSum = expenses.filter { isTimestampInMonthYear(it.date, curMonth, curYear) }.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Welcome Header & Status info row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "જય શ્રી રામ 🕉️",
                    color = Color(0xFFE65100), // Saffron accent color
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "કુલ સભ્યો: ${members.size} | ૧૦૦/- પ્રતિ મહિનો દર",
                    color = DenseLabelText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Available Fund (Balance) & Income Expense consolidated premium card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DenseCardFundBg),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "Available Fund (Balance)",
                                color = DenseLabelText,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = "₹ $availableBalance",
                                color = DenseFundText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(100.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            val activePeriod = "${viewModel.getMonthNameShort(curMonth).uppercase()} $curYear"
                            Text(
                                text = activePeriod,
                                color = DenseFundText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Sub-grid row for Monthly Income and Expense
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Monthly Income Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "MONTHLY INCOME",
                                    color = DenseLabelText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(1.dp))
                                Text(
                                    text = "+ ₹ $currentMonthPaymentsSum",
                                    color = DenseIncomeText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Monthly Expense Card
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "MONTHLY EXPENSE",
                                    color = DenseLabelText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(1.dp))
                                Text(
                                    text = "- ₹ $currentMonthExpensesSum",
                                    color = DenseExpenseText,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick action icons row matching HTML mockup (Only shown if Admin)
        if (viewModel.isAdmin) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Button 1: Income
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onAddPaymentClick() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(DenseBorder, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📥", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Income", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = DenseLabelText)
                    }

                    // Button 2: Expense
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onAddExpenseClick() }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(DenseBorder, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📤", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Expense", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = DenseLabelText)
                    }

                    // Button 3: Members
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTabSelected(1) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(DenseBorder, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👥", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Members", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = DenseLabelText)
                    }

                    // Button 4: PDF Report
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTabSelected(3) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(DenseActiveContainer, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📄", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("PDF Report", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DenseActiveOnContainer)
                    }
                }
            }
        }

        // Automatic Sliding Business Advertisement Banner row
        item {
            val banners by viewModel.bannersState.collectAsStateWithLifecycle()
            if (banners.isNotEmpty()) {
                var currentIndex by remember { mutableStateOf(0) }
                var isPaused by remember { mutableStateOf(false) }

                LaunchedEffect(banners, isPaused) {
                    if (!isPaused && banners.size > 1) {
                        while (true) {
                            delay(3000)
                            currentIndex = (currentIndex + 1) % banners.size
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, DenseBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .pointerInput(banners, isPaused) {
                            detectTapGestures(
                                onPress = {
                                    isPaused = true
                                    tryAwaitRelease()
                                    isPaused = false
                                }
                            )
                        }
                ) {
                    val banner = banners.getOrNull(currentIndex)
                    if (banner != null) {
                        val bgColor = try {
                            Color(android.graphics.Color.parseColor(banner.bgColorHex))
                        } catch (e: Exception) {
                            Color(0xFFFFF3E0)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgColor)
                                .padding(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFE65100), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "જાહેરાત (ADVERTISEMENT)",
                                                color = Color.White,
                                                fontSize = 8.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            banners.forEachIndexed { idx, _ ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(if (idx == currentIndex) 6.dp else 4.dp)
                                                        .clip(CircleShape)
                                                        .background(if (idx == currentIndex) Color(0xFFE65100) else Color.Gray.copy(alpha = 0.4f))
                                                )
                                            }
                                        }
                                    }

                                    if (isPaused) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.PauseCircle,
                                                contentDescription = "Paused",
                                                tint = Color(0xFFE65100),
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Text("PAUSED", color = Color(0xFFE65100), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    if (banner.imageUrl.isNotEmpty()) {
                                        AsyncImage(
                                            model = banner.imageUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White)
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(54.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color.White.copy(alpha = 0.4f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("📢", fontSize = 24.sp)
                                        }
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = banner.title,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 12.sp,
                                            color = Color.Black
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = banner.description,
                                            fontSize = 11.sp,
                                            color = Color.DarkGray,
                                            lineHeight = 13.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = "Call",
                                            tint = Color(0xFF1B5E20),
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Text(
                                            text = "સંપર્ક: ${banner.phone}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1B5E20)
                                        )
                                    }

                                    Text(
                                        text = "વાંચવા માટે દબાવી રાખો 👆",
                                        fontSize = 8.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (viewModel.isAdmin) {
            item {
                var bulkText by remember { mutableStateOf("") }
                Card(
                    colors = CardDefaults.cardColors(containerColor = SaffronLightOrange),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, SaffronDeepOrange.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("gemini_ai_bulk_card")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(SaffronDeepOrange, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✨", fontSize = 16.sp, color = Color.White)
                            }
                            Column {
                                Text(
                                    text = "રામસેવક AI બલ્ક ઈમ્પોર્ટ (Gemini)",
                                    color = SaffronTitleText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "સભ્યો, માસિક ફાળો કે ખર્ચ એકસાથે અપડેટ કરો",
                                    color = SaffronDarkSlate.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        OutlinedTextField(
                            value = bulkText,
                            onValueChange = { bulkText = it },
                            placeholder = {
                                Text(
                                    text = "અહીં બલ્ક લખાણ દાખલ કરો. જેમ કે:\n૧. વિજય દેસાઈ 9876543210 ગામ મહેસાણા\n૨. કલ્પેશભાઈ ₹૫૦૦ જમા\n૩. પ્રસાદ માટે ₹૧૫૦૦ ખર્ચ",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .testTag("gemini_bulk_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = SaffronDeepOrange,
                                unfocusedIndicatorColor = Color.LightGray,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = Color.Black)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Show status banners
                        viewModel.geminiErrorMessage?.let { err ->
                            Text(
                                text = "⚠️ $err",
                                color = Color(0xFFC62828),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        
                        viewModel.geminiSuccessMessage?.let { success ->
                            Text(
                                text = success,
                                color = Color(0xFF2E7D32),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    bulkText = "રમેશભાઈ વઘાસિયા 9898123456 ગામ નવસારી\n" +
                                              "હિતેશભાઈ ધોળકિયા 8765432109 ગામ સુરત\n" +
                                              "રમેશભાઈ વઘાસિયા ₹૪૦૦ આપ્યા\n" +
                                              "મંડળ બેનર અને ધ્વજ માટે ₹1250 ખર્ચો થયો"
                                },
                                modifier = Modifier.testTag("gemini_fill_sample_btn")
                            ) {
                                Text("નમૂનો દાખલ કરો 📋", fontSize = 11.sp, color = SaffronDeepOrange, fontWeight = FontWeight.Bold)
                            }
                            
                            if (viewModel.isGeminiProcessing) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = SaffronDeepOrange
                                    )
                                    Text("વિશ્લેષણ ચાલુ છે...", fontSize = 11.sp, color = SaffronDeepOrange, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = {
                                        viewModel.processBulkImportWithGemini(bulkText) {
                                            bulkText = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronDeepOrange),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .height(36.dp)
                                        .testTag("gemini_process_btn")
                                ) {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("AI વડે અપડેટ કરો", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Admin Business Advertisement Management
        if (viewModel.isAdmin) {
            item {
                var showManageAdsDialog by remember { mutableStateOf(false) }
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFECEFF1)),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.LightGray),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { showManageAdsDialog = true }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF37474F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("📢", fontSize = 18.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "બિઝનેસ જાહેરાત બેનર્સ (Business Ads)",
                                color = Color(0xFF263238),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "અહીંથી ૧૦ નવી સ્લાઈડિંગ જાહેરાતો ઉમેરો કે દૂર કરો",
                                color = Color.Gray,
                                fontSize = 11.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }

                if (showManageAdsDialog) {
                    Dialog(onDismissRequest = { showManageAdsDialog = false }) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(0.9f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "જાહેરાતોની યાદી (Business Ads List)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    )
                                    IconButton(onClick = { showManageAdsDialog = false }) {
                                        Icon(Icons.Default.Close, contentDescription = "Close")
                                    }
                                }

                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                                // Form variables
                                var addTitle by remember { mutableStateOf("") }
                                var addDesc by remember { mutableStateOf("") }
                                var addPhone by remember { mutableStateOf("") }
                                var addImgUrl by remember { mutableStateOf("") }
                                var addBgColor by remember { mutableStateOf("#FFF3E0") }

                                val banners by viewModel.bannersState.collectAsStateWithLifecycle()

                                LazyColumn(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF5F5F5), RoundedCornerShape(10.dp))
                                                .padding(10.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = "નવી જાહેરાત ઉમેરો (Add New Ad)",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = SaffronDeepOrange
                                            )

                                            OutlinedTextField(
                                                value = addTitle,
                                                onValueChange = { addTitle = it },
                                                label = { Text("વ્યવસાયનું નામ (Business Title)", fontSize = 11.sp) },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    focusedBorderColor = SaffronDeepOrange,
                                                    focusedLabelColor = SaffronDeepOrange
                                                )
                                            )

                                            OutlinedTextField(
                                                value = addDesc,
                                                onValueChange = { addDesc = it },
                                                label = { Text("જાહેરાત વિગત (Ad text description)", fontSize = 11.sp) },
                                                maxLines = 2,
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    focusedBorderColor = SaffronDeepOrange,
                                                    focusedLabelColor = SaffronDeepOrange
                                                )
                                            )

                                            OutlinedTextField(
                                                value = addPhone,
                                                onValueChange = { addPhone = it },
                                                label = { Text("મોબાઈલ નંબર (Contact Phone)", fontSize = 11.sp) },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    focusedBorderColor = SaffronDeepOrange,
                                                    focusedLabelColor = SaffronDeepOrange
                                                )
                                            )

                                            OutlinedTextField(
                                                value = addImgUrl,
                                                onValueChange = { addImgUrl = it },
                                                label = { Text("ફોટો લિંક URL (Optional Image URL)", fontSize = 11.sp) },
                                                singleLine = true,
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = OutlinedTextFieldDefaults.colors(
                                                    focusedTextColor = Color.Black,
                                                    unfocusedTextColor = Color.Black,
                                                    focusedBorderColor = SaffronDeepOrange,
                                                    focusedLabelColor = SaffronDeepOrange
                                                )
                                            )

                                            Text("બેકગ્રાઉન્ડ રંગ પસંદ કરો (Background Color):", fontSize = 11.sp, color = Color.Gray)
                                            val colorPresets = listOf(
                                                "#FFF3E0" to "નારંગી (Orange)",
                                                "#E8F5E9" to "લીલો (Green)",
                                                "#E3F2FD" to "વાદળી (Blue)",
                                                "#F3E5F5" to "જાંબલી (Purple)",
                                                "#FFFDE7" to "પીળો (Yellow)"
                                            )

                                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                colorPresets.forEach { (hex, name) ->
                                                    Box(
                                                        modifier = Modifier
                                                            .size(28.dp)
                                                            .clip(CircleShape)
                                                            .background(Color(android.graphics.Color.parseColor(hex)))
                                                            .border(
                                                                width = if (addBgColor == hex) 2.dp else 1.dp,
                                                                color = if (addBgColor == hex) Color.Black else Color.LightGray,
                                                                shape = CircleShape
                                                            )
                                                            .clickable { addBgColor = hex }
                                                    )
                                                }
                                            }

                                            Button(
                                                onClick = {
                                                    if (addTitle.trim().isNotEmpty() && addDesc.trim().isNotEmpty() && addPhone.trim().isNotEmpty()) {
                                                        viewModel.addBanner(addTitle, addDesc, addPhone, addImgUrl, addBgColor)
                                                        addTitle = ""
                                                        addDesc = ""
                                                        addPhone = ""
                                                        addImgUrl = ""
                                                        Toast.makeText(context, "જાહેરાત સફળતાપૂર્વક ઉમેરાઈ ગઈ!", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        Toast.makeText(context, "મહેરબાની કરીને પૂરતી વિગતો ભરો.", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = SaffronDeepOrange),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("નવી બેનર જાહેરાત સાચવો", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                            }
                                        }
                                    }

                                    item {
                                        Text(
                                            text = "હાલની સક્રિય જાહેરાતો (Active Banners count: ${banners.size}/10)",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color.Black,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }

                                    items(banners) { banner ->
                                        val cardColor = try {
                                            Color(android.graphics.Color.parseColor(banner.bgColorHex))
                                        } catch (e: Exception) {
                                            Color(0xFFFFF3E0)
                                        }
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = cardColor),
                                            border = BorderStroke(1.dp, Color.LightGray),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(10.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(banner.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Black)
                                                    Text(banner.description, fontSize = 10.sp, color = Color.DarkGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                                    Text("ફોન: ${banner.phone}", fontSize = 10.sp, color = Color.Black)
                                                }
                                                IconButton(onClick = {
                                                    viewModel.deleteBanner(banner.id)
                                                    Toast.makeText(context, "જાહેરાત દૂર કરી દીધી છે.", Toast.LENGTH_SHORT).show()
                                                }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section header with functional View All label on right
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pending Collections (EMI)",
                    color = DenseTitleText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "View All",
                    color = DensePrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onTabSelected(1) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Inner items inside a consolidated and beautiful grey container card
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DenseContainer, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (members.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "કોઈ સભ્ય ઉપલબ્ધ નથી. કૃપા કરીને સભ્ય ઉમેરો.",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    members.take(3).forEach { member ->
                        val memberPayments = payments.filter { it.memberId == member.id }
                        val pending = viewModel.calculatePendingMonths(member, memberPayments, curYear, curMonth)
                        val advance = memberPayments.filter { it.forYear > curYear || (it.forYear == curYear && it.forMonth > curMonth) }.sumOf { it.amount }
                        val isPending = pending.isNotEmpty()

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, DenseBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMemberClick(member) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Rounded initial circle
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (isPending) Color(0xFFFFEBEE) else DenseBorder),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = member.name.take(2).uppercase(Locale.getDefault()),
                                        color = if (isPending) Color(0xFFC62828) else DenseLabelText,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = member.name,
                                        fontWeight = FontWeight.Bold,
                                        color = DenseTitleText,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (isPending) "Pending: ${pending.size} Months (₹${pending.size * 100})" else if (advance > 0) "Advance: ₹$advance" else "Active (Up-to-date)",
                                        color = if (isPending) Color(0xFFBE123C) else if (advance > 0) Color(0xFF005AC1) else DenseLabelText,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Right trigger remind / status pill
                                if (isPending) {
                                    if (viewModel.isAdmin) {
                                        val context = LocalContext.current
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    val duesAmt = pending.size * 100
                                                    val pendingNames = pending.joinToString(", ") { "${viewModel.getMonthNameShort(it.second)} ${it.first}" }
                                                    val messageVal = "નમસ્તે ${member.name}, નમ્ર વિનંતી કે આપનું મંડળનું યોગદાન (₹$duesAmt/- $pendingNames ની બાકી EMI) ચૂકવવાનું બાકી છે. કૃપા કરીને વહેલી તકે જમા કરાવા નમ્ર વિનંતી. આભાર!"
                                                    try {
                                                         val uri = Uri.parse("smsto:${member.phone}")
                                                         val smsIntent = Intent(Intent.ACTION_SENDTO, uri).apply {
                                                             putExtra("sms_body", messageVal)
                                                         }
                                                         context.startActivity(smsIntent)
                                                    } catch (e: Exception) {
                                                         Toast.makeText(context, "મેસેજ એપ ખોલી શકાઈ નથી.", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(Color(0xFFE3F2FD), CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Sms,
                                                    contentDescription = "Send Message",
                                                    tint = Color(0xFF0288D1),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }

                                            Button(
                                                onClick = { onMemberClick(member) },
                                                colors = ButtonDefaults.buttonColors(containerColor = DensePrimary),
                                                shape = RoundedCornerShape(100.dp),
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                                modifier = Modifier.height(28.dp)
                                            ) {
                                                Text(
                                                    text = "REMIND",
                                                    color = Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFFFFEBEE), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = "PENDING",
                                                color = Color(0xFFC62828),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .background(Color(0xFFE8F5E9), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = "PAID",
                                            color = Color(0xFF2E7D32),
                                            fontSize = 9.sp,
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
    }
}

// ==================== SCREEN 2: MEMBERS DIRECTORY ====================
@Composable
fun MembersScreen(
    members: List<Member>,
    payments: List<MemberPayment>,
    viewModel: MandalViewModel,
    onMemberClick: (Member) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredMembers = members.filter { it.name.contains(searchQuery, ignoreCase = true) }
    
    val cal = Calendar.getInstance()
    val curYear = cal.get(Calendar.YEAR)
    val curMonth = cal.get(Calendar.MONTH) + 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search text field styled matching high-density inputs
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it.uppercase() },
            label = { Text("સભ્ય શોધો (Search Member)", fontWeight = FontWeight.SemiBold) },
            placeholder = { Text("દા.ત. Ramesh Patel") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DenseLabelText) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .testTag("member_search_input"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedBorderColor = DensePrimary,
                focusedLabelColor = DensePrimary,
                unfocusedBorderColor = DenseBorder,
                unfocusedLabelColor = DenseLabelText,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (filteredMembers.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("કોઈ સભ્યો જોવા મળ્યા નથી.", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            } else {
                items(filteredMembers) { sMember ->
                    val mPayments = payments.filter { it.memberId == sMember.id }
                    val pMonths = viewModel.calculatePendingMonths(sMember, mPayments, curYear, curMonth)
                    val advSum = mPayments.filter { it.forYear > curYear || (it.forYear == curYear && it.forMonth > curMonth) }.sumOf { it.amount }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, DenseBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMemberClick(sMember) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = sMember.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = DenseTitleText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "સંપર્ક: ${sMember.phone}",
                                    color = DenseLabelText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Joined: ${viewModel.getMonthNameShort(sMember.joinedMonth)} ${sMember.joinedYear}",
                                    color = Color.Gray,
                                    fontSize = 10.sp
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                if (pMonths.isNotEmpty()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "${pMonths.size} Months Pending",
                                                color = DenseExpenseText,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp
                                            )
                                            Text(
                                                text = "Dues: ₹${pMonths.size * 100}",
                                                color = Color.Gray,
                                                fontSize = 10.sp
                                            )
                                        }

                                        if (viewModel.isAdmin) {
                                            val context = LocalContext.current
                                            IconButton(
                                                onClick = {
                                                    val duesAmt = pMonths.size * 100
                                                    val pendingNames = pMonths.joinToString(", ") { "${viewModel.getMonthNameShort(it.second)} ${it.first}" }
                                                    val messageVal = "નમસ્તે ${sMember.name}, નમ્ર વિનંતી કે આપનું મંડળનું યોગદાન (₹$duesAmt/- $pendingNames ની બાકી EMI) ચૂકવવાનું બાકી છે. કૃપા કરીને વહેલી તકે જમા કરાવા નમ્ર વિનંતી. આભાર!"
                                                    try {
                                                        val uri = Uri.parse("smsto:${sMember.phone}")
                                                        val smsIntent = Intent(Intent.ACTION_SENDTO, uri).apply {
                                                            putExtra("sms_body", messageVal)
                                                        }
                                                        context.startActivity(smsIntent)
                                                    } catch (e: Exception) {
                                                        Toast.makeText(context, "મેસેજ એપ ખોલી શકાઈ નથી.", Toast.LENGTH_SHORT).show()
                                                    }
                                                },
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .background(Color(0xFFE3F2FD), CircleShape)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Sms,
                                                    contentDescription = "Send Message",
                                                    tint = Color(0xFF0288D1),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "Up-to-date ✔",
                                        color = Color(0xFF2E7D32),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }

                                if (advSum > 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Advance: ₹$advSum",
                                        color = Color(0xFF005AC1),
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 10.sp
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
         // ==================== SCREEN 3: CASH BOOK (AAVAK/JAVAK) ====================
@Composable
fun CashBookScreen(
    payments: List<MemberPayment>,
    expenses: List<Expense>,
    members: List<Member>,
    viewModel: MandalViewModel,
    ledgerTab: Int,
    onLedgerTabSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Tab selector styled cleanly with M3 modern colors
        TabRow(
            selectedTabIndex = ledgerTab,
            containerColor = Color(0xFFF1F0F4),
            contentColor = DensePrimary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(bottom = 12.dp)
        ) {
            Tab(
                selected = ledgerTab == 0,
                onClick = { onLedgerTabSelected(0) },
                text = { Text("આવક (Income)", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                unselectedContentColor = DenseLabelText,
                selectedContentColor = DensePrimary,
                modifier = Modifier.testTag("tab_hisab_income")
            )
            Tab(
                selected = ledgerTab == 1,
                onClick = { onLedgerTabSelected(1) },
                text = { Text("જાવક / દાન (Expense)", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                unselectedContentColor = DenseLabelText,
                selectedContentColor = DensePrimary,
                modifier = Modifier.testTag("tab_hisab_expense")
            )
        }

        if (ledgerTab == 0) {
            // Aavak section
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (payments.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("કોઈ આવક નોંધ ઉપલબ્ધ નથી.", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(payments) { p ->
                        val member = members.find { it.id == p.memberId }
                        val nameStr = member?.name ?: "Unknown Member"
                        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        val dStr = dateFormat.format(Date(p.paidDate))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, DenseBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Payment, contentDescription = null, tint = DenseIncomeText, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(nameStr, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DenseTitleText)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text("For: ${viewModel.getMonthNameShort(p.forMonth)} - ${p.forYear}", color = DenseLabelText, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text("Date: $dStr", color = Color.Gray, fontSize = 9.sp)
                                }
                                Text("₹${p.amount}", color = DenseIncomeText, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                if (viewModel.isAdmin) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(onClick = { viewModel.deletePayment(p) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Javak section
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (expenses.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("કોઈ જાવક (ખર્ચ / દાન) નોંધાયેલ નથી.", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                } else {
                    items(expenses) { e ->
                        val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                        val dStr = dateFormat.format(Date(e.date))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, DenseBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFFEBEE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = DenseExpenseText, modifier = Modifier.size(18.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(e.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DenseTitleText)
                                    val displayDesc = if (e.description.trim().isNotEmpty()) e.description.trim() else "(કોઈ વિગત નથી / No description)"
                                    Text(displayDesc, color = if (e.description.trim().isNotEmpty()) DenseLabelText else Color.Gray, fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(color = Color(0xFFFFEBEE), shape = RoundedCornerShape(4.dp)) {
                                            Text(e.category, color = DenseExpenseText, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Date: $dStr", color = Color.Gray, fontSize = 9.sp)
                                    }
                                }
                                Text("₹${e.amount}", color = DenseExpenseText, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                if (viewModel.isAdmin) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    IconButton(onClick = { viewModel.deleteExpense(e) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Gray, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== SCREEN 5: NOTIFICATIONS (સંદેશા) ====================
@Composable
fun NotificationsScreen(viewModel: MandalViewModel) {
    val context = LocalContext.current
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    
    LaunchedEffect(notifications) {
        viewModel.markNotificationsAsRead()
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .testTag("notifications_list_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Welcoming card or notice info
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DenseCardFundBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E2FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "Announcements",
                            tint = Color(0xFF005AC1)
                        )
                    }
                    Column {
                        Text(
                            text = "મંડળ નોટિસ બોર્ડ (Mandal Notice Board)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color(0xFF00174B)
                        )
                        Text(
                            text = "એડમિન દ્વારા મુકેલા તમામ અગત્યના સંદેશાઓ અહીં વાંચી શકાશે.",
                            fontSize = 10.sp,
                            color = Color(0xFF1B1B1F)
                        )
                    }
                }
            }
        }

        // Admin Notification publisher (Only visible if isAdmin == true)
        if (viewModel.isAdmin) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DenseSurface),
                    border = BorderStroke(1.dp, Color(0xFFFFB066).copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_notification_panel")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = null,
                                tint = Color(0xFFE65100),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "નવો સંદેશ મોકલો (Admin Announcement)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFE65100)
                            )
                        }

                        HorizontalDivider(color = Color(0xFFFFE0B2), thickness = 0.8.dp)

                        OutlinedTextField(
                            value = viewModel.newNotificationTitle,
                            onValueChange = { viewModel.newNotificationTitle = it.uppercase() },
                            label = { Text("સંદેશાનું શીર્ષક (Title)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("announcement_title_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color(0xFFFF9800),
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color(0xFFFF9800)
                            )
                        )

                        OutlinedTextField(
                            value = viewModel.newNotificationMessage,
                            onValueChange = { viewModel.newNotificationMessage = it.uppercase() },
                            label = { Text("સંદેશાની વિગત (Message)", fontSize = 11.sp) },
                            minLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("announcement_message_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black,
                                focusedBorderColor = Color(0xFFFF9800),
                                unfocusedBorderColor = Color.LightGray,
                                focusedLabelColor = Color(0xFFFF9800)
                            )
                        )

                        Button(
                            onClick = {
                                if (viewModel.newNotificationTitle.trim().isEmpty()) {
                                    Toast.makeText(context, "કૃપા કરી શીર્ષક લખો!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (viewModel.newNotificationMessage.trim().isEmpty()) {
                                    Toast.makeText(context, "કૃપા કરી વિગત લખો!", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.sendNotification {
                                    Toast.makeText(context, "સંદેશો સફળતાપૂર્વક મોકલવામાં આવ્યો છે!", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                                .testTag("publish_announcement_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "સંદેશ મોકલો (Broadcast Announcement)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // List of actual notices
        if (notifications.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("📭", fontSize = 42.sp)
                        Text(
                            text = "હજુ સુધી કોઈ સંદેશ પ્રસિદ્ધ થયો નથી.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            items(notifications) { notification ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = DenseSurface),
                    border = BorderStroke(1.dp, DenseBorder),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(Color(0xFFFFE0B2), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📢", fontSize = 12.sp)
                                }
                                Text(
                                    text = notification.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFFE65100),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            
                            if (viewModel.isAdmin) {
                                IconButton(
                                    onClick = { viewModel.deleteNotification(notification) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Notice",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = notification.message,
                            fontSize = 11.sp,
                            color = Color(0xFF2C2C2C),
                            lineHeight = 15.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))
                        HorizontalDivider(color = DenseBorder, thickness = 0.5.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "દ્વારા: ${notification.senderName}",
                                fontSize = 9.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            
                            val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                            val formattedDate = sdf.format(Date(notification.date))
                            Text(
                                text = formattedDate,
                                fontSize = 8.5.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==================== SCREEN 4: REPORTS WITH PDF ====================
@Composable
fun ReportsScreen(
    members: List<Member>,
    payments: List<MemberPayment>,
    expenses: List<Expense>,
    viewModel: MandalViewModel
) {
    val context = LocalContext.current

    val monthsList = (1..12).toList()
    val yearsList = listOf(2025, 2026, 2027, 2028)

    // Dropdowns open/close controllers
    var isMonthExpanded by remember { mutableStateOf(false) }
    var isYearExpanded by remember { mutableStateOf(false) }

    // Dynamic checks
    val m = viewModel.selectedReportMonth
    val y = viewModel.selectedReportYear

    // Cash flow fall in this month and year
    val isTimestampInMonthYear: (Long, Int, Int) -> Boolean = { ts, mIndex, yIndex ->
        val itemCal = Calendar.getInstance()
        itemCal.timeInMillis = ts
        itemCal.get(Calendar.YEAR) == yIndex && (itemCal.get(Calendar.MONTH) + 1) == mIndex
    }

    val isRange = viewModel.reportType == 1

    val refCalendar = Calendar.getInstance().apply { timeInMillis = viewModel.reportEndDate }
    val refMonth = if (isRange) refCalendar.get(Calendar.MONTH) + 1 else m
    val refYear = if (isRange) refCalendar.get(Calendar.YEAR) else y

    val currentMonthPayments = if (isRange) {
        payments.filter { it.paidDate in viewModel.reportStartDate..viewModel.reportEndDate }
    } else {
        payments.filter { isTimestampInMonthYear(it.paidDate, m, y) }
    }

    val currentMonthExpenses = if (isRange) {
        expenses.filter { it.date in viewModel.reportStartDate..viewModel.reportEndDate }
    } else {
        expenses.filter { isTimestampInMonthYear(it.date, m, y) }
    }

    val monthIncomeSum = currentMonthPayments.sumOf { it.amount }
    val monthExpenseSum = currentMonthExpenses.sumOf { it.amount }

    // Overall Balance calculation
    val allIncomeTotal = payments.sumOf { it.amount }
    val allExpenseTotal = expenses.sumOf { it.amount }
    val netOverallBalance = allIncomeTotal - allExpenseTotal

    // Advances calculations
    val advancePayersInfo = remember(members, payments, refMonth, refYear, isRange, viewModel.reportStartDate, viewModel.reportEndDate) {
        val list = mutableListOf<Pair<String, Int>>()
        for (member in members) {
            val memberPayments = payments.filter { it.memberId == member.id }
            val sum = memberPayments.filter { 
                it.forYear > refYear || (it.forYear == refYear && it.forMonth > refMonth)
            }.sumOf { it.amount }
            if (sum > 0) {
                list.add(Pair(member.name, sum))
            }
        }
        list
    }

    // Pending EMI calculations
    val pendingMembersInfo = remember(members, payments, refMonth, refYear, isRange, viewModel.reportStartDate, viewModel.reportEndDate) {
        val list = mutableListOf<Pair<String, List<Pair<Int, Int>>>>()
        for (member in members) {
            val memberPayments = payments.filter { it.memberId == member.id }
            val pendings = viewModel.calculatePendingMonths(member, memberPayments, refYear, refMonth)
            if (pendings.isNotEmpty()) {
                list.add(Pair(member.name, pendings))
            }
        }
        list
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Selection Dropdowns
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DenseBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "રિપોર્ટ પ્રકાર પસંદ કરો (Select Report Type)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DenseTitleText
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.reportType = 0 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewModel.reportType == 0) DensePrimary else Color(0xF0F4F8FF),
                                contentColor = if (viewModel.reportType == 0) Color.White else Color.Black
                            ),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text("Monthly (માસિક)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { viewModel.reportType = 1 },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewModel.reportType == 1) DensePrimary else Color(0xF0F4F8FF),
                                contentColor = if (viewModel.reportType == 1) Color.White else Color.Black
                            ),
                            contentPadding = PaddingValues(vertical = 4.dp, horizontal = 4.dp)
                        ) {
                            Text("Custom Range (તારીખ)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (viewModel.reportType == 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Month Dropdown Box
                            Box(modifier = Modifier.weight(1.5f)) {
                                OutlinedButton(
                                    onClick = { isMonthExpanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DenseBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = viewModel.getMonthName(m),
                                            fontSize = 11.sp,
                                            color = DenseTitleText,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = DenseLabelText)
                                    }
                                }
                                DropdownMenu(
                                    expanded = isMonthExpanded,
                                    onDismissRequest = { isMonthExpanded = false }
                                ) {
                                    monthsList.forEach { mIndex ->
                                        DropdownMenuItem(
                                            text = { Text(viewModel.getMonthName(mIndex), fontSize = 11.sp) },
                                            onClick = {
                                                viewModel.selectedReportMonth = mIndex
                                                isMonthExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Year Dropdown Box
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedButton(
                                    onClick = { isYearExpanded = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DenseBorder)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = y.toString(),
                                            fontSize = 11.sp,
                                            color = DenseTitleText,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = DenseLabelText)
                                    }
                                }
                                DropdownMenu(
                                    expanded = isYearExpanded,
                                    onDismissRequest = { isYearExpanded = false }
                                ) {
                                    yearsList.forEach { yr ->
                                        DropdownMenuItem(
                                            text = { Text(yr.toString(), fontSize = 11.sp) },
                                            onClick = {
                                                viewModel.selectedReportYear = yr
                                                isYearExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // From Date
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "શરૂઆતની તારીખ (From)", 
                                    fontSize = 10.sp, 
                                    fontWeight = FontWeight.SemiBold, 
                                    color = DenseLabelText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedButton(
                                    onClick = {
                                        showDatePicker(context, viewModel.reportStartDate) { ts ->
                                            viewModel.reportStartDate = ts
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DenseBorder),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dateFormat.format(Date(viewModel.reportStartDate)),
                                            fontSize = 11.sp,
                                            color = DenseTitleText,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            Icons.Default.DateRange, 
                                            contentDescription = null, 
                                            tint = DensePrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }

                            // To Date
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "અંતિમ તારીખ (To)", 
                                    fontSize = 10.sp, 
                                    fontWeight = FontWeight.SemiBold, 
                                    color = DenseLabelText
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedButton(
                                    onClick = {
                                        showDatePicker(context, viewModel.reportEndDate) { ts ->
                                            // Set to end of the selected day
                                            val c = Calendar.getInstance().apply {
                                                timeInMillis = ts
                                                set(Calendar.HOUR_OF_DAY, 23)
                                                set(Calendar.MINUTE, 59)
                                                set(Calendar.SECOND, 59)
                                                set(Calendar.MILLISECOND, 999)
                                            }
                                            viewModel.reportEndDate = c.timeInMillis
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DenseBorder),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = dateFormat.format(Date(viewModel.reportEndDate)),
                                            fontSize = 11.sp,
                                            color = DenseTitleText,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Icon(
                                            Icons.Default.DateRange, 
                                            contentDescription = null, 
                                            tint = DensePrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Selected Month statistics view
        item {
            val statementTitle = if (isRange) {
                val dateFormat = remember { SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()) }
                "${dateFormat.format(Date(viewModel.reportStartDate))} થી ${dateFormat.format(Date(viewModel.reportEndDate))} Hisab Statement"
            } else {
                "${viewModel.getMonthName(m)} $y Hisab Statement"
            }
            val aavakLabel = if (isRange) "આ સમયગાળાની આવક (Incoming):" else "આ મહિનાની આવક (Incoming):"
            val javakLabel = if (isRange) "આ સમયગાળાનો ખર્ચ (Expenses):" else "આ મહિનાનો ખર્ચ (Expenses):"

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DenseBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = statementTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = DenseTitleText
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("બચેલા ભંડોળ (All Time Net Cash):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DenseLabelText)
                        Text("₹$netOverallBalance", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DensePrimary)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = DenseBorder)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(aavakLabel, fontSize = 11.sp, color = DenseLabelText)
                        Text("+ ₹$monthIncomeSum", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DenseIncomeText)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(javakLabel, fontSize = 11.sp, color = DenseLabelText)
                        Text("- ₹$monthExpenseSum", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DenseExpenseText)
                    }
                }
            }
        }

        // Action Section: Register / Download PDF Section
        item {
            val pdfButtonLabel = if (isRange) "Range Report PDF Download" else "Monthly Report PDF Download"
            Button(
                onClick = { viewModel.downloadReport(context) },
                colors = ButtonDefaults.buttonColors(containerColor = DensePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("download_pdf_report_btn")
            ) {
                Icon(Icons.Filled.PictureAsPdf, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(pdfButtonLabel, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }

        // Section: Sabhyo na Advance details
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DenseBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "એડવાન્સ જમા આપેલા સભ્યો (Advances)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DensePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (advancePayersInfo.isEmpty()) {
                        Text("આ સમયગાળામાં એડવાન્સ સભ્યો ઉપલબ્ધ નથી.", fontSize = 11.sp, color = Color.Gray)
                    } else {
                        advancePayersInfo.forEach { (name, amt) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("• $name", fontSize = 11.sp, color = DenseTitleText)
                                Text("₹$amt", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF005AC1))
                            }
                        }
                    }
                }
            }
        }

        // Section: Pending Month EMI pending
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, DenseBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "બાકી યોગદાન વાળા સભ્યો (EMI Pending Members)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DenseExpenseText
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (pendingMembersInfo.isEmpty()) {
                        Text("બધા સભ્યોનું યોગદાન ચૂકવેલ છે. સરસ!", fontSize = 11.sp, color = Color(0xFF2E7D32))
                    } else {
                        pendingMembersInfo.forEach { (name, pendings) ->
                            val pendingStrs = pendings.joinToString(", ") { "${viewModel.getMonthNameShort(it.second)} ${it.first}" }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("• $name", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DenseTitleText)
                                    Text("₹${pendings.size * 100} Pending", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DenseExpenseText)
                                }
                                Text(
                                    text = "Pending: $pendingStrs",
                                    fontSize = 10.sp,
                                    color = DenseLabelText,
                                    modifier = Modifier.padding(start = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ==================== DIALOG 1: MEMBER DETAIL & CONTRIBUTIONS PAYMENT ====================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MemberDetailDialog(
    member: Member,
    payments: List<MemberPayment>,
    viewModel: MandalViewModel,
    onDismiss: () -> Unit
) {
    val cal = Calendar.getInstance()
    val curYear = cal.get(Calendar.YEAR)
    val curMonth = cal.get(Calendar.MONTH) + 1

    var customAmountToPay by remember { mutableStateOf("100") }
    val pendingMonths = viewModel.calculatePendingMonths(member, payments, curYear, curMonth)
    val futureAdvances = payments.filter { it.forYear > curYear || (it.forYear == curYear && it.forMonth > curMonth) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SaffronSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Member Basic info
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(member.name, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = SaffronPrimary)
                            Text("સંપર્ક: ${member.phone}", fontSize = 12.sp, color = Color.DarkGray)
                            Text("જોડાણ: ${viewModel.getMonthNameShort(member.joinedMonth)} ${member.joinedYear}", fontSize = 11.sp, color = Color.Gray)
                        }
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                }

                item { Divider() }

                // Record Contribution Section
                if (viewModel.isAdmin) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SaffronPrimary.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Text("નવું જમા કરો (Quick Contribution)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SaffronPrimary)
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = customAmountToPay,
                                    onValueChange = { customAmountToPay = it },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    label = { Text("જમા રકમ (Amount)") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1.5f)
                                        .height(56.dp)
                                        .testTag("detail_dialog_amt_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                        focusedBorderColor = SaffronPrimary,
                                        focusedLabelColor = SaffronPrimary,
                                        unfocusedLabelColor = Color.Gray,
                                        focusedPlaceholderColor = Color.Gray,
                                        unfocusedPlaceholderColor = Color.Gray
                                    )
                                )

                                Button(
                                    onClick = {
                                        val amt = customAmountToPay.toIntOrNull() ?: 100
                                        viewModel.addPayment(member.id, amt) {
                                            customAmountToPay = "100"
                                            Toast.makeText(viewModel.getApplication(), "₹$amt/- Settle success!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    contentPadding = PaddingValues(horizontal = 12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1.2f)
                                        .height(48.dp)
                                        .testTag("detail_dialog_record_btn")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("જમા કરો", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Presets
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("100", "300", "500", "1200").forEach { preset ->
                                    InputChip(
                                        selected = customAmountToPay == preset,
                                        onClick = { customAmountToPay = preset },
                                        label = { Text("₹$preset/-", fontSize = 10.sp) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Balance status dues or advance
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("બાકી મહિનાની સૂચી (EMI Pending Months)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SaffronAccent)
                        if (pendingMonths.isEmpty()) {
                            Text("કોઈ યોગદાન બાકી નથી! આ સમય સુધીના બધા જ પૈસા ભરેલા છે. ✔", fontSize = 10.sp, color = Color(0xFF2E7D32))
                        } else {
                            Text("કુલ બાકી તિથિ: ${pendingMonths.size} મહિના [કુલ બાકી: ₹${pendingMonths.size * 100}/-]", fontSize = 10.sp, color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                pendingMonths.forEach { pm ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text("${viewModel.getMonthNameShort(pm.second)} ${pm.first}", color = Color(0xFFC62828), fontSize = 10.sp) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Beautiful custom Send Reminder portion when there are pending months
                if (pendingMonths.isNotEmpty() && viewModel.isAdmin) {
                    item {
                        val context = LocalContext.current
                        val totalDues = pendingMonths.size * 100
                        val pendingNames = pendingMonths.joinToString(", ") { "${viewModel.getMonthNameShort(it.second)} ${it.first}" }
                        val messageVal = "નમસ્તે ${member.name}, નમ્ર વિનંતી કે આપનું મંડળનું યોગદાન (₹$totalDues/- $pendingNames ની બાકી EMI) ચૂકવવાનું બાકી છે. કૃપા કરીને વહેલી તકે જમા કરાવા નમ્ર વિનંતી. આભાર!"
                        
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8)),
                            border = BorderStroke(1.dp, Color(0xFFADC1D6)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = "સભ્યને યાદ સૂચના મોકલો (Send Reminder)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color(0xFF1E3A8A)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "મેસેજ વિગત: \"$messageVal\"",
                                    fontSize = 10.sp,
                                    color = Color.DarkGray,
                                    modifier = Modifier
                                        .background(Color.White, RoundedCornerShape(6.dp))
                                        .padding(8.dp)
                                        .fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            try {
                                                val uri = Uri.parse("smsto:${member.phone}")
                                                val smsIntent = Intent(Intent.ACTION_SENDTO, uri).apply {
                                                    putExtra("sms_body", messageVal)
                                                }
                                                context.startActivity(smsIntent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "મેસેજ એપ ખોલી શકાઈ નથી.", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Sms,
                                            contentDescription = "SMS",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("ટેક્સ્ટ SMS", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = {
                                            try {
                                                val cleanPh = member.phone.replace(" ", "").replace("-", "")
                                                val finalPh = if (cleanPh.startsWith("+")) cleanPh else if (cleanPh.length == 10) "91$cleanPh" else cleanPh
                                                val waUri = Uri.parse("https://api.whatsapp.com/send?phone=$finalPh&text=${Uri.encode(messageVal)}")
                                                val waIntent = Intent(Intent.ACTION_VIEW, waUri)
                                                context.startActivity(waIntent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "વોટ્સએપ ખોલી શકાયું નથી.", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "WhatsApp",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("વોટ્સએપ", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                // Advances list
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("એડવાન્સ તિથિ ક્રેડિટ (Advance Months Paid)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF1565C0))
                        if (futureAdvances.isEmpty()) {
                            Text("કોઈ એડવાન્સ જમા નથી.", fontSize = 10.sp, color = Color.Gray)
                        } else {
                            Text("જમા વિગત: ₹${futureAdvances.sumOf { it.amount }}/-", fontSize = 10.sp, color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                futureAdvances.forEach { fa ->
                                    SuggestionChip(
                                        onClick = {},
                                        label = { Text("${viewModel.getMonthNameShort(fa.forMonth)} ${fa.forYear} (₹${fa.amount})", color = Color(0xFF1565C0), fontSize = 10.sp) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Manage Member deletion
                if (viewModel.isAdmin) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    viewModel.deleteMember(member)
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFC62828))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("સભ્ય ની નોંધ રદ કરો (Delete Member)", color = Color(0xFFC62828), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==================== DIALOG 2: ADD MEMBER FORM ====================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddMemberDialog(
    viewModel: MandalViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val monthsList = (1..12).toList()
    val yearsList = listOf(2025, 2026, 2027, 2028)
    var isMonthDropdownOpen by remember { mutableStateOf(false) }
    var isYearDropdownOpen by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SaffronSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("નવો સભ્ય ઉમેરો (Add Member)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SaffronPrimary)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider()

                OutlinedTextField(
                    value = viewModel.newMemberName,
                    onValueChange = { viewModel.newMemberName = it.uppercase() },
                    label = { Text("સભ્ય નું પૂરું નામ (Full Name)") },
                    placeholder = { Text("e.g. Ramesh Patel") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_member_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = SaffronPrimary,
                        focusedLabelColor = SaffronPrimary,
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = viewModel.newMemberPhone,
                    onValueChange = { viewModel.newMemberPhone = it.uppercase() },
                    label = { Text("મોબાઈલ નંબર (Phone Number)") },
                    placeholder = { Text("e.g. 98765 43210") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_member_phone"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = SaffronPrimary,
                        focusedLabelColor = SaffronPrimary,
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                Text(
                    text = "યોગદાન ક્યારથી ગણવું? (Contribution Starting Month)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Month starting spinner
                    Box(modifier = Modifier.weight(1.5f)) {
                        OutlinedButton(
                            onClick = { isMonthDropdownOpen = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(viewModel.getMonthNameShort(viewModel.newMemberJoinedMonth), fontSize = 11.sp, color = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                        }
                        DropdownMenu(
                            expanded = isMonthDropdownOpen,
                            onDismissRequest = { isMonthDropdownOpen = false }
                        ) {
                            monthsList.forEach { mIndex ->
                                DropdownMenuItem(
                                    text = { Text(viewModel.getMonthName(mIndex), fontSize = 11.sp) },
                                    onClick = {
                                        viewModel.newMemberJoinedMonth = mIndex
                                        isMonthDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }

                    // Year starting spinner
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedButton(
                            onClick = { isYearDropdownOpen = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(viewModel.newMemberJoinedYear.toString(), fontSize = 11.sp, color = Color.Black)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                        }
                        DropdownMenu(
                            expanded = isYearDropdownOpen,
                            onDismissRequest = { isYearDropdownOpen = false }
                        ) {
                            yearsList.forEach { yr ->
                                DropdownMenuItem(
                                    text = { Text(yr.toString(), fontSize = 11.sp) },
                                    onClick = {
                                        viewModel.newMemberJoinedYear = yr
                                        isYearDropdownOpen = false
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = { viewModel.addMember(onSuccess) },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_member_create_btn")
                ) {
                    Text("સભ્યની નોંધણી કરો (Save Member)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ==================== DIALOG 3: ADD EXPENSE (JAVAK) ====================
@Composable
fun AddExpenseDialog(
    viewModel: MandalViewModel,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val categories = listOf("Donation", "Event", "Rent", "Pritsada", "Other")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SaffronSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("મંડળનો ખર્ચ / દાન (Javak expense)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = SaffronAccent)
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider()

                OutlinedTextField(
                    value = viewModel.newExpenseTitle,
                    onValueChange = { viewModel.newExpenseTitle = it.uppercase() },
                    label = { Text("ખર્ચનું નામ / શીર્ષક (Expense Title)") },
                    placeholder = { Text("દા.ત. Ram Mandir Donating") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_expense_title"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = SaffronAccent,
                        focusedLabelColor = SaffronAccent,
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = viewModel.newExpenseAmount,
                    onValueChange = { viewModel.newExpenseAmount = it },
                    label = { Text("ભાવ / ખર્ચ રકમ (Amount ₹)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_expense_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = SaffronAccent,
                        focusedLabelColor = SaffronAccent,
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                OutlinedTextField(
                    value = viewModel.newExpenseDescription,
                    onValueChange = { viewModel.newExpenseDescription = it.uppercase() },
                    label = { Text("ટૂંકી વિગત (Brief details description)") },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = SaffronAccent,
                        focusedLabelColor = SaffronAccent,
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                Text("શ્રેણી (Category Selection)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(84.dp)
                    ) {
                        items(categories.size) { index ->
                            val cat = categories[index]
                            FilterChip(
                                selected = viewModel.newExpenseCategory == cat,
                                onClick = { viewModel.newExpenseCategory = cat },
                                label = { Text(cat, fontSize = 9.sp) },
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = { viewModel.addExpense(onSuccess) },
                    colors = ButtonDefaults.buttonColors(containerColor = SaffronAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_expense_create_btn")
                ) {
                    Text("જાવક નોંધો (Save Expense)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

// ==================== DIALOG 4: REGISTER PAYMENT (AAVAK) ====================
@Composable
fun AddPaymentDialog(
    viewModel: MandalViewModel,
    members: List<Member>,
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var memberDropdownOpen by remember { mutableStateOf(false) }
    var chosenMember by remember { mutableStateOf<Member?>(null) }
    var payAmountPaid by remember { mutableStateOf("100") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = SaffronSurface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("સભ્ય યોગદાન આવક (Record Contribution)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2E7D32))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Divider()

                // Member Spinner Box
                Column {
                    Text("સભ્ય પસંદ કરો (Select Member)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { memberDropdownOpen = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = SaffronSurface)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(chosenMember?.name ?: "પસંદ કરો (Select Member)", color = if (chosenMember != null) Color.Black else Color.Gray, fontSize = 12.sp)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray)
                            }
                        }
                        DropdownMenu(
                            expanded = memberDropdownOpen,
                            onDismissRequest = { memberDropdownOpen = false }
                        ) {
                            if (members.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("કોઈ સભ્યો ઉપલબ્ધ નથી", fontSize = 11.sp) },
                                    onClick = { memberDropdownOpen = false }
                                )
                            } else {
                                members.forEach { m ->
                                    DropdownMenuItem(
                                        text = { Text(m.name, fontSize = 11.sp) },
                                        onClick = {
                                            chosenMember = m
                                            memberDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // Amount Text field
                OutlinedTextField(
                    value = payAmountPaid,
                    onValueChange = { payAmountPaid = it },
                    label = { Text("જમા ભાડું / યોગદાન રકમ (Amount ₹)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dialog_payment_amount"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedBorderColor = Color(0xFF2E7D32),
                        focusedLabelColor = Color(0xFF2E7D32),
                        unfocusedLabelColor = Color.Gray,
                        focusedPlaceholderColor = Color.Gray,
                        unfocusedPlaceholderColor = Color.Gray
                    )
                )

                // Quick presets buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("100", "300", "500", "1200").forEach { p ->
                        Button(
                            onClick = { payAmountPaid = p },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (payAmountPaid == p) Color(0xFF2E7D32) else SaffronBackground
                            ),
                            elevation = ButtonDefaults.buttonElevation(1.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                "₹ $p/-",
                                color = if (payAmountPaid == p) Color.White else Color(0xFF2E7D32),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        val mId = chosenMember?.id ?: return@Button
                        val amt = payAmountPaid.toIntOrNull() ?: 100
                        viewModel.addPayment(mId, amt, onSuccess)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(10.dp),
                    enabled = chosenMember != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dialog_payment_create_btn")
                ) {
                    Text("આવક જમા કરો (Save Contribution)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

fun showDatePicker(context: Context, initialDate: Long, onDateSelected: (Long) -> Unit) {
    val calendar = Calendar.getInstance().apply { timeInMillis = initialDate }
    android.app.DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val resultCal = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            onDateSelected(resultCal.timeInMillis)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}
