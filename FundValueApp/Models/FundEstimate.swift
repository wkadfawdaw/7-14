import Foundation

struct FundEstimate: Identifiable, Decodable {
    let fundCode: String
    let fundName: String
    let netValueDate: String
    let lastNetValue: String
    let estimatedNetValue: String
    let estimatedGrowthRate: String
    let estimateTime: String

    var id: String { fundCode }

    enum CodingKeys: String, CodingKey {
        case fundCode = "fundcode"
        case fundName = "name"
        case netValueDate = "jzrq"
        case lastNetValue = "dwjz"
        case estimatedNetValue = "gsz"
        case estimatedGrowthRate = "gszzl"
        case estimateTime = "gztime"
    }

    var growthRateDouble: Double {
        Double(estimatedGrowthRate) ?? 0
    }
}

struct FundEstimateResponse: Decodable {
    let data: FundEstimate
}
