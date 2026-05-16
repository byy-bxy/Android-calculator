package com.example.a111.mycontrol;

import android.content.Context;
import android.graphics.Surface;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceTexture;
import android.view.TextureView;
import android.annotation.NonNull;

// 确保 MainActivity 包路径正确（根据实际位置调整）
import com.example.a111.MainActivity; // 如果 MainActivity 在 com.example.a111 包下

// 使用当前项目的 IPCService 和 IMutual
import com.example.a111.mycontrol.IPCService;
import com.example.a111.mycontrol.IMutual;

// 确保 SuperJNI 在当前包下
import com.example.a111.mycontrol.SuperJNI;


/**
 * 自定义 TextureView 用于显示视频/图像内容
 * 实现 SurfaceTextureListener 监听纹理表面状态
 */
public class TV extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "TV"; // 日志标签，便于调试
    private SurfaceTexture mSurfaceTexture; // 保存纹理表面引用，避免重复创建


    public TV(Context context) {
        super(context);
        this.setOpaque(true); // 不透明（根据需求调整，透明设为 false）
        setSurfaceTextureListener(this); // 设置纹理监听器
    }


    /**
     * 当 SurfaceTexture 可用时调用（初始化完成）
     */
    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        mSurfaceTexture = surface; // 保存纹理引用
        try {
            // 根据运行模式初始化服务
            if (MainActivity.app_Operation_mode_root) {
                // Root 模式：通过当前项目的 IPCService 获取服务
                IMutual ipc = IPCService.getIPC(); // 使用修正后的 getIPC() 方法（小写开头）
                if (ipc != null) {
                    ipc.start(width, height);
                    ipc.setSurface(new Surface(surface));
                    Log.d(TAG, "Root模式：IPC服务启动成功，宽高：" + width + "x" + height);
                } else {
                    Log.e(TAG, "Root模式：IPC服务未初始化（getIPC() 返回 null）");
                }
            } else {
                // 非 Root 模式：通过当前包的 SuperJNI 启动
                SuperJNI.start(width, height);
                SuperJNI.setSurface(new Surface(surface));
                Log.d(TAG, "非Root模式：JNI启动成功，宽高：" + width + "x" + height);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IPC服务调用失败：" + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "初始化失败：" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 当 SurfaceTexture 尺寸变化时调用
     */
    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
        Log.d(TAG, "纹理尺寸变化：" + width + "x" + height);
        // 可选：如果 IPC 服务支持尺寸更新，添加以下逻辑
        if (MainActivity.app_Operation_mode_root) {
            IMutual ipc = IPCService.getIPC();
            if (ipc != null) {
                try {
                    // 假设 IMutual 有 updateSize 方法（根据实际接口调整）
                    // ipc.updateSize(width, height);
                } catch (RemoteException e) {
                    Log.e(TAG, "更新尺寸失败：" + e.getMessage());
                }
            }
        }
    }


    /**
     * 当 SurfaceTexture 销毁时调用（释放资源）
     */
    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        Log.d(TAG, "纹理销毁，释放资源");
        try {
            // 停止服务并释放资源
            if (MainActivity.app_Operation_mode_root) {
                IMutual ipc = IPCService.getIPC();
                if (ipc != null) {
                    // 确保 IMutual 接口有 stop 方法（如果没有，注释此行）
                    ipc.stop();
                }
            } else {
                // 确保 SuperJNI 有 stop 方法（如果没有，注释此行）
                SuperJNI.stop();
            }
        } catch (RemoteException e) {
            Log.e(TAG, "IPC服务停止失败：" + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "资源释放失败：" + e.getMessage());
        }

        mSurfaceTexture = null; // 清空引用
        return true; // 允许系统回收纹理
    }


    /**
     * 当 SurfaceTexture 更新时调用（每帧刷新）
     */
    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {
        // 调试用，正式环境可注释
        // Log.d(TAG, "纹理更新");
    }


    /**
     * 手动更新纹理（可选，根据需求添加）
     */
    public void updateTexture() {
        if (mSurfaceTexture != null) {
            mSurfaceTexture.updateTexImage();
        }
    }
}