/*
 * SourceCodeCrawlerCommon.java
 *
 * Copyright (C) 2016-2018 Pavel Prokhorov (pavelvpster@gmail.com)
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
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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

    private static final Pattern PATTERN = Pattern.compile("(class |interface )(.*)( extends | implements )(.*)( \\{)");

    public static void indexSuperclasses(final String text, final BiConsumer<String, String> mapper) {
        final Matcher matcher = PATTERN.matcher(text);
        if (matcher.find()) {
            final String clazz = matcher.group(2).trim();
            final String[] superclasses = matcher.group(4).split(",");
            Arrays.asList(superclasses).forEach(superclass -> mapper.accept(superclass.trim(), clazz));
        }
    }

    public static void indexSuperclasses(final Path file, final BiConsumer<String, String> mapper) throws IOException {
        System.out.println("Index file '" + file.getFileName() + "'");
        final String text = new String(Files.readAllBytes(file), "UTF-8");
        indexSuperclasses(text, mapper);
    }

    public static <T> List<T> indexSuperclassesToList(final String text, final BiFunction<String, String, T> mapper) {
        final List<T> items = new ArrayList<>();
        indexSuperclasses(text, (key, value) -> items.add(mapper.apply(key, value)));
        return items;
    }

    public static Map<String, List<String>> indexSuperclassesToMap(final Path file) {
        final Map<String, List<String>> index = new HashMap<>();
        try {
            indexSuperclasses(file, (key, value) -> addToIndex(key, value, index));
        } catch (IOException e) {
        }
        return index;
    }

    public static void addToIndex(final String key, final String value, final Map<String, List<String>> index) {
        final List<String> values = Optional.ofNullable(index.get(key)).orElseGet(ArrayList::new);
        if (!values.contains(value)) {
            values.add(value);
            index.put(key, values);
        }
    }

    public static void addToIndex(final String key, final List<String> values, final Map<String, List<String>> index) {
        values.forEach(value -> addToIndex(key, value, index));
    }

    public static void printIndex(final Map<String, List<String>> index) {
        index.entrySet().forEach(e ->
                System.out.println("Class '" + e.getKey() + "' has following subclasses: " + e.getValue()));
    }
}
