package edu.iu.luddy.i403prototype

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import edu.iu.luddy.i403prototype.ui.theme.I403PrototypeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            I403PrototypeTheme {
                // Need to pass context for launching other activities
                Scaffold(
                    topBar = { TopBar(this, "Navigation") { } },
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { Actions(this) }
                ) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Orientator(200, "Right", "Kirkwood Avenue")
                    }
                }
            }
        }
    }
}

/**
 * Application-wide center-alinged `TopAppBar()` showcasing constant application actions.
 * Such as going back to the previous page (back arrow), settings page (cog), and mute volume.
 * Each page has a title, shown in the center.
 *
 * @param context context of the `MainActivity` (launches intents)
 * @param title the page title
 * @param onNavigate what the back arrow should do (not present on `MainActivity`; `finish()`)
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(context: Context, title: String, onNavigate: () -> Unit) {
    Column() {
        // Bar
        CenterAlignedTopAppBar(
            title = { Text(title) },
            expandedHeight = 52.dp,
            navigationIcon = {
                if (title != "Navigation") { // No need to show a back arrow on the `MainActivity`
                    IconButton(
                        onClick = onNavigate,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Go Back (Arrow)",
                        )
                    }
                }
            },
            actions = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    // Settings
                    IconButton(
                        onClick = {
                            val intent = Intent(context, Settings::class.java)
                            context.startActivity(intent)
                        },
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

/**
 * Showcases directions in a huge format for the user. These directions are paired with the distance,
 * direction (plus its word form), and the current street. If the user wishes to an overhead view, they
 * can click the "View Map" button to toggle a map.
 *
 * @param distance numerical feet until the turn
 * @param direction what change of current direction is next (to get onto this road)
 * @param street the street name
 */
@Composable
fun Orientator(distance: Int, direction: String, street: String) {
    val mode = rememberSaveable { mutableIntStateOf(0) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (mode.intValue == 0) Arrangement.SpaceAround else Arrangement.Bottom,
        modifier = Modifier
            .height(620.dp)
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 20.dp) // Margin
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .paint(
                painter = if (mode.intValue == 0) ColorPainter(Color.Transparent) else painterResource(
                    R.drawable.map_view
                ),
                contentScale = ContentScale.Inside
            )
            .padding(
                start = if (mode.intValue == 0) 20.dp else 20.dp,
                top = if (mode.intValue == 0) 20.dp else 0.dp,
                end = if (mode.intValue == 0) 20.dp else 20.dp,
                bottom = if (mode.intValue == 0) 20.dp else 36.dp
            ) // Actual Padding
    ) {
        if (mode.intValue == 0) {
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
            onClick = { if (mode.intValue == 0) mode.intValue = 1 else mode.intValue = 0 },
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
                    text = if (mode.intValue == 0) "View Map" else "View Directions",
                    fontSize = 32.sp
                )
            }
        }
    }
}

/**
 * A semi-application-wide `bottomBar` of actions that the user can do main user actions
 * with. Most importantly, with the AI agent (displayed via a popout sheet) which allows
 * the user to converse and make important decisions with. Then, the emergency contacts
 * allowing the user to contact 911, IUPD, family members, and most importantly Prof.
 * James. Lastly is the directions page, which just showcases a summary of turns or
 * the possibility of changing your points (A, B).
 *
 * @param context of the `MainActivity` (launches intents)
 */
@Composable
fun Actions(context: Context) {
    val showAISheet = rememberSaveable { mutableStateOf(false) }
    val showEmergencyDialog = rememberSaveable { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    ) {
        // Emergency Services Dialog Launcher
        ActionButton(0, "Emergency Contacts (Star)") { showEmergencyDialog.value = !showEmergencyDialog.value }

        if (showEmergencyDialog.value) {
            EmergencyDialog() { showEmergencyDialog.value = false }
        }

        // AI Sheet Launcher
        ActionButton(1, "AI Mascot (Robot)") { showAISheet.value = !showAISheet.value }

        if (showAISheet.value) AISheet(onDismiss = { showAISheet.value = false })

        // Directions Page Launcher
        if (context is MainActivity) {
            ActionButton(2, "Navigation (Arrow)") {
                val intent = Intent(context, Directions::class.java)
                context.startActivity(intent)
            }
        }
    }
}

