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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.iu.luddy.i403prototype.ui.theme.I403PrototypeTheme

class Directions : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Hardcoded Streets
        val directions: Map<String, Pair<String, String>> = mapOf(
            "Kirkwood Avenue" to ("Right" to "Danger Further Ahead"),
            "S Dunn Street" to ("Right" to ""),
            "Atwater Avenue" to ("Straight" to ""),
            "S Mitchell Street" to ("Left" to "Danger on N Eagleson Avenue"),
            "S Rose Avenue" to ("Left" to ""),
            "E Third Street" to ("Right" to ""),
            "S Union Street" to ("Left" to ""),
            "E Tenth Street" to ("Left" to ""),
            "N Sunrise Drive" to ("Left" to ""),
        )

        setContent {
            Scaffold(
                topBar = { TopBar(this, "Directions") { finish() } },
                modifier = Modifier.fillMaxSize(),
                bottomBar = { Actions(this) }
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
                        // Point A
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.current_location),
                                    contentDescription = "Current Location (Pin with person)",
                                    modifier = Modifier.size(48.dp)
                                )

                                Text("Current Location: ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

                            Text("Kirkwood Avenue", fontSize = 20.sp)
                        }

                        // Point B
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(R.drawable.destination_location),
                                    contentDescription = "Destination Location (Home with person)",
                                    modifier = Modifier.size(48.dp)
                                )

                                Text("Destination: ", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

                            Text("Teter Quadd", fontSize = 20.sp)
                        }
                    }

                    // Take all directions and stuff them into my `DirectionView()`s
                    LazyColumn() {
                        directions.forEach { (street, information) ->
                            item { DirectionView(street, information.first, information.second) }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact way to constantly display steps/direction to the user. Each step has a
 * `direction` (left, right, straight) the street in which this direction change takes
 * place, and an optional `danger`.
 *
 * @param street the name of the street which to turn onto
 * @param direction the direction from your current position to turn onto the `street`
 * @param danger an optional text subtitle for the `street`
 */
@Composable
fun DirectionView(street: String, direction: String, danger: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(92.dp)
    ) {
        Icon(
            painter = painterResource(
                when (direction) {
                    "Left" -> R.drawable.left
                    "Right" -> R.drawable.right
                    else -> R.drawable.straight
                }
            ),
            contentDescription = if (direction == "Left") "Left" else "Right",
        )

        Column() {
            Text(street.uppercase(), fontSize = 28.sp)

            if (danger.isNotBlank()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.danger),
                        contentDescription = "Dangerous (Octagon with an X)",
                        tint = Color.Red
                    )

                    Text(danger, fontSize = 14.sp, color = Color.Red)
                }
            }
        }
    }
}