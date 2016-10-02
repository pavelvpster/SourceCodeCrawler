/*
 * Step0.java
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

package org.interactiverobotics.source_code_crawler.step0;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.counting;

/**
 * Step 0.
 * Counts how many times each word appears in text.
 */
public class Step0 {

    private static final String FILENAME = "data/lorem-ipsum-5000-words.txt";

    public static void main(String[] args) throws IOException {
        countWords(FILENAME).entrySet().forEach(e ->
                System.out.println("Word '" + e.getKey() + "' observed " + e.getValue() + " time(s)."));
    }

    private static Map<String, Long> countWords(final String filename) throws IOException {
        final String text = new String(Files.readAllBytes(Paths.get(filename)), "UTF-8");
        return Arrays.asList(text.split(DELIMITERS)).stream().collect(groupingBy(Function.identity(), counting()));
    }

    private static final String DELIMITERS = "\\s+|,\\s*|\\.\\s*";
}
