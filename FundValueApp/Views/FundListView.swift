import SwiftUI

struct FundListView: View {
    @StateObject private var viewModel = FundListViewModel()

    var body: some View {
        NavigationStack {
            VStack(spacing: 12) {
                inputBar

                if let errorMessage = viewModel.errorMessage {
                    Text(errorMessage)
                        .font(.footnote)
                        .foregroundStyle(.red)
                }

                List {
                    ForEach(viewModel.estimates) { estimate in
                        FundEstimateRow(estimate: estimate)
                    }
                    .onDelete(perform: viewModel.removeFund)
                }
                .listStyle(.plain)
            }
            .padding(.horizontal)
            .navigationTitle("基金实时估值")
            .toolbar {
                Button {
                    Task { await viewModel.refreshAll() }
                } label: {
                    if viewModel.isLoading {
                        ProgressView()
                    } else {
                        Label("刷新", systemImage: "arrow.clockwise")
                    }
                }
            }
            .task {
                await viewModel.refreshAll()
                viewModel.startAutoRefresh()
            }
            .onDisappear {
                viewModel.stopAutoRefresh()
            }
        }
    }

    private var inputBar: some View {
        HStack(spacing: 8) {
            TextField("输入 6 位基金代码", text: $viewModel.inputCode)
                .textFieldStyle(.roundedBorder)
                .keyboardType(.numberPad)

            Button("添加") {
                viewModel.addFundCode()
            }
            .buttonStyle(.borderedProminent)
        }
    }
}

private struct FundEstimateRow: View {
    let estimate: FundEstimate

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            HStack {
                Text("\(estimate.fundName) (\(estimate.fundCode))")
                    .font(.headline)
                Spacer()
                Text(estimate.estimatedGrowthRate + "%")
                    .fontWeight(.semibold)
                    .foregroundStyle(estimate.growthRateDouble >= 0 ? .red : .green)
            }

            HStack {
                Text("昨日净值: \(estimate.lastNetValue)")
                Spacer()
                Text("估算净值: \(estimate.estimatedNetValue)")
            }
            .font(.subheadline)
            .foregroundStyle(.secondary)

            Text("更新时间: \(estimate.estimateTime)")
                .font(.caption)
                .foregroundStyle(.tertiary)
        }
        .padding(.vertical, 6)
    }
}

#Preview {
    FundListView()
}
