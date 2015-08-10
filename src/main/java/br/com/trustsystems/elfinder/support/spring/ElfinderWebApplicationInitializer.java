package br.com.trustsystems.elfinder.support.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ElfinderWebApplicationInitializer implements WebApplicationInitializer {

    public static final String SERVLET_NAME = "elfinder";

    @Override
    public final void onStartup(ServletContext servletContext) throws ServletException {

        AnnotationConfigWebApplicationContext rootWebApplicationContext = new AnnotationConfigWebApplicationContext();
        rootWebApplicationContext.setDisplayName("Elfinder Java Connector");
        rootWebApplicationContext.setServletContext(servletContext);
        rootWebApplicationContext.register(ElfinderRootConfig.class);
        rootWebApplicationContext.refresh();

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(SERVLET_NAME, new DispatcherServlet(rootWebApplicationContext));
        dispatcher.addMapping("/" + SERVLET_NAME + "/*");
    }

}
