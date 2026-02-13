# 基金实时估值 iOS 应用（SwiftUI）

这是一个可打包到 iPhone 的 SwiftUI 应用示例，实现：

- 添加/删除基金代码（6 位）
- 拉取基金实时估值（东财估值接口）
- 手动刷新 + 30 秒自动刷新
- 涨跌幅颜色展示

## 目录结构

- `FundValueApp/App/FundValueApp.swift`：应用入口
- `FundValueApp/Views/FundListView.swift`：主页面与列表行 UI
- `FundValueApp/ViewModels/FundListViewModel.swift`：状态管理与轮询刷新
- `FundValueApp/Services/FundAPIService.swift`：基金估值接口请求与 JSONP 解析
- `FundValueApp/Models/FundEstimate.swift`：基金估值模型
- `packaging/project.yml`：XcodeGen 工程配置（用于自动生成 `.xcodeproj`）
- `scripts/package_ios.sh`：一键归档并导出 IPA

---

## 一键打包（在 Mac 上）

> 说明：当前运行环境不是 macOS，无法直接产出可安装 IPA。你可以把本仓库拉到 Mac 上执行下面命令。

### 1) 安装依赖

```bash
brew install xcodegen
```

并确保你已经安装 Xcode，且可用：

```bash
xcodebuild -version
```

### 2) 打包 Ad Hoc（用于设备分发）

```bash
./scripts/package_ios.sh
```

导出后的 IPA 在：

- `build/export/*.ipa`

### 3) 打包 App Store/TestFlight

```bash
./scripts/package_ios.sh packaging/ExportOptions-AppStore.plist
```

---

## 安装到手机（常见方式）

### 方式 A：Xcode 直装（开发调试最快）

1. 用数据线连接 iPhone 到 Mac。
2. 打开生成的 `FundValueApp.xcodeproj`。
3. 在 Xcode 的 **Signing & Capabilities** 中选择你的 Team。
4. 选择你的设备，点击 Run，直接安装。

### 方式 B：Ad Hoc IPA 安装

1. Apple Developer 后台创建 Ad Hoc Profile，并包含目标设备 UDID。
2. 使用 `ExportOptions-AdHoc.plist` 导出 IPA。
3. 用 Apple Configurator 2 / MDM / 企业分发平台安装 IPA。

### 方式 C：TestFlight 分发

1. 使用 `ExportOptions-AppStore.plist` 导出。
2. 通过 Xcode Organizer 或 Transporter 上传到 App Store Connect。
3. 在 TestFlight 邀请测试人员安装。

---

## 接口说明

当前使用：

- `https://fundgz.1234567.com.cn/js/{fundCode}.js`

返回为 JSONP，已在 `FundAPIService` 中解析。

> 注意：基金估值属于第三方数据，可能存在延迟或接口变更。
