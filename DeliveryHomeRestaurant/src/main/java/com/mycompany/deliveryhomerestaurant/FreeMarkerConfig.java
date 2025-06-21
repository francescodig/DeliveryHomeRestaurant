/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.deliveryhomerestaurant;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

/**
 *
 * @author franc
 */



import jakarta.servlet.ServletContext;
import java.io.File;
import java.io.IOException;


public class FreeMarkerConfig {
    private static Configuration config;
    
    public static Configuration getConfig(ServletContext servletContext) throws IOException {
        if (config == null) {
            config = new Configuration(Configuration.VERSION_2_3_31);
            config.setDefaultEncoding("UTF-8");
            
            // Usa il percorso assoluto del filesystem
            String templatePath = servletContext.getRealPath("/WEB-INF/templates");
            config.setDirectoryForTemplateLoading(new File(templatePath));
        }
        return config;
    }
}