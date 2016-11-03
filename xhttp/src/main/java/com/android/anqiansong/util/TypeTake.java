package com.android.anqiansong.util;

import com.android.anqiansong.callback.Callback;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * 主题:获取泛型<br>
 * XHttp作者博客:<a href="http://blog.csdn.net/qq243223991">安前松博客</a></br>
 */
public class TypeTake {

    public static Type getType(Class<?> subclass, Class<?> interfClass) {
        Type[] types = subclass.getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                if (interfClass.isAssignableFrom(getRawType(type))) {
                    ParameterizedType parameterized = (ParameterizedType) type;
                    return parameterized.getActualTypeArguments()[0];
                }
            }
        }
        return null;
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) {
                throw new IllegalArgumentException();
            }
            return (Class<?>) rawType;
        } else if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        } else if (type instanceof TypeVariable) {
            return Object.class;
        } else if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType, or " + "GenericArrayType, but <" + type
                    + "> is of type " + className);
        }
    }

    public static Class getClass(Callback callback) {
        ParameterizedType parameterizedType = (ParameterizedType) callback.getClass().getGenericSuperclass();//获取当前new对象的泛型的父类类型
        Class clazz = (Class) parameterizedType.getActualTypeArguments()[0];
        return clazz;
    }
}
