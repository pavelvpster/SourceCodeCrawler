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

import java.util.Map;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.indexSuperclassesToList;

/**
 * Step9.
 */
public class Step9 {

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
                .flatMapToPair(text ->
                    indexSuperclassesToList(text, (superclass, clazz) -> new Tuple2<>(superclass, clazz))
                            .iterator())
                .reduceByKey((a, b) -> String.join(" ", a, b))
                .collectAsMap();

        output.entrySet().forEach(e ->
                System.out.println("Class '" + e.getKey() + "' has following subclasses: " + e.getValue()));
    }
}
