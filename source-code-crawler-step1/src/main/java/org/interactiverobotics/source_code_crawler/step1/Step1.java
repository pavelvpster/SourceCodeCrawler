/*
 * Step1.java
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

package org.interactiverobotics.source_code_crawler.step1;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.indexSuperclasses;
import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.printIndex;
import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.walk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.*;

/**
 * Step 1.
 * Walks file tree and builds inverted index of class inheritance.
 */
public class Step1 {

    private static final String PATH = "source-code-crawler-step1";

    public static void main(String[] args) throws IOException {
        final Map<String, List<String>> index = new HashMap<>();
        walk(Paths.get(PATH), file -> {
            try {
                indexSuperclasses(file, index);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });
        printIndex(index);
    }
}
