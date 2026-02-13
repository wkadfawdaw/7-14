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
fun FundDetailScreen(viewModel: FundViewModel) {
    val uiState by viewModel.state.collectAsState()
    val selectedFund = uiState.selectedFund

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "持仓与历史净值", style = MaterialTheme.typography.headlineSmall)

        if (selectedFund == null) {
            Text("请先在“搜基金”页选择基金")
            return
        }

        Text("当前基金：${selectedFund.name} (${selectedFund.code})", fontWeight = FontWeight.Bold)

        Text("前十大持仓")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(uiState.holdings) { holding ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(holding.stockName)
                        Text("占比 ${holding.ratio}%")
                        val changeColor = if (holding.change >= 0) Color(0xFFD32F2F) else Color(0xFF388E3C)
                        Text("${holding.change}%", color = changeColor)
                    }
                }
            }
        }

        Text("历史净值", fontWeight = FontWeight.Bold)
        uiState.navHistory.forEach { point ->
            val growthColor = if (point.growthRate >= 0) Color(0xFFD32F2F) else Color(0xFF388E3C)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(point.date)
                Text(point.nav.toString())
                Text("${point.growthRate}%", color = growthColor)
            }
        }
    }
}
