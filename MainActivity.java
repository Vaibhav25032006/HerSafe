package com.hersafe.app;

import android.os.Bundle;
import android.view.KeyEvent;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    public static volatile boolean volumeSos = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        registerPlugin(HerSafePlugin.class); // must be before super.onCreate
        super.onCreate(savedInstanceState);
    }

    /** hardware volume-UP while the app is open -> page event "volumeupbutton" */
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (volumeSos && e.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
            if (e.getAction() == KeyEvent.ACTION_DOWN && e.getRepeatCount() == 0 && getBridge() != null)
                getBridge().triggerWindowJSEvent("volumeupbutton");
            return true;
        }
        return super.dispatchKeyEvent(e);
    }
}