/**
 * Simple reusable and adaptable button component for user actions.
 * Click events are controlled at invocation location, as to save
 * this from being a massive function.
 *
 * @param type the action type (0: emergency, 1: AI, 2: directions)
 * @param alt the `contentDescription` of the icons
 * @param onClick lambda for determining how to launch the action
 */
@Composable
fun ActionButton(type: Int, alt: String, onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .height(72.dp)
            .width(if (type == 1) 200.dp else 72.dp),
        colors = IconButtonColors(
            containerColor = if (type == 1) Color(0xFF4287F5) else Color.Transparent,
            contentColor = if (type == 1) Color.White else Color.DarkGray,
            disabledContainerColor = if (type == 1) Color(0xFF4287F5) else Color.Transparent,
            disabledContentColor = if (type == 1) Color.White else Color.DarkGray
        )
    ) {
        Icon(
            painter = painterResource(
                when (type) {
                    0 -> R.drawable.emergency
                    1 -> R.drawable.robot
                    else -> R.drawable.navigation
                }
            ),
            contentDescription = alt,
            modifier = Modifier.size(48.dp)
        )
    }
}

/**
 * Shows the user a bottom sheet of a mock AI conversation (big part of our Wizard of Oz),
 * with a dummy button for voice capture.
 *
 * @param onDismiss toggles a `MutableState<Boolean>` outside this function to handle the visibility state
 */
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
                entity = 0,
                alt = "AI Mascot (Robot)",
                name = "Mr. James Rouety",
                text = "Hello! I'm here for you every step of the way. How can I help you navigate today?"
            )

            // You
            ChatEntry(
                entity = 1,
                alt = "Yourself (Person)",
                name = "You",
                text = "Please navigate me home.. I'm drunk and cannot drive. Please use shortest route, no mistakes."
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

/**
 * Chat component to build towards a "mock conversation." Depending on the `entity` it changes
 * the `name` and the icon associated with it. The `text` is what the `entity` says in this
 * conversation.
 *
 * @param entity either the AI (0) or a person (1; you)
 * @param alt the `contentDescription` of the icons
 * @param name the name of the `entity`
 * @param text what the `entity` says
 */
@Composable
fun ChatEntry(entity: Int, alt: String, name: String, text: String) {
    Column() {
        Row(
            horizontalArrangement = if (entity == 0) Arrangement.Start else Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (entity == 0) {
                    Icon(
                        painter = painterResource(
                            when(entity) {
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
                            when(entity) {
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
            fontSize = 16.sp,
            textAlign = if (entity == 0) TextAlign.Left else TextAlign.Right,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (entity == 0) 6.dp else 0.dp,
                    end = if (entity == 0) 0.dp else 6.dp
                )
        )
    }
}

/**
 * Brings a `Dialog` to the foreground to allow the user to immediately (dummy) call emergency contacts.
 * It includes: 911, IUPD, family members, and Prof. James, just as dummy buttons (don't feel like
 * programming that).
 *
 * @param onDismiss toggles a `MutableState<Boolean>` outside this function to handle the visibility state
 */
@Composable
fun EmergencyDialog(onDismiss: () -> Unit) {
    val buttonHeight: Dp = 48.dp

    Dialog(onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 6.dp
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                // Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.emergency),
                        contentDescription = "Emergency Contacts (Star)"
                    )

                    Text(
                        "Emergency Contacts",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Description
                Text(
                    "To initiate a call to an emergency contact, simply click the button to start a call:",
                    fontSize = 14.sp
                )

                // Button Column
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 911
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
                            .height(buttonHeight)
                            .fillMaxWidth()
                    ) {
                        Text("Call 911", fontSize = 28.sp)
                    }

                    // IUPD
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
                            .height(buttonHeight)
                            .fillMaxWidth()
                    ) {
                        Text("Call IUPD", fontSize = 28.sp)
                    }

                    // Mom
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
                            .height(buttonHeight)
                            .fillMaxWidth()
                    ) {
                        Text("Call Mom", fontSize = 28.sp)
                    }

                    // Dad
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
                            .height(buttonHeight)
                            .fillMaxWidth()
                    ) {
                        Text("Call Dad", fontSize = 28.sp)
                    }

                    // Prof. James
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
                        Text("Call Prof. James", fontSize = 28.sp)
                    }
                }

                // Cancel Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilledTonalButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.White
                        ),
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                    ) { Text("CANCEL") }
                }
            }
        }
    }
}