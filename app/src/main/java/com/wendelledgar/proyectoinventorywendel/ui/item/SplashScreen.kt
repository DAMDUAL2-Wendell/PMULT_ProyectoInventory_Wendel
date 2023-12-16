package com.wendelledgar.proyectoinventorywendel.ui.item

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.wendelledgar.proyectoinventorywendel.MainActivity
import com.wendelledgar.proyectoinventorywendel.R
import com.wendelledgar.proyectoinventorywendel.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay

object SplashScreenDestination : NavigationDestination {
    override val route: String = "splash_screen"
    override val titleRes: Int = R.string.splash_screen
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SplashScreenView(
    navigateToHome: () -> Unit, modifier: Modifier = Modifier
) {

    var visible by remember { mutableStateOf(false) }

    val fadeInDelay = 1500
    val fadeOutDelay = 800

    Scaffold(content = {
        Box(modifier = Modifier
            .background(colorResource(id = R.color.black))
            .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(durationMillis = fadeInDelay, easing = LinearEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = fadeOutDelay, easing = LinearEasing))
            ) {
                Column(
                    modifier = Modifier
                        .background(colorResource(id = R.color.black))
                        .fillMaxSize()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(top = 50.dp)
                    ) {
                        imagen()
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                    ) {

                        Text(
                            stringResource(id = R.string.splash_screen),
                            fontSize = 20.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                }

                LaunchedEffect(true){
                    delay(fadeInDelay.toLong())
                    visible = false
                }

                LaunchedEffect(true){
                    delay((fadeInDelay + fadeOutDelay).toLong())
                    navigateToHome.invoke()
                }

            }
        }

        LaunchedEffect(true) {
            visible = true
        }

    })


}

@Composable
fun imagen(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier.fillMaxSize(),
        painter = painterResource(R.drawable.rutinas_logo_splash_screen),
        contentDescription = null,
        colorFilter = ColorFilter.lighting(multiply = Color.White, add = Color.White)
    )
}

@Preview
@Composable
fun previewSplashScreen() {
    SplashScreenView(navigateToHome = { /*TODO*/ })
}