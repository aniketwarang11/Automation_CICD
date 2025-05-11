package CapabilityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


import CapabilityOptions.CapabilityOptions;
import Utility.PropertyReader;


public class CapabilityManager{

    private static InheritableThreadLocal<Properties> xmlP = new InheritableThreadLocal<>();
    private static InheritableThreadLocal<Properties> localprop = new InheritableThreadLocal<>();

    
    
    public static void setCapabilities(Class<?> testClass) {
        CapabilityOptions opt = testClass.getAnnotation(CapabilityOptions.class);
        if (opt == null) {
            throw new RuntimeException("No CapabilityOptions annotation found on class " + testClass.getName());
        }
        

        File configFile = new File(System.getProperty("user.dir") + "/configs/" + opt.configName() + ".xml");
        InputStream reader;
        try {
            reader = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        Properties xmlProps = new Properties();
        try {
            xmlProps.loadFromXML(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        xmlP.set(xmlProps);
        Properties localProps = new Properties();
        xmlProps.setProperty("configName", opt.configName());

        setProp(localProps, xmlProps, "deviceName");
        setProp(localProps, xmlProps, "platformName");
        setProp(localProps, xmlProps, "securityToken");
        setProp(localProps, xmlProps, "cloudURL");
        setProp(localProps, xmlProps, "manufacturer");
        setProp(localProps, xmlProps, "automationName");
        setProp(localProps, xmlProps, "screenshotOnError");
        setProp(localProps, xmlProps, "driver_retries");
        setProp(localProps, xmlProps, "driver_retryIntervalSec");
        setProp(localProps, xmlProps, "browser");
        setProp(localProps, xmlProps, "appURL");
        setProp(localProps, xmlProps, "fastWeb");
        setProp(localProps, xmlProps, "platformVersion");
        setProp(localProps, xmlProps, "browserName");
        setProp(localProps, xmlProps, "browserVersion");
        setProp(localProps, xmlProps, "location");
        setProp(localProps, xmlProps, "resolution");

        localprop.set(localProps);
        PropertyReader.setProperties(localProps);
    }

    private static void setProp(Properties targetProps, Properties sourceProps, String pName) {
        if (sourceProps.getProperty(pName) != null) {
            targetProps.setProperty(pName, sourceProps.getProperty(pName));
        }
    }

    public static Properties getLocalProp() {
        return localprop.get();
    }

    public static Properties getXMLProp() {
        return xmlP.get();
    }
}
