package com.example.fundnav.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.fundnav.ui.viewmodel.FundViewModel

@Composable
fun FundSearchScreen(viewModel: FundViewModel) {
    val uiState by viewModel.state.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "基金实时估值", style = MaterialTheme.typography.headlineSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = uiState.keyword,
                onValueChange = viewModel::updateKeyword,
                label = { Text("基金代码/基金名") }
            )
            Button(onClick = viewModel::search) {
                Text("搜索")
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(uiState.searchResult) { fund ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.loadFundDetail(fund) }
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${fund.name} (${fund.code})")
                        Text(fund.type, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
