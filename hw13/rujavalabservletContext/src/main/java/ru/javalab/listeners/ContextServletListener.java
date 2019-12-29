package ru.javalab.listeners;

import ru.javalab.context.ApplicationContext;
import ru.javalab.context.ApplicationContextReflectionBased;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextServletListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("IN LISTENER");
        ServletContext servletContext = servletContextEvent.getServletContext();
        ApplicationContext context = new ApplicationContextReflectionBased();
        servletContext.setAttribute("context", context);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
