package com.github.jferard.pgloaderutils;

import java.util.Map;

/**
 * Replaces keys in a template query
 *
 * @author Julien Férard
 */
public class QueryProvider {
    public String newQuery(String template, Map<String, String> valueByKey) {
        StringBuilder queryBuilder = new StringBuilder();

        int lastJ = 0;
        int i = template.indexOf('{');
        while (i >= 0) {
            int j = template.indexOf('}', i);
            if (j == -1)
                throw new IllegalStateException(template);
            String before = template.substring(lastJ, i);
            String key = template.substring(i+1, j);
            queryBuilder.append(before);
            String value = valueByKey.get(key);
            if (value == null)
                throw new IllegalStateException(template+" % "+valueByKey+" missing "+key);

            queryBuilder.append(value);

            lastJ = j+1;
            i = template.indexOf('{', lastJ);
        }
        queryBuilder.append(template.substring(lastJ));
        return queryBuilder.toString();
    }
}