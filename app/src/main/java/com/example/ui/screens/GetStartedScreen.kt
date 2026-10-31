package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange

@Composable
fun GetStartedScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_scale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF003D1E),
                        Color(0xFF0B1710),
                        Color(0xFF060B08)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("get_started_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Official Uploaded Logo
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.size(110.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.the_light_logo),
                        contentDescription = "THE LIGHT OF AKWA IBOM Logo",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Brand Title
                Text(
                    text = "THE LIGHT",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        letterSpacing = 1.5.sp
                    ),
                    color = LightSunGold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "THE LIGHT OF AKWA IBOM",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFA5D6A7),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Slogan
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = LightSunriseOrange.copy(alpha = 0.2f),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(LightSunriseOrange, LightSunGold)))
                ) {
                    Text(
                        text = "“BRIGHTER WE SHINE!”",
                        color = LightSunriseOrange,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Welcome to your premier all-in-one African and global digital home. Instant vertical entertainment, lively community chats, dating for adults 18+, Ibibio cultural pride, live football, and local opportunities.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Highlights Grid / List
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureHighlightCard(
                        icon = Icons.Default.PlayCircle,
                        iconColor = LightSunGold,
                        title = "TikTok-Style Video Feed",
                        description = "Immediate full-screen autoplay featuring African, Chinese, and Philippine short movies and entertainment."
                    )
                    FeatureHighlightCard(
                        icon = Icons.Default.Groups,
                        iconColor = LightEmeraldGreen,
                        title = "Community & Voice Messaging",
                        description = "Post updates by LGA, chat 1-on-1, join vibrant group channels, and send instant voice notes."
                    )
                    FeatureHighlightCard(
                        icon = Icons.Default.Favorite,
                        iconColor = Color(0xFFEF5350),
                        title = "Light Dating (18+ Adults)",
                        description = "Verified profiles, swiping, mutual matching, private messaging, and strict privacy safety controls."
                    )
                    FeatureHighlightCard(
                        icon = Icons.Default.Translate,
                        iconColor = Color(0xFF42A5F5),
                        title = "Ibibio Language Zone",
                        description = "Strictly Ibibio communication lounge, interactive audio pronunciation phrasebook, and wisdom proverbs."
                    )
                    FeatureHighlightCard(
                        icon = Icons.Default.SportsSoccer,
                        iconColor = LightSunGold,
                        title = "Live European Sports & Entertainment",
                        description = "Live EPL & UEFA Champions League scores, fixtures, standings, and entertainment news."
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // GET STARTED Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(buttonScale)
                        .testTag("btn_get_started"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightEmeraldGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 2.dp)
                ) {
                    Text(
                        text = "GET STARTED",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Tap to enter the Entertainment Video Feed immediately",
                    color = Color.White.copy(alpha = 0.65f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun FeatureHighlightCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF132219),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(Color(0xFF1E3627), Color(0xFF132219)))),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color.White
                )
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.75f),
                    lineHeight = 15.sp
                )
            }
        }
    }
}
