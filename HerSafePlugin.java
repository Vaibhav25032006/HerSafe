package com.hersafe.app;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;
import java.util.ArrayList;

/** HerSafe native plugin: real permissions, direct SMS, direct call, volume-key switch. */
@CapacitorPlugin(
    name = "HerSafe",
    permissions = {
        @Permission(alias = "location", strings = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION }),
        @Permission(alias = "sms", strings = { Manifest.permission.SEND_SMS }),
        @Permission(alias = "call", strings = { Manifest.permission.CALL_PHONE })
    }
)
public class HerSafePlugin extends Plugin {

    private JSObject states() {
        JSObject o = new JSObject();
        for (String a : new String[] { "location", "sms", "call" })
            o.put(a, getPermissionState(a) == PermissionState.GRANTED ? "granted" : "denied");
        return o;
    }

    @PluginMethod
    public void check(PluginCall call) { call.resolve(states()); }

    @PluginMethod
    public void request(PluginCall call) {
        String alias = call.getString("alias", "location");
        if (getPermissionState(alias) == PermissionState.GRANTED) {
            JSObject o = new JSObject(); o.put("state", "granted"); call.resolve(o); return;
        }
        requestPermissionForAlias(alias, call, "permResult");
    }

    @PermissionCallback
    private void permResult(PluginCall call) {
        String alias = call.getString("alias", "location");
        JSObject o = new JSObject();
        o.put("state", getPermissionState(alias) == PermissionState.GRANTED ? "granted" : "denied");
        call.resolve(o);
    }

    /** sends a real SMS to every number; rejects if SEND_SMS is not granted */
    @PluginMethod
    public void sendSms(PluginCall call) {
        if (getPermissionState("sms") != PermissionState.GRANTED) { call.reject("no-permission"); return; }
        String nums = call.getString("numbers", ""), msg = call.getString("message", "");
        SmsManager sm = Build.VERSION.SDK_INT >= 31 ? getContext().getSystemService(SmsManager.class) : SmsManager.getDefault();
        ArrayList<String> parts = sm.divideMessage(msg);
        int sent = 0;
        for (String n : nums.split(",")) {
            n = n.trim();
            if (n.isEmpty()) continue;
            try { sm.sendMultipartTextMessage(n, null, parts, null, null); sent++; } catch (Exception e) { }
        }
        JSObject o = new JSObject(); o.put("sent", sent); call.resolve(o);
    }

    /** direct call when CALL_PHONE is granted, otherwise opens the dialer */
    @PluginMethod
    public void call(PluginCall call) {
        final String num = call.getString("number", "");
        final boolean direct = getPermissionState("call") == PermissionState.GRANTED;
        getActivity().runOnUiThread(() -> {
            try {
                getActivity().startActivity(new Intent(direct ? Intent.ACTION_CALL : Intent.ACTION_DIAL, Uri.fromParts("tel", num, null)));
                JSObject o = new JSObject(); o.put("direct", direct); call.resolve(o);
            } catch (Exception e) { call.reject("call-failed"); }
        });
    }

    @PluginMethod
    public void setVolume(PluginCall call) {
        MainActivity.volumeSos = call.getBoolean("on", true);
        call.resolve();
    }
}
