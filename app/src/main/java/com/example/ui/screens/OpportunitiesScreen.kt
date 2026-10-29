package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnnouncementItem
import com.example.data.OpportunityItem
import com.example.ui.theme.LightEmeraldGreen
import com.example.ui.theme.LightSunGold
import com.example.ui.theme.LightSunriseOrange
import com.example.viewmodel.LightViewModel

@Composable
fun OpportunitiesScreen(
    viewModel: LightViewModel,
    modifier: Modifier = Modifier
) {
    val opportunities by viewModel.allOpportunities.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val selectedCategory by viewModel.oppCategoryFilter.collectAsState()
    var isCreateOppOpen by remember { mutableStateOf(false) }
    var selectedOppDetail by remember { mutableStateOf<OpportunityItem?>(null) }

    val filteredOpps = remember(opportunities, selectedCategory) {
        if (selectedCategory == "All") opportunities
        else opportunities.filter { it.category.equals(selectedCategory, ignoreCase = true) }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("opportunities_screen"),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isCreateOppOpen = true },
                containerColor = LightEmeraldGreen,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.testTag("btn_post_opportunity_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.AddBusiness,
                    contentDescription = "Post Opportunity"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Pinned State Announcements Banner
            if (announcements.isNotEmpty()) {
                item {
                    val latest = announcements.first()
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        colors = CardDefaults.cardColors(containerColor = LightEmeraldGreen),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = LightSunGold
                                ) {
                                    Text(
                                        text = latest.badge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = latest.dateStr,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = latest.title,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = latest.content,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.9f),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            // Categories Filter Row
            item {
                val categories = listOf("All", "Jobs", "Scholarships", "Business Grants", "Skills Training", "Tourism & Heritage")
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { viewModel.setOppCategoryFilter(cat) },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LightEmeraldGreen,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("opp_cat_$cat")
                        )
                    }
                }
            }

            // Opportunities list
            items(filteredOpps, key = { it.id }) { opp ->
                OpportunityCardItem(
                    opp = opp,
                    onBookmark = { viewModel.toggleOpportunityBookmark(opp) },
                    onClick = { selectedOppDetail = opp }
                )
            }
        }
    }

    // Opportunity Detail Dialog
    if (selectedOppDetail != null) {
        val detail = selectedOppDetail!!
        val context = LocalContext.current
        AlertDialog(
            onDismissRequest = { selectedOppDetail = null },
            title = {
                Text(
                    text = detail.title,
                    fontWeight = FontWeight.Bold,
                    color = LightEmeraldGreen,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Organization: ${detail.organization}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Location: ${detail.location}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Deadline: ${detail.deadline}",
                        fontSize = 12.sp,
                        color = LightSunriseOrange,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider()
                    Text("Description:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(detail.description, fontSize = 13.sp, lineHeight = 18.sp)
                    Text("Requirements:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(detail.requirements, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Contact / Portal: ${detail.contactOrUrl}", fontWeight = FontWeight.Bold, color = LightEmeraldGreen, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Opportunity: ${detail.title} by ${detail.organization}\nApply via: ${detail.contactOrUrl}\nShared from THE LIGHT of Akwa Ibom"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Opportunity via"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen)
                ) {
                    Text("Share Opportunity")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedOppDetail = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Post Opportunity Dialog
    if (isCreateOppOpen) {
        CreateOpportunityDialog(
            onDismiss = { isCreateOppOpen = false },
            onSubmit = { title, org, category, location, deadline, desc, reqs, contact ->
                viewModel.createOpportunity(title, org, category, location, deadline, desc, reqs, contact)
                isCreateOppOpen = false
            }
        )
    }
}

@Composable
private fun OpportunityCardItem(
    opp: OpportunityItem,
    onBookmark: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
            .testTag("opp_card_${opp.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = when (opp.category.lowercase()) {
                        "jobs" -> LightEmeraldGreen.copy(alpha = 0.15f)
                        "scholarships" -> Color(0xFF1976D2).copy(alpha = 0.15f)
                        "business grants" -> LightSunGold.copy(alpha = 0.2f)
                        "tourism & heritage" -> LightSunriseOrange.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = opp.category.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (opp.category.lowercase()) {
                            "jobs" -> LightEmeraldGreen
                            "scholarships" -> Color(0xFF1976D2)
                            "business grants" -> Color(0xFF8C5300)
                            "tourism & heritage" -> LightSunriseOrange
                            else -> MaterialTheme.colorScheme.onSurface
                        },
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                IconButton(onClick = onBookmark, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (opp.isBookmarked) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (opp.isBookmarked) LightSunGold else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = opp.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "${opp.organization} • ${opp.location}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = opp.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Deadline: ${opp.deadline}",
                    fontSize = 11.sp,
                    color = LightSunriseOrange,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "View Details →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightEmeraldGreen
                )
            }
        }
    }
}

@Composable
fun CreateOpportunityDialog(
    onDismiss: () -> Unit,
    onSubmit: (title: String, org: String, category: String, location: String, deadline: String, desc: String, reqs: String, contact: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var org by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Jobs") }
    var location by remember { mutableStateOf("Uyo, Akwa Ibom") }
    var deadline by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var reqs by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

    val categories = listOf("Jobs", "Scholarships", "Business Grants", "Skills Training", "Tourism & Heritage")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Post Opportunity / Grant", fontWeight = FontWeight.Bold, color = LightEmeraldGreen) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("e.g. Agri-Tech Fellowship") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = org,
                    onValueChange = { org = it },
                    label = { Text("Organization") },
                    placeholder = { Text("e.g. Ibom Youth Council") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contact,
                    onValueChange = { contact = it },
                    label = { Text("Application Link or Email") },
                    placeholder = { Text("e.g. apply@akwaibom.org") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(title, org, category, location, deadline, desc, reqs, contact) },
                colors = ButtonDefaults.buttonColors(containerColor = LightEmeraldGreen),
                enabled = title.isNotBlank() && desc.isNotBlank()
            ) {
                Text("Publish Opportunity")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
