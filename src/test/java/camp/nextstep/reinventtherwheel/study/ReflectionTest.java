package camp.nextstep.reinventtherwheel.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;

        logger.debug(carClass.getName());
        for (Method method : carClass.getDeclaredMethods()) {
            logger.debug(method.getName());
        }

        for (Field field : carClass.getDeclaredFields()) {
            logger.debug(field.getName());
        }
    }

    @Test
    @DisplayName("test로 시작하는 메소드 실행")
    void testMethodRun() {
        Class<Car> carClass = Car.class;

        Car car = new Car("제네시스g80", 10_000);
        List<Method> testMethods = Arrays.stream(carClass.getMethods())
                .filter(method -> method.getName().startsWith("test"))
                .collect(Collectors.toList());

        List<String> results = testMethods.stream()
                .map(it -> {
                    try {
                        return (String) it.invoke(car);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        assertThat(results).contains("test : 제네시스g80", "test : 10000");
    }

    @Test
    @DisplayName("@PrintView 애노테이션 메소드 실행")
    void testAnnotationMethodRun() {
        Class<Car> carClass = Car.class;
        Car car = new Car("제네시스g80", 10_000);

        Arrays.stream(carClass.getMethods())
                .filter(method -> method.isAnnotationPresent(PrintView.class))
                .forEach(it -> {
                    try {
                        it.invoke(car);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }
}
