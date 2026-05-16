
package com.example.a111.mycontrol;

import android.view.Surface;

/**
 * IPC跨进程通信接口
 * 定义与远程服务交互的方法
 */
interface IMutual {
    void setSurface(in Surface surface);
    String start(int ScreenX, int ScreenY);
    void MotionEventClick(int Event_getAction, float PosX, float PosY);
    float[] GetImGuiwinsize();
    void setPid(int pid);
    void setKey(String key);
    void setUUid(String UUID);
}