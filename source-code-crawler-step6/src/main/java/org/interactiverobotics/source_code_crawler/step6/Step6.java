/*
 * Step6.java
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

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * Step6.
 */
public class Step6 extends Configured implements Tool {

    private Step6() {
    }

    public int run(final String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length < 2) {
            System.out.println("Step6 <input-directory> <output-directory>");
            ToolRunner.printGenericCommandUsage(System.out);
            return 2;
        }
        System.setProperty("HADOOP_USER_NAME", "hdfs");
        final Configuration configuration = getConf();
        final Job job = Job.getInstance(configuration);
        job.setJobName("source-code-crawler");
        job.setJarByClass(Step6.class);
        FileInputFormat.setInputDirRecursive(job, true);
        FileInputFormat.setInputPaths(job, args[0]);
        job.setInputFormatClass(TextFileInputFormat.class);
        job.setMapperClass(IndexMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setReducerClass(IndexReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true) ? 1 : 0;
    }

    public static void main(final String[] args) throws Exception {
        System.exit(ToolRunner.run(new Configuration(), new Step6(), args));
    }
}
