package datastream.integration;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @ClassName main5
 * @Author mcq
 * @Date 2021/12/22 10:01
 * @Description main5
 * @Version 1.0
 */
public class main5 {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Person> flintstones = env.fromElements(
                new Person("Fred", 35),
                new Person("wilma", 35),
                new Person("Pebbles", 2)
        );

        SingleOutputStreamOperator<Person> adults = flintstones.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {

                return person.age >= 18;
            }
        });

        adults.print();

        env.execute();

    }

    public static class Person{
        public String name;
        public Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age='" + age + '\'' +
                    '}';
        }
    }
}
