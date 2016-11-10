/*
 * IndexReducer.java
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
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * IndexReducer.
 */
public class IndexReducer extends Reducer<Text, Text, Text, Text> {

    public void reduce(final Text key, final Iterable<Text> values, final Context context)
            throws IOException, InterruptedException {
        context.write(key, new Text(StreamSupport.stream(values.spliterator(), false)
                .map(Object::toString).collect(Collectors.joining(" "))));
    }
}
