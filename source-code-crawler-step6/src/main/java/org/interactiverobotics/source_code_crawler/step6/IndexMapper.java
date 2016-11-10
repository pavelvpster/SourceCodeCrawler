/*
 * IndexMapper.java
 *
 * Copyright (C) 2016 Pavel Prokhorov (pavelvpster@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.interactiverobotics.source_code_crawler.step6;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IndexMapper.
 */
public class IndexMapper extends Mapper<Text, Text, Text, Text> {

    public static final Pattern PATTERN = Pattern.compile("(class |interface )(.*)( extends | implements )(.*)( \\{)");

    public void map(final Text key, final Text value, final Context context)
            throws IOException, InterruptedException {
        final Matcher matcher = PATTERN.matcher(value.toString());
        if (matcher.find()) {
            final String clazz = matcher.group(2);
            final String[] superclasses = matcher.group(4).split(",");
            for (final String superclass : superclasses) {
                context.write(new Text(superclass.trim()), new Text(clazz.trim()));
            }
        }
    }
}
