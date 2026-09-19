package com.sihan.weather;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public final class WeatherMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final Text outputDate = new Text();
    private final IntWritable outputTemperature = new IntWritable();

    public enum InputCounter {
        MALFORMED_LINES,
        MISSING_TEMPERATURES
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        IsdLiteParser.ParseResult result = IsdLiteParser.parse(value.toString());

        if (result.getStatus() == IsdLiteParser.Status.MALFORMED) {
            context.getCounter(InputCounter.MALFORMED_LINES).increment(1);
            return;
        }
        if (result.getStatus() == IsdLiteParser.Status.MISSING_TEMPERATURE) {
            context.getCounter(InputCounter.MISSING_TEMPERATURES).increment(1);
            return;
        }

        outputDate.set(result.getDate());
        outputTemperature.set(result.getTemperatureTenths());
        context.write(outputDate, outputTemperature);
    }
}
