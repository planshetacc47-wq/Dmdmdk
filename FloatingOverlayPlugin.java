package com.vadim.duraktracker;
import android.content.Intent;import android.net.Uri;import android.os.Build;import android.provider.Settings;
import com.getcapacitor.Plugin;import com.getcapacitor.PluginCall;import com.getcapacitor.PluginMethod;import com.getcapacitor.annotation.CapacitorPlugin;import com.getcapacitor.JSObject;
@CapacitorPlugin(name="FloatingOverlay")
public class FloatingOverlayPlugin extends Plugin {
 private static boolean overlayStarted = false;
 @PluginMethod public void start(PluginCall call){if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M&&!Settings.canDrawOverlays(getContext())){Intent i=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:"+getContext().getPackageName()));getActivity().startActivity(i);JSObject r=new JSObject();r.put("permissionRequired",true);call.resolve(r);return;}getContext().startService(new Intent(getContext(),FloatingOverlayService.class));overlayStarted=true;JSObject r=new JSObject();r.put("permissionRequired",false);call.resolve(r);}
 @PluginMethod public void update(PluginCall call){Intent i=new Intent(getContext(),FloatingOverlayService.class);i.setAction(FloatingOverlayService.ACTION_UPDATE);i.putExtra("mine",call.getInt("mine",0));i.putExtra("deck",call.getInt("deck",24));i.putExtra("enemy",call.getInt("enemy",0));i.putExtra("out",call.getInt("out",0));if(overlayStarted)getContext().startService(i);call.resolve();}
 @PluginMethod public void stop(PluginCall call){getContext().stopService(new Intent(getContext(),FloatingOverlayService.class));overlayStarted=false;call.resolve();}
}
