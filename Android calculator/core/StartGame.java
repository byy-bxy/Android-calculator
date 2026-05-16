package com.example.a111.mycontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

// 悬浮窗管理类：负责创建、更新、移除悬浮窗
public class StartGame {
    private static WindowManager windowManager; // 窗口管理器
    private static View touchView; // 触摸交互视图
    private static TV myTV; // 自定义TV视图（需确保项目中有此类）
    private static WindowManager.LayoutParams tvParams; // TV视图参数
    private static WindowManager.LayoutParams touchParams; // 触摸视图参数
    private static Handler mainHandler; // 主线程Handler（避免内存泄漏）


    /**
     * 显示悬浮窗
     * @param activity 上下文（需确保已申请悬浮窗权限）
     */
    public static void showFloatWindow(final Activity activity) {
        // 检查权限（Android O及以上需要悬浮窗权限）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasOverlayPermission(activity)) {
            Toast.makeText(activity, "请先授予悬浮窗权限", Toast.LENGTH_SHORT).show();
            return;
        }

        // 初始化主线程Handler
        mainHandler = new Handler(Looper.getMainLooper());

        // 获取WindowManager
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);

        // 初始化TV视图参数
        tvParams = new WindowManager.LayoutParams();
        // 设置窗口类型（悬浮窗必须）
        tvParams.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        // 全屏覆盖（不接受触摸）
        tvParams.gravity = Gravity.TOP | Gravity.LEFT;
        tvParams.format = PixelFormat.TRANSPARENT; // 透明背景
        tvParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        tvParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        // 窗口标志（不接受触摸、不获取焦点，仅作为覆盖层）
        tvParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        // 适配刘海屏（覆盖刘海区域）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            tvParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // 初始化触摸交互视图参数（可拖动/接收触摸）
        touchParams = new WindowManager.LayoutParams();
        touchParams.type = tvParams.type; // 同TV视图的窗口类型
        touchParams.gravity = Gravity.TOP | Gravity.LEFT;
        touchParams.format = PixelFormat.TRANSPARENT;
        touchParams.width = 770; // 初始宽度
        touchParams.height = 780; // 初始高度
        touchParams.x = 60; // 初始X坐标
        touchParams.y = 60; // 初始Y坐标
        // 触摸视图标志（接受触摸、不阻塞其他视图触摸）
        touchParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        // 适配刘海屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            touchParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        // 初始化自定义TV视图（确保项目中有此类）
        myTV = new TV(activity);
        // 初始化触摸视图（透明，仅用于接收触摸事件）
        touchView = new View(activity);
        touchView.setOnTouchListener((v, event) -> {
            // 转发触摸事件到项目中的处理类
            try {
                if (MainActivity.app_Operation_mode_root) {
                    IPCService.GetIPC().MotionEventClick(event.getAction(), event.getRawX(), event.getRawY());
                } else {
                    SuperJNI.MotionEventClick(event.getAction(), event.getRawX(), event.getRawY());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
                Toast.makeText(activity, "触摸事件转发失败", Toast.LENGTH_SHORT).show();
            }
            return true; // 消费触摸事件
        });

        // 添加视图到窗口管理器
        try {
            windowManager.addView(myTV, tvParams);
            windowManager.addView(touchView, touchParams);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "悬浮窗创建失败", Toast.LENGTH_SHORT).show();
            return;
        }

        // 根据运行模式更新触摸窗口大小
        if (MainActivity.app_Operation_mode_root) {
            updateTouchWinSize();
        } else {
            updateTouchWinSize_not_root();
        }
    }


    /**
     * 根模式下更新触摸窗口大小
     */
    private static void updateTouchWinSize() {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (windowManager == null || touchView == null) return;

                try {
                    // 从IPC服务获取窗口大小
                    float[] rect = IPCService.GetIPC().GetImGuiwinsize();
                    touchParams.x = (int) rect[0];
                    touchParams.y = (int) rect[1];
                    touchParams.width = (int) rect[2];
                    touchParams.height = (int) rect[3];
                    // 更新触摸窗口布局
                    windowManager.updateViewLayout(touchView, touchParams);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                // 循环更新（17ms约等于60fps）
                mainHandler.postDelayed(this, 17);
            }
        }, 17);
    }


    /**
     * 非根模式下更新触摸窗口大小
     */
    private static void updateTouchWinSize_not_root() {
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (windowManager == null || touchView == null) return;

                // 从JNI获取窗口大小
                float[] rect = SuperJNI.GetImGuiwinsize();
                touchParams.x = (int) rect[0];
                touchParams.y = (int) rect[1];
                touchParams.width = (int) rect[2];
                touchParams.height = (int) rect[3];
                // 更新触摸窗口布局
                windowManager.updateViewLayout(touchView, touchParams);

                // 循环更新
                mainHandler.postDelayed(this, 17);
            }
        }, 17);
    }


    /**
     * 移除悬浮窗（必须调用，避免内存泄漏）
     */
    public static void removeFloatWindow() {
        if (windowManager != null) {
            if (myTV != null) {
                windowManager.removeViewImmediate(myTV);
                myTV = null;
            }
            if (touchView != null) {
                windowManager.removeViewImmediate(touchView);
                touchView = null;
            }
            windowManager = null;
        }
        // 移除Handler回调
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
            mainHandler = null;
        }
    }


    /**
     * 检查是否有悬浮窗权限
     */
    private static boolean hasOverlayPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true; // 6.0以下默认有权限
    }


    /**
     * 申请悬浮窗权限（在Activity中调用）
     */
    public static void requestOverlayPermission(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasOverlayPermission(activity)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, requestCode);
        }
    }
}