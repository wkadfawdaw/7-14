#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

SCHEME="FundValueApp"
CONFIGURATION="Release"
ARCHIVE_PATH="$ROOT_DIR/build/FundValueApp.xcarchive"
EXPORT_DIR="$ROOT_DIR/build/export"
PROJECT_PATH="$ROOT_DIR/FundValueApp.xcodeproj"
DEFAULT_EXPORT_PLIST="$ROOT_DIR/packaging/ExportOptions-AdHoc.plist"

EXPORT_PLIST="${1:-$DEFAULT_EXPORT_PLIST}"

if [[ "$(uname -s)" != "Darwin" ]]; then
  echo "❌ 必须在 macOS 上执行（需要 Xcode/xcodebuild）。"
  exit 1
fi

if ! command -v xcodebuild >/dev/null 2>&1; then
  echo "❌ 未检测到 xcodebuild，请先安装 Xcode。"
  exit 1
fi

if ! command -v xcodegen >/dev/null 2>&1; then
  echo "❌ 未检测到 xcodegen，请先安装：brew install xcodegen"
  exit 1
fi

if [[ ! -f "$EXPORT_PLIST" ]]; then
  echo "❌ 导出配置不存在：$EXPORT_PLIST"
  exit 1
fi

echo "==> 1/4 生成 Xcode 工程"
xcodegen generate --spec packaging/project.yml

echo "==> 2/4 清理旧产物"
rm -rf "$ROOT_DIR/build"

echo "==> 3/4 归档应用"
xcodebuild \
  -project "$PROJECT_PATH" \
  -scheme "$SCHEME" \
  -configuration "$CONFIGURATION" \
  -destination "generic/platform=iOS" \
  -archivePath "$ARCHIVE_PATH" \
  archive

echo "==> 4/4 导出 IPA"
xcodebuild \
  -exportArchive \
  -archivePath "$ARCHIVE_PATH" \
  -exportPath "$EXPORT_DIR" \
  -exportOptionsPlist "$EXPORT_PLIST"

echo "✅ 打包完成，输出目录：$EXPORT_DIR"
ls -lh "$EXPORT_DIR"
