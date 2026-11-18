package edu.iu.luddy.i403prototype

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.iu.luddy.i403prototype.ui.theme.I403PrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hardcoded Streets
        val streets: List<String> = listOf<String>(
            "Kirkwood Avenue",
            "Walnut Street",
            "2nd Street",
            "3rd Street",
            "Atwater Avenue",
            "7th Street",
            "10th Street",
            "Eagleson Avenue",
            "University Street",
            "College Avenue",
            "Sunrise Drive",
            "Union Street",
        )

        // Mutables
        setContent {
            val orientatorMode = remember { mutableIntStateOf(0) }
            val showAISheet = remember { mutableStateOf(0) }

            I403PrototypeTheme {
                Scaffold(
                    topBar = { TopBar("Navigation") },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { Actions(showAISheet) }
                ) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Orientator(orientatorMode, 200, "Right", streets[0])
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(title: String) {
    Column() {
        // Bar
        CenterAlignedTopAppBar(
            title = { Text(title) },
            expandedHeight = 52.dp,
            actions = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    // Settings
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.settings),
                            contentDescription = "Settings",
                        )
                    }

                    // Mute Toggle
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.mute),
                            contentDescription = "Mute",
                        )
                    }
                }
            }
        )

        // Divider
        HorizontalDivider(thickness = 2.dp, color = Color.DarkGray)
    }
}

@Composable
fun Orientator(mode: MutableState<Int>, distance: Int, direction: String, street: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (mode.value == 0) Arrangement.SpaceAround else Arrangement.Bottom,
        modifier = Modifier
            .height(620.dp)
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp) // Margin
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp))
            .paint(
                painter = if (mode.value == 0) ColorPainter(Color.Transparent) else painterResource(R.drawable.map_view),
                contentScale = ContentScale.Inside
            )
            .padding(
                start = if (mode.value == 0) 20.dp else 20.dp,
                top = if (mode.value == 0) 20.dp else 0.dp,
                end = if (mode.value == 0) 20.dp else 20.dp,
                bottom = if (mode.value == 0) 20.dp else 36.dp
            ) // Actual Padding
    ) {
        if (mode.value == 0) {
            Text("${distance}ft • Turn $direction", fontSize = 36.sp)

            Image(
                painter = painterResource(
                    when (direction) {
                        "left" -> R.drawable.left
                        else -> R.drawable.right
                    }
                ),
                contentDescription = "Turn $direction arrow",
                modifier = Modifier.size(256.dp)
            )

            Text(street, fontSize = 36.sp)
        }

        IconButton(
            onClick = { if (mode.value == 0) mode.value = 1 else mode.value = 0 },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            colors = IconButtonColors(
                containerColor = Color.Gray,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.map),
                    contentDescription = "Map",
                )

                Text(
                    text = if (mode.value == 0) "View Map" else "View Directions",
                    fontSize = 32.sp
                )
            }
        }
    }
}

@Composable
fun Actions(showAISheet: MutableState<Int>) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        // Emergency Services Dialog Launcher
        IconButton(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.emergency),
                contentDescription = "Emergency (Star)",
                modifier = Modifier.size(48.dp)
            )
        }

        // AI Sheet Launcher
        IconButton(
            onClick = { if (showAISheet.value == 0) showAISheet.value = 1 else showAISheet.value = 0},
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .height(72.dp)
                .width(200.dp),
            colors = IconButtonColors(
                containerColor = Color(0xFF4287F5),
                contentColor = Color.White,
                disabledContainerColor = Color(0xFF4287F5),
                disabledContentColor = Color.White
            )
        ) {
            Icon(
                painter = painterResource(R.drawable.robot),
                contentDescription = "AI Mascot (Robot)",
                modifier = Modifier.size(48.dp)
            )
        }

        if (showAISheet.value == 1) AISheet(onDismiss = { showAISheet.value = 0 })

        // Directions Page Launcher
        IconButton(
            onClick = {},
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(72.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.navigation),
                contentDescription = "Navigation (Arrow)",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)
        ) {
            // James
            ChatEntry(
                type = 0,
                alt = "AI Mascot (Robot)",
                name = "Mr. James Rouety",
                text = "Hello! I'm here for you every step of the way. How can I help you navigate today?"
            )

            // You
            ChatEntry(
                type = 1,
                alt = "Yourself (Person)",
                name = "You",
                text = "Please navigate me home.. I'm drunk, and cannot drive. Please use shortest route, no mistakes."
            )

            IconButton(
                onClick = { }, // Dummy Button (Wizard of Oz)
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth(),
                colors = IconButtonColors(
                    containerColor = Color(0xFF23873D),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF23873D),
                    disabledContentColor = Color.White
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(R.drawable.waveform),
                        contentDescription = "AI Mascot (Robot)",
                        modifier = Modifier.size(32.dp)
                    )

                    Text("Speak to Mr. James Routey", fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun ChatEntry(type: Int, alt: String, name: String, text: String) {
    Column() {
        Row(
            horizontalArrangement = if (type == 0) Arrangement.Start else Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (type == 0) {
                    Icon(
                        painter = painterResource(
                            when(type) {
                                0 -> R.drawable.robot
                                else -> R.drawable.person
                            }
                        ),
                        contentDescription = alt,
                        modifier = Modifier.size(48.dp)
                    )

                    Text(name, fontSize = 26.sp)
                } else { // Reverse View
                    Text(name, fontSize = 26.sp)

                    Icon(
                        painter = painterResource(
                            when(type) {
                                0 -> R.drawable.robot
                                else -> R.drawable.person
                            }
                        ),
                        contentDescription = alt,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Gray)

        Text(
            text = text,
            textAlign = if (type == 0) TextAlign.Left else TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (type == 0) 6.dp else 0.dp,
                    end = if (type == 0) 0.dp else 6.dp
                )
        )
    }
}