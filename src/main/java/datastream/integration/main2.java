package datastream.integration;


import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @ClassName main2
 * @Author mcq
 * @Date 2021/12/16 10:04
 * @Description main2
 * @Version 1.0
 */
public class main2 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        DataStream<Row> datastream = env.fromElements(
                Row.of("Alice", 12),
                Row.of("Bob", 20),
                Row.of("Alice", 100));

        Table inputTable = tableEnv.fromDataStream(datastream).as("name","score");
        tableEnv.createTemporaryView("InputTable",inputTable);
        Table resultTable = tableEnv.sqlQuery(
                "select name,sum(score) from InputTable  group by name"
        );

        DataStream<org.apache.flink.types.Row> resultStream = tableEnv.toChangelogStream(resultTable);

/*        3> +I[Alice, 12]
        3> +I[Bob, 20]
        3> -U[Alice, 12]
        3> +U[Alice, 112]*/


//        DataStream<org.apache.flink.types.Row> resultStream = tableEnv.toDataStream(resultTable);

        //toDatastream 不支持带更新的流操作
//        Exception in thread "main" org.apache.flink.table.api.TableException: Table sink 'default_catalog.default_database.Unregistered_DataStream_Sink_1' doesn't support consuming update changes which is produced by node GroupAggregate(groupBy=[name], select=[name, SUM(score) AS EXPR$1])


        resultStream.print();
        env.execute();
    }
}
