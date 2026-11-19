package edu.iu.luddy.i403prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                topBar = { TopBar(this, "Settings") { finish() } },
                modifier = Modifier.fillMaxSize(),
                bottomBar = { }
            ) { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Privacy Settings
                            Column() {
                                Column() {
                                    Text("Privacy", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                                }

                                Column() {
                                    Setting("Keep computation on device", true)
                                    Setting("Share location with outside AI", false)
                                    Setting("Fenced AI context (AI only see what you allow)", true)
                                    Setting("Ask before every action", true)
                                }
                            }

                            // Behavior Settings
                            Column() {
                                Column() {
                                    Text("Behavior", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                    HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
                                }

                                Column() {
                                    Setting("Always on display", true)
                                    Setting("Raise to wake", true)
                                    Setting("Simplify directions when needed", false)
                                    Setting("Ask before every action", true)
                                }
                            }

                            // Wipe Button
                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(4.dp),
                                colors = ButtonColors(
                                    containerColor = Color(0xFF872323),
                                    contentColor = Color.White,
                                    disabledContainerColor = Color(0xFF872323),
                                    disabledContentColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(48.dp)
                                    .fillMaxWidth()
                            ) {
                                Text("Wipe ALL Data", fontSize = 28.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Reusable `Text` plus `Switch` to mimic a real setting toggle.
 *
 * @param text the setting's title
 * @param checked default state of the setting (cannot be changed; Wizard of Oz)
 */
@Composable
fun Setting(text: String, checked: Boolean) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text, fontSize = 16.sp)

        Switch(
            checked = checked,
            onCheckedChange = {  }
        )
    }
}