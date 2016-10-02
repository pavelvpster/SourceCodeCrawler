/*
 * SourceCodeCrawlerCommon.java
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

package org.interactiverobotics.source_code_crawler.common;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Contains common methods.
 */
public final class SourceCodeCrawlerCommon {

    private SourceCodeCrawlerCommon() {
    }

    public static void walk(final Path path, final Consumer<Path> consumer) throws IOException {
        final PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.java");
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (attrs.isRegularFile() && matcher.matches(file.getFileName())) {
                    consumer.accept(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void indexSuperclasses(final Path file, final Map<String, List<String>> index) throws IOException {
        System.out.println("Index file '" + file.getFileName() + "'");
        final String text = new String(Files.readAllBytes(file), "UTF-8");
        final Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            final String[] superclasses = matcher.group(3).split(",");
            Arrays.asList(superclasses).forEach(t -> addToIndex(t.trim(), file.getFileName().toString(), index));
        }
    }

    private static final Pattern PATTERN = Pattern.compile("(.*)(extends |implements )(.*)( \\{)");

    public static void addToIndex(final String key, final String value, final Map<String, List<String>> index) {
        final List<String> values = Optional.ofNullable(index.get(key)).orElseGet(ArrayList::new);
        values.add(value);
        index.put(key, values);
    }

    public static void printIndex(final Map<String, List<String>> index) {
        index.entrySet().forEach(e ->
                System.out.println("Class '" + e.getKey() + "' has following subclasses: " + e.getValue()));
    }
}
