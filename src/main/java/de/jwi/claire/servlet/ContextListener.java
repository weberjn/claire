package de.jwi.claire.servlet;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public final class ContextListener
    implements ServletContextListener {

    private ServletContext context = null;

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        log("contextDestroyed()");
        this.context = null;

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        this.context = event.getServletContext();
        log("contextInitialized()");

    }

    private void log(String message) {

        if (context != null) {
            context.log("ContextListener: " + message);
        } else {
            System.out.println("ContextListener: " + message);
        }

    }

}
