package camp.nextstep.edu.reinventthewheel.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    @Test
    @DisplayName("test로 시작하는 함수 실행")
    void testMethodRun() throws Exception {

        Car car = new Car("test", 10000);
        Method[] methods = car.getClass().getDeclaredMethods();
        List<String> filteredMethods = Arrays.stream(methods)
            .filter(method -> method.getName().startsWith("test"))
            .map(method -> method.getName())
            .collect(Collectors.toList());

        assertThat(filteredMethods).contains("testGetName", "testGetPrice");
    }

    @Test
    @DisplayName("@PrintView 애노테이션 메소드 실행")
    public void runPrintView() {
        Car car = new Car("test", 10000);
        Method[] methods = car.getClass().getDeclaredMethods();
        List<String> filteredMethods = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(PrintView.class))
            .map(method -> method.getName())
            .collect(Collectors.toList());

        assertThat(filteredMethods).contains("printView");
    }
}