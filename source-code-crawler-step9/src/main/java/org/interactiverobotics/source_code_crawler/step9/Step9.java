/*
 * Step9.java
 *
 * Copyright (C) 2018 Pavel Prokhorov (pavelvpster@gmail.com)
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

package org.interactiverobotics.source_code_crawler.step9;

import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Step9.
 */
public class Step9 {

    public static final Pattern PATTERN = Pattern.compile("(class |interface )(.*)( extends | implements )(.*)( \\{)");

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Step9 <input-directory>");
            System.exit(2);
        }

        final SparkSession spark = SparkSession
                .builder()
                .master("local[2]") // 2 workers
                .appName("source-code-crawler")
                .getOrCreate();

        final Map<String, String> output = spark
                .read()
                .textFile(args[0])
                .toJavaRDD()
                .flatMapToPair(text -> {
                    final List<Tuple2<String, String>> classAndSubclassTuples = new ArrayList<>();
                    final Matcher matcher = PATTERN.matcher(text);
                    if (matcher.find()) {
                        final String clazz = matcher.group(2);
                        final String[] superclasses = matcher.group(4).split(",");
                        for (final String superclass : superclasses) {
                            classAndSubclassTuples.add(new Tuple2(superclass.trim(), clazz.trim()));
                        }
                    }
                    return classAndSubclassTuples.iterator();
                })
                .reduceByKey((a, b) -> String.join(" ", a, b))
                .collectAsMap();

        output.entrySet().forEach(e ->
                System.out.println("Class '" + e.getKey() + "' has following subclasses: " + e.getValue()));
    }
}
