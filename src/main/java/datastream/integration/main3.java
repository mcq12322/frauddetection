package datastream.integration;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
/**
 * @ClassName main3
 * @Author mcq
 * @Date 2021/12/16 10:20
 * @Description main3
 * @Version 1.0
 */
public class main3 {

    public static void main(String[] args) throws Exception {
        // create environments of both APIs
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //批处理模式
//        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);



        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, EnvironmentSettings.inBatchMode());

// create a DataStream
        DataStream<Row> dataStream = env.fromElements(
                Row.of("Alice", 12),
                Row.of("Bob", 10),
                Row.of("Alice", 100));

// interpret the insert-only DataStream as a Table
        Table inputTable = tableEnv.fromDataStream(dataStream).as("name", "score");


//        tableEnv.toChangelogStream(inputTable).
//                executeAndCollect().forEachRemaining(System.out::println);




// register the Table object as a view and query it
// the query contains an aggregation that produces updates
        tableEnv.createTemporaryView("InputTable", inputTable);
        Table resultTable = tableEnv.sqlQuery(
                "SELECT name, SUM(score) FROM InputTable GROUP BY name");

// interpret the updating Table as a changelog DataStream
//        DataStream<Row> resultStream = tableEnv.toChangelogStream(resultTable);
/*        3> +I[Alice, 112]
        2> +I[Bob, 10]*/

        DataStream<Row> resultStream = tableEnv.toDataStream(resultTable);


/*
        3> +I[Alice, 112]
        2> +I[Bob, 10]*/


// add a printing sink and execute in DataStream API
        resultStream.print();

        //先执行 table execute 然后执行datastream 任务
        resultTable.execute().print();
        env.execute();

    }

}
