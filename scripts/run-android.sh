#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
export ANDROID_HOME="${ANDROID_HOME:-$HOME/Library/Android/sdk}"
export PATH="$PATH:$ANDROID_HOME/platform-tools"

if ! adb get-state >/dev/null 2>&1; then
  echo "Запустите эмулятор: emulator -avd <AVD>"
  exit 1
fi

cd "$ROOT/code"
chmod +x gradlew
./gradlew installDebug
adb shell monkey -p stanulpych.pmvs.labtracker -c android.intent.category.LAUNCHER 1
echo "PmvsLabTracker запущен"
