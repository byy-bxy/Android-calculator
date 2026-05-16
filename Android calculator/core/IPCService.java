package com.example.a111.mycontrol;

// 修正导入路径，指向你的项目中的 Imutual 接口

/**
 * IPC 通信服务管理类
 * 用于管理 Imutual 接口实例，提供全局访问点
 */
public class IPCService {
    private static volatile IMutual sIpc; // 使用你项目中的 Imutual 接口

    /**
     * 检查 IPC 连接是否已建立
     * @return 连接状态（true：已连接）
     */
    public static boolean isConnected() {
        return sIpc != null;
    }

    /**
     * 初始化 IPC 连接
     * @param ipc Imutual 接口实例（从远程服务获取）
     */
    public static void initIPC(IMutual ipc) {
        if (sIpc == null) {
            sIpc = ipc;
        }
    }

    /**
     * 获取 IPC 连接实例
     * @return Imutual 接口实例（可能为 null，使用前需判空）
     */
    public static IMutual getIPC() {
        return sIpc;
    }

    /**
     * 释放 IPC 连接
     */
    public static void releaseIPC() {
        sIpc = null;
    }
}