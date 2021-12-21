package datastream.integration;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @ClassName main1
 * @Author mcq
 * @Date 2021/12/16 9:41
 * @Description datastream  和 table 转换
 * @Version 1.0
 */
public class main1 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        DataStreamSource<String> datastream = env.fromElements("Alice", "Bob", "Joln");

        Table inputTable = tableEnv.fromDataStream(datastream);

        tableEnv.createTemporaryView("InputTable",inputTable);
        Table resultTable = tableEnv.sqlQuery("select upper(f0) from InputTable");

        DataStream<Row> resultStream = tableEnv.toDataStream(resultTable);

        resultStream.print();
        env.execute();


    }
}
