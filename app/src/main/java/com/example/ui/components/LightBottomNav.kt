package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.viewmodel.AppTab

@Composable
fun LightBottomNav(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .testTag("light_bottom_nav"),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 10.dp,
        tonalElevation = 3.dp
    ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 4.dp)
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NavItem(
                tab = AppTab.VIDEOS,
                isSelected = currentTab == AppTab.VIDEOS,
                icon = Icons.Default.PlayCircle,
                label = "Videos",
                badge = "Feed",
                onClick = { onTabSelected(AppTab.VIDEOS) }
            )
            NavItem(
                tab = AppTab.COMMUNITY,
                isSelected = currentTab == AppTab.COMMUNITY,
                icon = Icons.Default.Groups,
                label = "Community",
                badge = null,
                onClick = { onTabSelected(AppTab.COMMUNITY) }
            )
            NavItem(
                tab = AppTab.CHAT,
                isSelected = currentTab == AppTab.CHAT,
                icon = Icons.AutoMirrored.Filled.Chat,
                label = "Messages",
                badge = "3",
                onClick = { onTabSelected(AppTab.CHAT) }
            )
            NavItem(
                tab = AppTab.DATING,
                isSelected = currentTab == AppTab.DATING,
                icon = Icons.Default.Favorite,
                label = "Dating",
                badge = "18+",
                badgeColor = Color(0xFFD32F2F),
                onClick = { onTabSelected(AppTab.DATING) }
            )
            NavItem(
                tab = AppTab.IBIBIO,
                isSelected = currentTab == AppTab.IBIBIO,
                icon = Icons.Default.Translate,
                label = "Ibibio Zone",
                badge = "Culture",
                badgeColor = LightEmeraldGreen,
                onClick = { onTabSelected(AppTab.IBIBIO) }
            )
            NavItem(
                tab = AppTab.SPORTS,
                isSelected = currentTab == AppTab.SPORTS,
                icon = Icons.Default.SportsSoccer,
                label = "Sports",
                badge = "Live",
                badgeColor = Color(0xFF2E7D32),
                onClick = { onTabSelected(AppTab.SPORTS) }
            )
            NavItem(
                tab = AppTab.OPPORTUNITIES,
                isSelected = currentTab == AppTab.OPPORTUNITIES,
                icon = Icons.Default.WorkOutline,
                label = "Opportunities",
                badge = null,
                onClick = { onTabSelected(AppTab.OPPORTUNITIES) }
            )
            NavItem(
                tab = AppTab.PROFILE,
                isSelected = currentTab == AppTab.PROFILE,
                icon = Icons.Default.AccountCircle,
                label = "Profile",
                badge = null,
                onClick = { onTabSelected(AppTab.PROFILE) }
            )
        }
    }
}

@Composable
private fun NavItem(
    tab: AppTab,
    isSelected: Boolean,
    icon: ImageVector,
    label: String,
    badge: String?,
    badgeColor: Color = LightSunGold,
    onClick: () -> Unit
) {
    val bg = if (isSelected) LightEmeraldGreen.copy(alpha = 0.14f) else Color.Transparent
    val tint = if (isSelected) LightEmeraldGreen else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(tab.tag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
                if (badge != null) {
                    Surface(
                        color = badgeColor,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 10.dp, y = (-4).dp)
                    ) {
                        Text(
                            text = badge,
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = tint
            )
        }
    }
}
