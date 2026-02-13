import Foundation

enum FundAPIError: Error, LocalizedError {
    case invalidURL
    case invalidResponse
    case decodeFailed

    var errorDescription: String? {
        switch self {
        case .invalidURL: return "请求地址无效"
        case .invalidResponse: return "基金接口返回异常"
        case .decodeFailed: return "解析基金估值失败"
        }
    }
}

final class FundAPIService {
    static let shared = FundAPIService()

    private init() {}

    func fetchEstimate(fundCode: String) async throws -> FundEstimate {
        guard let url = URL(string: "https://fundgz.1234567.com.cn/js/\(fundCode).js") else {
            throw FundAPIError.invalidURL
        }

        let (data, response) = try await URLSession.shared.data(from: url)
        guard (response as? HTTPURLResponse)?.statusCode == 200 else {
            throw FundAPIError.invalidResponse
        }

        guard let text = String(data: data, encoding: .utf8),
              let jsonData = extractJSONPBody(text)?.data(using: .utf8)
        else {
            throw FundAPIError.decodeFailed
        }

        do {
            return try JSONDecoder().decode(FundEstimate.self, from: jsonData)
        } catch {
            throw FundAPIError.decodeFailed
        }
    }

    private func extractJSONPBody(_ raw: String) -> String? {
        guard let start = raw.firstIndex(of: "{"),
              let end = raw.lastIndex(of: "}")
        else {
            return nil
        }

        return String(raw[start...end])
    }
}
