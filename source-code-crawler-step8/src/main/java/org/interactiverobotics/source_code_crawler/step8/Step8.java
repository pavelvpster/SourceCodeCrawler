/*
 * Step8.java
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

package org.interactiverobotics.source_code_crawler.step8;

import org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.printIndex;
import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.walk;

/**
 * Step 8.
 * Parallel stream from Java Stream API.
 */
public class Step8 {

    private static final String PATH = "source-code-crawler-step1";

    private static final int N = 4;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        final List<Path> files = new ArrayList<>();
        walk(Paths.get(PATH), file -> files.add(file));
        final Callable<Map<String, List<String>>> task = () -> files.parallelStream()
                .map(SourceCodeCrawlerCommon::indexSuperclassesToMap)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> {
                            a.addAll(b);
                            return a;
                        }));
        // run in custom thread pool instead of default one
        final ForkJoinPool pool = new ForkJoinPool(N);
        final Map<String, List<String>> index = pool.submit(task).get();
        printIndex(index);
    }
}
