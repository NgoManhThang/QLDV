package com.viettel.api.config;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
/*import org.apache.cxf.jaxws.EndpointImpl;*/
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by VTN-PTPM-NV19 on 2/22/2018.
 */
@Configuration
public class WebServiceConfig {
    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(),"/checkinws");
    }
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus(){
        return new SpringBus();
    }
}
