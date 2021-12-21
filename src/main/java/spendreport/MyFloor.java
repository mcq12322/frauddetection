package spendreport;

import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.functions.ScalarFunction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @ClassName MyFloor
 * @Author mcq
 * @Date 2021/12/15 14:56
 * @Description MyFloor
 * @Version 1.0
 */
public class MyFloor  extends ScalarFunction {


    public @DataTypeHint("TIMESTAMP(3)") LocalDateTime eval(@DataTypeHint("TIMESTAMP(3)") LocalDateTime timestamp){
        return  timestamp.truncatedTo(ChronoUnit.HOURS);
    }
}
