package kz.kaznu.web;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/3/16
 */

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class SpringWebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {

        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(appContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); // to handle all errors by GlobalErrorHanlder
        ServletRegistration.Dynamic dispatcher = container.addServlet(
                "SpringDispatcher", dispatcherServlet);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration charEncodingfilterReg = container.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        charEncodingfilterReg.setInitParameter("encoding", "UTF-8");
        charEncodingfilterReg.setInitParameter("forceEncoding", "true");
        charEncodingfilterReg.addMappingForUrlPatterns(null, false, "/*");
    }

}
