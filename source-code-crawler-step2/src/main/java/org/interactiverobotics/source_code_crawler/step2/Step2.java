/*
 * Step2.java
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

package org.interactiverobotics.source_code_crawler.step2;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

/**
 * Step 2.
 * Creates separate thread to process each file. Each thread prints its results.
 */
public class Step2 {

    private static final String PATH = "source-code-crawler-step1";

    public static void main(String[] args) throws IOException {
        walk(Paths.get(PATH), file -> new Thread(() -> {
            try {
                final Map<String, List<String>> index = new HashMap<>();
                indexSuperclasses(file, (key, value) -> addToIndex(key, value, index));
                printIndex(index);
            } catch (IOException e) { }
        }).start());
    }
}
