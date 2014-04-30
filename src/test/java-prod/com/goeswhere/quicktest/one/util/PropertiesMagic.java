package com.goeswhere.quicktest.one.util;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesMagic extends PropertyPlaceholderConfigurer {

    public PropertiesMagic() throws IOException {
        try (final InputStream is = PropertiesMagic.class.getResourceAsStream("/config/dev.properties")) {
            final Properties props = new Properties();
            props.load(is);
            setProperties(props);
        }
    }
}
