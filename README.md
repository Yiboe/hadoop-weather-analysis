# Hadoop Weather Analysis

一个使用 Hadoop MapReduce 处理 NOAA ISD-Lite 气象观测数据的 Java 示例项目。程序按日期聚合观测值，并输出每日最低、最高和平均气温。

## 功能

- 解析 ISD-Lite 文本记录。
- 忽略气温缺失值 `-9999`，并通过 Hadoop Counter 统计缺失或格式错误的记录。
- 保留原始的十分之一摄氏度精度，避免整数除法造成负温截断。
- 使用命令行参数指定 HDFS 输入、输出路径。
- 使用单元测试验证解析和温度聚合逻辑。

## 数据来源与格式

数据格式来自 [NOAA/NCEI Integrated Surface Data Lite](https://www.ncei.noaa.gov/pub/data/noaa/isd-lite/)。第五个字段为空气温度，缩放因子为 10，缺失值为 `-9999`。仓库只包含一个小型示例文件 [`examples/isd-lite-sample.txt`](examples/isd-lite-sample.txt)，不包含完整原始数据。

ISD-Lite 通常按“气象站—年份”保存为独立文件，记录本身不包含站点编号。若把多个站点文件直接合并，程序得到的是所有记录混合后的每日统计。需要站点级结果时，应在数据采集阶段保留站点编号，并把 MapReduce 键改为“站点编号 + 日期”。

## 环境要求

- JDK 8 或更高版本
- Apache Maven 3.8 或更高版本
- Apache Hadoop 3.3.x；默认编译依赖版本为 3.3.6

Hadoop 依赖使用 Maven `provided` 作用域，不会被重复打入项目 JAR，运行时由 Hadoop 集群提供。

## 构建与测试

```bash
mvn clean verify
```

构建产物位于：

```text
target/hadoop-weather-analysis.jar
```

## 运行

先将输入文件上传到 HDFS：

```bash
hdfs dfs -mkdir -p /user/$USER/weather/input
hdfs dfs -put examples/isd-lite-sample.txt /user/$USER/weather/input/
```

提交 MapReduce 作业。输出目录必须尚不存在：

```bash
hadoop jar target/hadoop-weather-analysis.jar \
  /user/$USER/weather/input \
  /user/$USER/weather/output
```

查看结果：

```bash
hdfs dfs -cat /user/$USER/weather/output/part-r-*
```

每行输出格式为：

```text
日期<TAB>最低气温<TAB>最高气温<TAB>平均气温
```

## 项目结构

```text
src/main/java/       MapReduce 与纯 Java 统计逻辑
src/test/java/       单元测试
examples/            可公开的小型输入样例
.github/workflows/   GitHub Actions 持续集成
```

## 已知限制

- 当前作业只分析空气温度字段。
- 输入数据若未保留站点编号，只能生成跨全部输入记录的每日聚合结果。
- 原始数据的完整性、站点覆盖和时间范围需要由数据采集流程单独校验。

## 许可证

本项目代码使用 [Apache License 2.0](LICENSE)。NOAA/NCEI 数据不包含在代码许可证范围内；使用数据时请注明来源并遵守其适用条款。
