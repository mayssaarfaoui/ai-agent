package com.aimanager.agent.services;

import com.aimanager.agent.enums.ServiceName;
import com.aimanager.agent.form.ServiceRequestForm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);

    public static String getConfigFileName(ServiceName serviceName){
        logger.info("Getting config file name for service: " + serviceName);
        switch (serviceName){
            case TaskServer:
                return "task-server";
            case OrganizationServer:
                return "organization-server";
            default:
                logger.error("Invalid service name: " + serviceName);
                throw new IllegalArgumentException("Invalid service name: " + serviceName);
        }
    }

    public static ServiceRequestForm loadConfig(ServiceName serviceName) throws IOException {
        // Specify the file path dynamically
          Yaml yaml = new Yaml(new Constructor(ServiceRequestForm.class));
          String configName = getConfigFileName(serviceName);
          String configPath = "config/" + configName + ".yml";
          logger.info("Load config from : {}",configPath);
        try (InputStream inputStream = ServiceRequestForm.class.getClassLoader().getResourceAsStream(configPath)) {
            if (inputStream == null) {
                throw new RuntimeException("Could not find the YAML file.");
            }

            // Parse YAML to Java object
            ServiceRequestForm serviceRequestForm = yaml.load(inputStream);
            System.out.println(serviceRequestForm);
            return serviceRequestForm;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
