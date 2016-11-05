/*
 * Step5.java
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

package org.interactiverobotics.source_code_crawler.step5;

import static org.interactiverobotics.source_code_crawler.common.SourceCodeCrawlerCommon.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Step 5.
 * Supplier (anonymous thread), Mappers, Reducer (main thread) are separated by BlockingQueues.
 */
public class Step5 {

    private static final String PATH = "source-code-crawler-step1";

    private static final int N = 4;

    public static void main(String[] args) throws InterruptedException {
        final BlockingQueue<Path> files = new ArrayBlockingQueue<>(N);
        final AtomicLong fileCount = new AtomicLong(0L);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(() -> {
            try {
                walk(Paths.get(PATH), file -> {
                    try {
                        files.put(file);
                        fileCount.incrementAndGet();
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                    }
                });
            } catch (IOException e) {
            }
        }).start();
        final BlockingQueue<Map<String, List<String>>> results = new ArrayBlockingQueue<>(N);
        final List<Thread> mappers = IntStream.range(0, N)
                .mapToObj(i -> new Thread(() -> {
                    try {
                        while (true) {
                            final Map<String, List<String>> index = new HashMap<>();
                            try {
                                indexSuperclasses(files.take(), (key, value) -> addToIndex(key, value, index));
                            } catch (IOException e) {
                            }
                            results.put(index);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                })).peek(Thread::start).collect(Collectors.toList());
        countDownLatch.await();
        final Map<String, List<String>> index = new HashMap<>();
        while (fileCount.getAndDecrement() > 0) {
            results.take().entrySet().forEach(e -> e.getValue().forEach(v -> addToIndex(e.getKey(), v, index)));
        }
        printIndex(index);
        mappers.forEach(mapper -> mapper.interrupt());
    }
}
