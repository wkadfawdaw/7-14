import Foundation

@MainActor
final class FundListViewModel: ObservableObject {
    @Published var trackedCodes: [String] = ["161725", "260108", "110011"]
    @Published var estimates: [FundEstimate] = []
    @Published var inputCode: String = ""
    @Published var errorMessage: String?
    @Published var isLoading = false

    private var timer: Timer?

    func startAutoRefresh() {
        stopAutoRefresh()
        timer = Timer.scheduledTimer(withTimeInterval: 30, repeats: true) { [weak self] _ in
            Task { await self?.refreshAll() }
        }
    }

    func stopAutoRefresh() {
        timer?.invalidate()
        timer = nil
    }

    func addFundCode() {
        let code = inputCode.trimmingCharacters(in: .whitespacesAndNewlines)
        guard code.count == 6, Int(code) != nil else {
            errorMessage = "请输入 6 位基金代码"
            return
        }

        guard !trackedCodes.contains(code) else {
            errorMessage = "该基金已在列表中"
            return
        }

        trackedCodes.insert(code, at: 0)
        inputCode = ""
        Task { await refreshAll() }
    }

    func removeFund(at offsets: IndexSet) {
        trackedCodes.remove(atOffsets: offsets)
        estimates.removeAll { estimate in
            !trackedCodes.contains(estimate.fundCode)
        }
    }

    func refreshAll() async {
        isLoading = true
        errorMessage = nil
        defer { isLoading = false }

        var latest: [FundEstimate] = []
        for code in trackedCodes {
            do {
                let estimate = try await FundAPIService.shared.fetchEstimate(fundCode: code)
                latest.append(estimate)
            } catch {
                errorMessage = "部分基金更新失败，请稍后重试"
            }
        }

        estimates = trackedCodes.compactMap { code in
            latest.first(where: { $0.fundCode == code })
        }
    }
}
