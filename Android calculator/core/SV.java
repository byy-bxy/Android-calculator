package com.example.a111.mycontrol;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// 使用你项目中的 IPCService 和 IMutual
import com.example.a111.mycontrol.IPCService;
import com.example.a111.mycontrol.IMutual;

/**
 * 自定义 SurfaceView，用于显示远程服务渲染的内容
 * 通过 IPC 与服务通信，接收并显示图形数据
 */
public class SV extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = "SV"; // 日志标签
    private IMutual ipcService; // IPC 服务实例

    public SV(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setZOrderOnTop(true); // 置于顶层（可根据需求调整）
        getHolder().setFormat(PixelFormat.TRANSPARENT); // 设置透明背景
        getHolder().addCallback(this); // 添加 Surface 状态回调
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created");
        // 设置 Surface 类型（在 API 31+ 已弃用，可移除）
        // holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);

        // 获取 IPC 服务实例并设置 Surface
        ipcService = IPCService.getIPC();
        if (ipcService != null) {
            try {
                ipcService.setSurface(holder.getSurface());
                Log.d(TAG, "Surface 设置成功");
            } catch (RemoteException e) {
                Log.e(TAG, "IPC 调用失败: setSurface", e);
            }
        } else {
            Log.e(TAG, "IPC 服务未连接");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "Surface 尺寸变化: " + width + "x" + height);

        // 通知服务 Surface 尺寸变化
        if (ipcService != null) {
            try {
                ipcService.start(width, height);
                Log.d(TAG, "服务已启动，尺寸: " + width + "x" + height);
            } catch (RemoteException e) {
                Log.e(TAG, "IPC 调用失败: start", e);
            }
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface 销毁");
        // 释放资源（根据需求添加）
        if (ipcService != null) {
            try {
                // 假设 IMutual 有 stop 方法（如果没有，可忽略）
                // ipcService.stop();
            } catch (Exception e) {
                Log.e(TAG, "释放资源失败", e);
            }
        }
    }
}