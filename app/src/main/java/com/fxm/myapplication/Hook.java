package com.fxm.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {

    private static final int DEFAULT_DPI = 250; // 默认DPI值

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.ss.android.ugc.aweme")) {
            return;
        }

        XposedBridge.log("Hooking TikTok (Douyin)");

        XposedHelpers.findAndHookMethod("android.content.res.Resources", lpparam.classLoader, "getDisplayMetrics", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                DisplayMetrics displayMetrics = (DisplayMetrics) param.getResult();
                if (displayMetrics != null) {
                    displayMetrics.densityDpi = DEFAULT_DPI;
                    displayMetrics.density = DEFAULT_DPI / 160f;
                    displayMetrics.scaledDensity = DEFAULT_DPI / 160f;
                    XposedBridge.log("DPI has been set to: " + DEFAULT_DPI);
                }
            }
        });
    }
}
