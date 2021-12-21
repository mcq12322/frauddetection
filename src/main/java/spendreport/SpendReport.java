package spendreport;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.flink.table.api.*;
import org.apache.flink.table.expressions.TimeIntervalUnit;

import static org.apache.flink.table.api.Expressions.*;

/**
 * @ClassName SpendReport
 * @Author mcq
 * @Date 2021/12/15 14:26
 * @Description SpendReport
 * @Version 1.0
 */
public class SpendReport {
    public static void main(String[] args) {
        EnvironmentSettings settings = EnvironmentSettings.inStreamingMode();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        tEnv.useCatalog("custom_catalog");
        tEnv.useDatabase("custom_databases");


        tEnv.executeSql("create table transactions (");

        tEnv.executeSql("");

        Table transactions = tEnv.from("transactions");
        report(transactions).executeInsert("spend_report");

        tEnv.createTemporaryView("temp_view",transactions);

        TableDescriptor sourceDescriptor = TableDescriptor.forConnector("kafka")
                .schema(Schema.newBuilder()
                        .column("f0",DataTypes.STRING())
                        .build())
                .build();
        tEnv.createTable("",sourceDescriptor);

    }

    private static Table report(Table transactions) {

        return transactions.select(
                $("account_id"),
//                $("transaction_time").floor(TimeIntervalUnit.HOUR).as("log_ts"),
                call(MyFloor.class,$("transactions_time")).as("log_ts"),
                $("amount"))
                .groupBy($("account_id"),$("log_ts"))
                .select(
                        $("account_id"),
                        $("log_ts"),
                        $("amount").sum().as("amount")
                );
    }

    private static  Table report_stream(Table transcations){
        return  transcations
                .window(Tumble.over(lit(1).hour()).on($("transaction_time")).as("log_ts"))
                .groupBy($("account_id"),$("log_ts"))
                .select(
                        $("account_id"),
                        $("log_ts").start().as("log_ts"),
                        $("amount").sum().as("amount")
                );
    }
}
