package com.example.a111.mycontrol;

import android.annotation.NonNull;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.view.Surface;

// 导入当前项目的 IMutual（编译后自动生成）
import com.example.a111.mycontrol.IMutual;
// 导入当前项目的 SuperJNI
import com.example.a111.mycontrol.SuperJNI;
// 导入 libsu 库的 RootService
import com.topjohnwu.superuser.ipc.RootService;

public class SuperMain extends RootService {
    static {
        if (Process.myUid() == 0) {
            System.loadLibrary("main");
        }
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return new IMutual.Stub() {
            @Override
            public void setSurface(Surface surface) throws RemoteException {
                SuperJNI.setSurface(surface);
            }

            @Override
            public String start(int ScreenX, int ScreenY) throws RemoteException {
                return SuperJNI.start(ScreenX, ScreenY);
            }

            @Override
            public void MotionEventClick(int Event_getAction, float PosX, float PosY) throws RemoteException {
                SuperJNI.MotionEventClick(Event_getAction, PosX, PosY);
            }

            @Override
            public float[] GetImGuiwinsize() throws RemoteException {
                return SuperJNI.GetImGuiwinsize();
            }

            @Override
            public void setPid(int pid) throws RemoteException {
                SuperJNI.setPid(pid);
            }

            @Override
            public void setKey(String key) throws RemoteException {
                SuperJNI.setKey(key);
            }

            @Override
            public void setUUid(String UUID) throws RemoteException {
                SuperJNI.setUUid(UUID);
            }
        };
    }
}