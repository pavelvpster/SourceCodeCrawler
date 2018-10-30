/*
 * Step4.java
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

package org.interactiverobotics.source_code_crawler.step4;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Step 4.
 * Each thread builds intermediate index. Main thread receives indexes through {@code Future} and merges them.
 * {@code ExecutorService} is used to limit number of threads created.
 */
public class Step4 {

    private static final String PATH = "source-code-crawler-step1";

    public static void main(String[] args) throws IOException {
        final ExecutorService service = Executors.newWorkStealingPool();
        final List<Future<Map<String, List<String>>>> results = new ArrayList<>();
        walk(Paths.get(PATH), file -> results.add(service.submit(() -> indexSuperclassesToMap(file))));
        service.shutdown();
        final Map<String, List<String>> index = results.stream()
                .flatMap(future -> {
                    try {
                        return Stream.of(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return Stream.empty();
                    }
                })
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> {
                            a.addAll(b);
                            return a;
                        }));
        printIndex(index);
    }
}
