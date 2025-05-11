package CapabilityOptions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface CapabilityOptions {

    String deviceName() default "";
    
    String platformName() default "";
    
    String cloudURL() default "nyl-gmadportal.perfectomobile.com";
    
    String securityToken() default "";
    
    String manufacturer() default "";
    
    String automationName() default "Appium";
    
    String screenshotOnError() default "true";
    
    String driver_retries() default "1";
    
    String driver_retryIntervalSec() default "5";
    
    String browser() default "CHROME";
    
    String appURL() default "";
    
    String fastWeb() default "false";
    
    String configName() default "";
    
}
