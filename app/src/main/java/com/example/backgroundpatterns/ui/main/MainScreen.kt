package com.example.backgroundpatterns.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.backgroundpatterns.data.DefaultDataRepository
import com.example.backgroundpatterns.theme.BackgroundPatternsTheme

@Composable
fun MainScreen(
  onItemClick: (NavKey) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: MainScreenViewModel = viewModel { MainScreenViewModel(DefaultDataRepository()) },
) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()
  when (state) {
    MainScreenUiState.Loading -> {
      // Blank
    }
    is MainScreenUiState.Success -> {
      MainScreen(data = (state as MainScreenUiState.Success).data, modifier = modifier)
    }
    is MainScreenUiState.Error -> {
      Text("Error loading data: ${(state as MainScreenUiState.Error).throwable.message}")
    }
  }
}

@Composable
internal fun MainScreen(data: List<String>, modifier: Modifier = Modifier) {
  LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = modifier.fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
  ) {
      item(span = { GridItemSpan(2) }) {
          Text(
              "Compose Background Modifiers",
              style = MaterialTheme.typography.headlineMedium
          )
      }

      item {
          ExampleCard("Scalloped") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .scallopedPatternBackground(patternColor = Color.LightGray)
              )
          }
      }

      item {
          ExampleCard("Diagonal Stripes") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .diagonalStripedBackground(
                          stripes = listOf(
                              Color(0xFFFFB6C1) to 12.dp,
                              Color(0xFFFFC0CB) to 4.dp
                          )
                      )
              )
          }
      }

      item {
          ExampleCard("Hex Polka Dots") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .staggeredPolkaDotBackground(dotColor = Color.Gray, dotRadius = 4.dp, spacing = 20.dp)
              )
          }
      }

      item {
          ExampleCard("Blueprint Grid") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .background(Color(0xFF1E3A8A))
                      .blueprintBackground(gridColor = Color(0x66FFFFFF), cellSize = 24.dp)
              )
          }
      }

      item {
          ExampleCard("Wave Header") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .waveHeaderBackground(color = Color(0xFF10B981))
              )
          }
      }

      item {
          ExampleCard("Checkerboard") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .checkerboardBackground(color1 = Color(0xFFF3F4F6), color2 = Color(0xFFD1D5DB), cellSize = 20.dp)
              )
          }
      }

      item {
          ExampleCard("Ripples") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .rippleBackground(ringColor = Color(0xFF3B82F6), ringSpacing = 20.dp, strokeWidth = 1.dp)
              )
          }
      }

      item {
          ExampleCard("Crosshatch") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .crosshatchBackground(lineColor = Color.LightGray, lineSpacing = 12.dp)
              )
          }
      }

      item {
          ExampleCard("Halftone Dots") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .halftoneBackground(dotColor = Color(0xFF8B5CF6), gridSize = 16.dp)
              )
          }
      }

      item {
          ExampleCard("Sawtooth Header") {
              Box(
                  modifier = Modifier
                      .fillMaxWidth()
                      .height(100.dp)
                      .sawtoothHeaderBackground(color = Color(0xFFF59E0B), toothSize = 12.dp)
              )
          }
      }
  }
}

@Composable
fun ExampleCard(title: String, content: @Composable () -> Unit) {
  Card(
      modifier = Modifier.fillMaxWidth(),
      elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
      Column {
          content()
          Box(
              modifier = Modifier
                  .fillMaxWidth()
                  .background(Color.White)
                  .padding(8.dp)
          ) {
              Text(
                  text = title,
                  style = MaterialTheme.typography.titleSmall
              )
          }
      }
  }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  BackgroundPatternsTheme { MainScreen(listOf("Android")) }
}

@Preview(showBackground = true, widthDp = 340)
@Composable
fun MainScreenPortraitPreview() {
  BackgroundPatternsTheme { MainScreen(listOf("Android")) }
}
