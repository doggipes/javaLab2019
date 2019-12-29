package ru.javalab.context;

public interface ApplicationContext {
    <T> T getComponent(Class<T> componentType, String name);
}
