package com.metromart.wedapp.ui.auth

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.metromart.wedapp.R

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        Image(
            painter = painterResource(id = R.drawable.day_raining),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp) // Reduced from 100.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Welcome Back!",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthTextField(
            label = "Email address",
            value = email,
            onValueChange = { 
                email = it
                emailError = if (isValidEmail(it)) null else "Invalid email format"
            },
            placeholder = "Enter email address",
            error = emailError,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            label = "Password",
            value = password,
            onValueChange = { 
                password = it
                passwordError = if (it.length >= 6) null else "Password must be at least 6 characters"
            },
            placeholder = "Enter password",
            isPassword = true,
            error = passwordError,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray)
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val isEmailValid = isValidEmail(email)
                val isPasswordValid = password.length >= 6
                
                if (isEmailValid && isPasswordValid) {
                    onLoginSuccess()
                } else {
                    if (!isEmailValid) emailError = "Please enter a valid email"
                    if (!isPasswordValid) passwordError = "Password is too short"
                }
            },
            modifier = Modifier
                .width(120.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Log in", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = { /* Handle forgot password */ }) {
            Text(
                text = "Forgot password?",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register", color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLoginSuccess = {}, onNavigateToRegister = {})
}

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        Image(
            painter = painterResource(id = R.drawable.day_raining),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp) // Reduced from 100.dp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthTextField(
            label = "Email address",
            value = email,
            onValueChange = { 
                email = it
                emailError = if (isValidEmail(it)) null else "Invalid email format"
            },
            placeholder = "Enter email address",
            error = emailError,
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            label = "Password",
            value = password,
            onValueChange = { 
                password = it
                passwordError = if (it.length >= 6) null else "Password must be at least 6 characters"
            },
            placeholder = "Enter password",
            isPassword = true,
            error = passwordError,
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray)
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val isEmailValid = isValidEmail(email)
                val isPasswordValid = password.length >= 6
                
                if (isEmailValid && isPasswordValid) {
                    onRegisterSuccess()
                } else {
                    if (!isEmailValid) emailError = "Please enter a valid email"
                    if (!isPasswordValid) passwordError = "Password must be at least 6 characters"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
        ) {
            Text("Register", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login", color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen(onRegisterSuccess = {}, onNavigateToLogin = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    error: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            placeholder = { Text(placeholder, color = Color.LightGray) },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            leadingIcon = leadingIcon ?: {
                Row(modifier = Modifier.padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(24.dp).background(Color(0xFFFF5F00), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.size(24.dp).background(Color(0xFFEB001B), CircleShape).offset(x = (-12).dp))
                }
            },
            trailingIcon = {
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.LightGray)
            },
            shape = RoundedCornerShape(16.dp),
            isError = error != null,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorContainerColor = Color.White,
                errorIndicatorColor = Color.Red
            )
        )
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
