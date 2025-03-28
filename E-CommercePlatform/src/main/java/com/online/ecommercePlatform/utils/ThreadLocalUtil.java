package com.online.ecommercePlatform.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal 工具类
 * 提供线程本地变量的存储和访问功能，用于在多线程环境下保存线程私有数据
 * 使用示例：
 * // 存储数据
 * ThreadLocalUtil.set(userInfo);
 *
 * // 获取数据
 * UserInfo user = ThreadLocalUtil.get();
 *
 * // 使用后清理
 * ThreadLocalUtil.remove();
 *
 */
@SuppressWarnings("all")
public class ThreadLocalUtil {

    /**
     * ThreadLocal实例，用于存储线程本地变量
     * 使用static final修饰确保全局唯一实例
     */
    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    /**
     * 获取当前线程中存储的值
     *
     * @param <T> 返回值的泛型类型
     * @return 当前线程中存储的值，如果没有则返回null
     *
     * @apiNote 需要进行强制类型转换，确保类型安全
     */
    public static <T> T get() {
        return (T) THREAD_LOCAL.get();
    }

    /**
     * 在当前线程中存储值
     *
     * @param value 要存储的值，可以是任意类型
     *
     * @throws NullPointerException 如果value为null
     * @warning 存储大对象可能导致内存泄漏，务必在使用后调用remove()清理
     */
    public static void set(Object value) {
        if (value == null) {
            throw new NullPointerException("ThreadLocal value cannot be null");
        }
        THREAD_LOCAL.set(value);
    }

    /**
     * 清除当前线程中存储的值
     *重要：必须在使用完毕后调用此方法，特别是在线程池环境下，
     * 防止内存泄漏和旧数据污染问题
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}