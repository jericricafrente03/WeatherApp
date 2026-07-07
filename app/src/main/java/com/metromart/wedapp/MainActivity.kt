package com.metromart.wedapp

import android.graphics.Color as AndroidColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import kotlinx.serialization.Serializable
import com.metromart.wedapp.data.remote.RetrofitInstance
import com.metromart.wedapp.data.remote.WeatherRemoteDataSourceImpl
import com.metromart.wedapp.data.repository.WeatherRepositoryImpl
import com.metromart.wedapp.ui.auth.LoginScreen
import com.metromart.wedapp.ui.auth.RegisterScreen
import com.metromart.wedapp.ui.screens.CurrentWeatherScreen
import com.metromart.wedapp.ui.screens.WeatherListScreen
import com.metromart.wedapp.ui.theme.WedAppTheme
import com.metromart.wedapp.ui.viewmodel.WeatherUiState
import com.metromart.wedapp.ui.viewmodel.WeatherViewModel

@Serializable
sealed interface Dest : NavKey {
    @Serializable
    data object Login : Dest
    @Serializable
    data object Register : Dest
    @Serializable
    data object Main : Dest
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(AndroidColor.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        setContent {
            WedAppTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
fun WeatherApp(
    viewModel: WeatherViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val remoteDataSource = WeatherRemoteDataSourceImpl(RetrofitInstance.api)
                val repository = WeatherRepositoryImpl(remoteDataSource)
                return WeatherViewModel(repository) as T
            }
        }
    )
) {
    val backStack = rememberNavBackStack(Dest.Login)

    val entryProvider = entryProvider<NavKey> {
        entry<Dest.Login> {
            LoginScreen(
                onLoginSuccess = { 
                    backStack.clear()
                    backStack.add(Dest.Main)
                },
                onNavigateToRegister = { backStack.add(Dest.Register) }
            )
        }
        entry<Dest.Register> {
            RegisterScreen(
                onRegisterSuccess = { 
                    backStack.clear()
                    backStack.add(Dest.Main)
                },
                onNavigateToLogin = { 
                    if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
                    else {
                        backStack.clear()
                        backStack.add(Dest.Login)
                    }
                }
            )
        }
        entry<Dest.Main> {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val tabs = listOf("Current", "Forecast")

            WeatherAppContent(
                uiState = uiState,
                selectedTabIndex = selectedTabIndex,
                tabs = tabs,
                onTabSelected = { selectedTabIndex = it },
                onSignOut = {
                    backStack.clear()
                    backStack.add(Dest.Login)
                }
            )
        }
    }

    NavDisplay(
        entries = rememberDecoratedNavEntries<NavKey>(
            backStack = backStack,
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
            entryProvider = entryProvider
        ),
        onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
    )
}

@Composable
fun WeatherAppContent(
    uiState: WeatherUiState,
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit,
    onSignOut: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is WeatherUiState.Error && uiState.fallbackData != null) {
            snackbarHostState.showSnackbar(
                message = "Using offline static data",
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Indefinite
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Surface(
                color = Color(0xFF2E335A), // Unified dark purple background
                tonalElevation = 0.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().statusBarsPadding(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onSignOut) {
                            Text("Sign Out", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        }
                    }
                    TabRow(
                        selectedTabIndex = selectedTabIndex,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        indicator = {}, // Removed indicator (underline)
                        divider = {},
                        modifier = Modifier
                            .padding(horizontal = 48.dp, vertical = 8.dp)
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                            .padding(4.dp)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val selected = selectedTabIndex == index
                            Tab(
                                selected = selected,
                                onClick = { onTabSelected(index) },
                                text = {
                                    Text(
                                        text = title,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                },
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selected) Color.White.copy(alpha = 0.2f) else Color.Transparent)
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2E335A))) {
            when (uiState) {
                is WeatherUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFF1E88E5)
                    )
                }
                is WeatherUiState.Error -> {
                    Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                        Text(
                            text = "Offline Mode - Error: ${uiState.message}",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall
                        )
                        uiState.fallbackData?.let { data ->
                            when (selectedTabIndex) {
                                0 -> CurrentWeatherScreen(weather = data)
                                1 -> WeatherListScreen(weather = data)
                            }
                        } ?: Text(text = "No cached data available", modifier = Modifier.padding(16.dp))
                    }
                }
                is WeatherUiState.Success -> {
                    Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                        when (selectedTabIndex) {
                            0 -> CurrentWeatherScreen(weather = uiState.data)
                            1 -> WeatherListScreen(weather = uiState.data)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherAppPreview() {
    WedAppTheme {
        WeatherApp()
    }
}
