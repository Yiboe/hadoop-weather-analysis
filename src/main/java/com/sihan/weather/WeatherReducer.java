package com.sihan.weather;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public final class WeatherReducer extends Reducer<Text, IntWritable, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        TemperatureStatistics statistics = new TemperatureStatistics();
        for (IntWritable value : values) {
            statistics.add(value.get());
        }

        if (!statistics.isEmpty()) {
            context.write(key, new Text(statistics.format()));
        }
    }
}
