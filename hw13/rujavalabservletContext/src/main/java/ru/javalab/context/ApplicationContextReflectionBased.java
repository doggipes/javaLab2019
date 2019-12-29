package ru.javalab.context;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class ApplicationContextReflectionBased implements ApplicationContext {
    private String componentName;
    private HashMap<String, Object> components;

    public ApplicationContextReflectionBased(){
        components = new HashMap<>();
    }

    private Component find(Field field) {
        return (Component) components.entrySet().stream()
                .filter(c -> c.getValue().getClass().getName().equals(field.getType().toString().substring(6)))
                .findFirst().get().getValue();
    }

    public <T> void create(Class<T> component) {
        Field[] fields = component.getDeclaredFields();

        for (Field field : fields) {

            Class[] interfaces = field.getType().getInterfaces();

            for (Class aClass : interfaces) {

                if (aClass.getName().equals(componentName)) {
                    create(field.getType());
                }
            }
        }
        di(component);
    }

    private <T> void di(Class<T> component){
        try {
            Object o = component.getDeclaredConstructor().newInstance();
            Component comp = (Component) o;
            components.put(comp.getName(), o);

            Field[] fields = component.getDeclaredFields();
            for (Field field : fields) {

                Class[] interfaces = field.getType().getInterfaces();

                for (Class aClass : interfaces) {

                    if (aClass.getName().equals(componentName)) {
                        field.setAccessible(true);
                        field.set(o, find(field));
                    }
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    @Override
    public <T> T getComponent(Class<T> component, String name) {
        if(!components.containsKey(name)){
            componentName = Component.class.getName();
            create(component);
        }

        return (T) components.get(name);
    }
}
