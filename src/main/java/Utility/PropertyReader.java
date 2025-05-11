package Utility;

import java.util.Properties;

public class PropertyReader {
	
	private static InheritableThreadLocal<Properties> props = new InheritableThreadLocal<Properties>() {

        @Override
        protected Properties initialValue() {
            return new Properties();
        }

        @Override
        protected Properties childValue(Properties parentValue) {
            return parentValue;
        }
    };

    public static Properties getProperties() {
        return props.get();
    }

    public static void setProperties(Properties properties) {
        props.set(properties);
    }

}
