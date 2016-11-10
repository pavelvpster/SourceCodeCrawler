/*
 * TextFileInputFormat.java
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

package org.interactiverobotics.source_code_crawler.step6;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

/**
 * TextFileInputFormat.
 * An {@code InputFormat} for plain text files. In comparing to {@code TextInputFormat} does not split file.
 */
public class TextFileInputFormat extends FileInputFormat<Text, Text> {

    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        return false;
    }

    @Override
    public RecordReader<Text, Text> createRecordReader(final InputSplit split, final TaskAttemptContext context)
            throws IOException, InterruptedException {
        final TextFileRecordReader reader = new TextFileRecordReader();
        reader.initialize(split, context);
        return reader;
    }
}
