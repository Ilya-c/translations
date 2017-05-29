package com.haulmont.mp2xls.writer.commons;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.convert.ListDelimiterHandler;

import java.io.Writer;

/**
 * @author chekashkin
 * @version $Id$
 */
public class MessagePropertiesIOFactory extends PropertiesConfiguration.DefaultIOFactory {
    @Override
    public PropertiesConfiguration.PropertiesWriter createPropertiesWriter(Writer out, ListDelimiterHandler handler) {
        return new MessagePropertiesWriter(out, handler);
    }
}
