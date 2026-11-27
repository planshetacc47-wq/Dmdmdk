#!/usr/bin/env bash
set -euo pipefail
PKG_PATH="android/app/src/main/java/com/vadim/duraktracker"
mkdir -p "$PKG_PATH"
cp native/FloatingOverlayPlugin.java "$PKG_PATH/"
cp native/FloatingOverlayService.java "$PKG_PATH/"
cat > "$PKG_PATH/MainActivity.java" <<'JAVA'
package com.vadim.duraktracker;
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
public class MainActivity extends BridgeActivity {
 @Override public void onCreate(Bundle savedInstanceState) { registerPlugin(FloatingOverlayPlugin.class); super.onCreate(savedInstanceState); }
}
JAVA
python3 - <<'PY'
from pathlib import Path
p=Path('android/app/src/main/AndroidManifest.xml')
s=p.read_text()
if 'android.permission.SYSTEM_ALERT_WINDOW' not in s:
 s=s.replace('<application','<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />\n    <application',1)
if 'FloatingOverlayService' not in s:
 s=s.replace('</application>','<service android:name="com.vadim.duraktracker.FloatingOverlayService" android:exported="false" />\n    </application>')
p.write_text(s)
PY
