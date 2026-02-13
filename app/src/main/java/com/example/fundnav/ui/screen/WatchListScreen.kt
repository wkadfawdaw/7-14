package com.example.fundnav.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fundnav.ui.viewmodel.FundViewModel

@Composable
fun WatchListScreen(viewModel: FundViewModel) {
    val uiState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("其他功能（参考养基宝）", style = MaterialTheme.typography.headlineSmall)
        Text("提供自选基金、涨跌提醒、当日估值快照")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.watchList) { item ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("${item.name} (${item.code})", fontWeight = FontWeight.Bold)
                            val color = if (item.estimatedGrowth >= 0) Color(0xFFD32F2F) else Color(0xFF388E3C)
                            Text("实时估值：${item.estimatedGrowth}%", color = color)
                        }
                        Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                            Text("提醒")
                            Switch(checked = item.reminderEnabled, onCheckedChange = {})
                        }
                    }
                }
            }
        }
    }
}
