package com.shelly.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.shelly.entity.pojo.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class TaskInvokeUtils {
    private TaskInvokeUtils() {
    }

    /**
     * 反射调用任务目标方法
     *
     * @param task 系统任务对象，其中包含 invokeTarget，例如 "timedTask.clear"
     * 补充：如果是个有参方法，则需要用括号以及逗号分隔参数，例如 "timedTask.clear(1,2)"
     */
    public static void invokeMethod(Task task) throws Exception {
        // 1. 从任务中获取需要执行的目标方法，例如 "timedTask.clear"
        String invokeTarget = task.getInvokeTarget();
        // 2. 获取 Bean 名称（类名或 Spring Bean 名），如 "timedTask"
        String beanName = getBeanName(invokeTarget);
        // 3. 获取方法名，如 "clear"
        String methodName = getMethodName(invokeTarget);
        // 4. 获取方法参数列表（已转换成值+类型），如果没有参数，即没有（），返回 null
        List<Object[]> methodParams = getMethodParams(invokeTarget);
        // 5. 判断 beanName 是否是类全名（如 com.xxx.MyClass），即是否是“类路径”
        if (!isValidClassName(beanName)) {
            // 5.1 如果不是类路径，就认为是 Spring Bean 名
            //     通过 Spring 工具类获取对应的 Bean 实例
            Object bean = SpringUtil.getBean(beanName);
            // 5.2 使用反射调用目标方法
            invokeMethod(bean, methodName, methodParams);
        } else {
            // 5.3 如果是类路径（如 com.xxx.MyClass），通过反射直接实例化对象
            Object bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
            // 5.4 再调用该实例的方法
            invokeMethod(bean, methodName, methodParams);
        }
    }


    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (methodParams != null && !methodParams.isEmpty()) {
            Method method = bean.getClass().getDeclaredMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(bean, getMethodParamsValue(methodParams));
        } else {
            Method method = bean.getClass().getDeclaredMethod(methodName);
            method.invoke(bean);
        }
    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 调用目标
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
  /*      判断是否为一个类名（即是否带有至少两个 .）
        用于区分是 Spring Bean 还是直接的类全名*/
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     *
     * @param invokeTarget 调用目标
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 调用目标
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取method方法参数相关列表
     * 将字符串表达式中的参数解析成 Java 类型和值
     * @param invokeTarget 调用目标
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        List<Object[]> clazz = new LinkedList<>();
        for (String methodParam : methodParams) {
            String str = StringUtils.trimToEmpty(methodParam);
            // String字符串类型，以'或"开头
            if (StringUtils.startsWithAny(str, "'", "\"")) {
                clazz.add(new Object[]{StringUtils.substring(str, 1, str.length() - 1), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                clazz.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，以L结尾
            else if (StringUtils.endsWith(str, "L")) {
                clazz.add(new Object[]{Long.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Long.class});
            }
            // double浮点类型，以D结尾
            else if (StringUtils.endsWith(str, "D")) {
                clazz.add(new Object[]{Double.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Double.class});
            }
            // 其他类型归类为整形
            else {
                clazz.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return clazz;
    }

    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] clazz = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            clazz[index] = (Class<?>) os[1];
            index++;
        }
        return clazz;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] clazz = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            clazz[index] = os[0];
            index++;
        }
        return clazz;
    }
}
