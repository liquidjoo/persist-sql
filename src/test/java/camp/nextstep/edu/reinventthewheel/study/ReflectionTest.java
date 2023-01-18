package camp.nextstep.edu.reinventthewheel.study;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car("test", 10000);
    }

    @Test
    @DisplayName("Car 객체 정보 가져오기")
    void showClass() {
        Class<Car> carClass = Car.class;
        logger.debug(carClass.getName());
    }

    @Test
    @DisplayName("test로 시작하는 함수 실행")
    void testMethodRun() throws Exception {
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
        Method[] methods = car.getClass().getDeclaredMethods();
        List<String> filteredMethods = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(PrintView.class))
            .map(method -> method.getName())
            .collect(Collectors.toList());

        assertThat(filteredMethods).contains("printView");
    }

    @Test
    void privateFieldAccess() throws NoSuchFieldException, IllegalAccessException {
        Field carName = car.getClass().getDeclaredField("name");
        Field carPrice = car.getClass().getDeclaredField("price");
        carName.setAccessible(true);
        carPrice.setAccessible(true);
        carName.set(car, "hello");
        carPrice.set(car, 100000);

        assertThat(car.getName()).isEqualTo("hello");
        assertThat(car.getPrice()).isEqualTo(100000);
    }

    @Test
    void constructorWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<Car> constructor = Car.class.getConstructor(String.class, int.class);
        Car car = constructor.newInstance("test2", 1000022);

        assertThat(car.getName()).isEqualTo("test2");
        assertThat(car.getPrice()).isEqualTo(1000022);
    }

    @Test
    void getAllFieldByColumnAnnotation() {
        Class<Wheel> wheelClass = Wheel.class;

        List<String> columnNames = Arrays.stream(wheelClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .map(Field::getName)
            .collect(Collectors.toList());

        assertThat(wheelClass.isAnnotationPresent(Entity.class)).isTrue();
        assertThat(columnNames).contains("name");
        assertThat(columnNames).hasSize(1);
    }

    @Test
    @DisplayName("요구사항 7 - 클래스의 Entity 애노에티션을 확인 후 Column 애노테이션에 설정된 name 값으로 필드정보를 가져온다.")
    void getFieldByColumnAnnotationNameValue() throws IllegalAccessException {
        Class<Wheel> wheelClass = Wheel.class;
        Wheel wheel = new Wheel("test", 100);

        String columnName = "name";

        Field hasColumnAnnotationField = Arrays.stream(wheelClass.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .filter(field -> columnName.equals(field.getAnnotation(Column.class).name()))
            .peek(field -> field.setAccessible(true))
            .findFirst()
            .orElseThrow(NoSuchElementException::new);

        String wheelName = (String) hasColumnAnnotationField.get(wheel);

        assertThat(wheelClass.isAnnotationPresent(Entity.class)).isTrue();
        assertThat(wheelName).isEqualTo("test");
    }
}
