package com.haulmont.mp2xls.writer.commons;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.convert.ListDelimiterHandler;
import org.apache.commons.configuration2.convert.ValueTransformer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * @author chekashkin
 * @version $Id$
 */
public class MessagePropertiesWriter extends PropertiesConfiguration.PropertiesWriter {

    public MessagePropertiesWriter(Writer writer, ListDelimiterHandler delHandler) {
        super(writer, delHandler);
    }

    /**
     * Remove escaping of the unicode characters.
     */
    private static final CharSequenceTranslator ESCAPE_PROPERTIES =
            new AggregateTranslator(new LookupTranslator(new String[][]{
                    {"\\", "\\\\"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()));


    private static final ValueTransformer MESSAGE_PROPERTIES_TRANSFORMER =
            new ValueTransformer() {
                @Override
                public Object transformValue(Object value) {
                    String strVal = String.valueOf(value);
                    return ESCAPE_PROPERTIES.translate(strVal);
                }
            };

    /**
     * Copy-pasted from parent. Using own {@link MessagePropertiesWriter#MESSAGE_PROPERTIES_TRANSFORMER}
     * to avoid escaping unicode symbols in localized messages.
     */
    public void writeProperty(String key, Object value,
                              boolean forceSingleLine) throws IOException {
        String v;

        if (value instanceof List) {
            v = null;
            List<?> values = (List<?>) value;
            if (forceSingleLine) {
                try {
                    v = String.valueOf(getDelimiterHandler()
                            .escapeList(values, MESSAGE_PROPERTIES_TRANSFORMER));
                } catch (UnsupportedOperationException ignored) {
                    // the handler may not support escaping lists,
                    // then the list is written in multiple lines
                }
            }
            if (v == null) {
                writeProperty(key, values);
                return;
            }
        } else {
            v = String.valueOf(getDelimiterHandler().escape(value, MESSAGE_PROPERTIES_TRANSFORMER));
        }

        write(escapeKey(key));
        write(fetchSeparator(key, value));
        write(v);

        writeln(null);
    }
}
