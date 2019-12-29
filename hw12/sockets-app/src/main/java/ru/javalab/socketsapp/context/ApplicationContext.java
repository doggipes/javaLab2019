package ru.javalab.socketsapp.context;

public interface ApplicationContext {
    <T> T getComponent(Class<T> componentType, String name);
}
