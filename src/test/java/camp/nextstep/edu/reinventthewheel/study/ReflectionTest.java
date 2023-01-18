package camp.nextstep.edu.reinventthewheel.study;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    @DisplayName("요구사항 1 - 클래스 정보 출력")
    @Test
    void showClass() {
        Class<Car> carClass = Car.class;
        logger.debug(carClass.getName());
    }

    @DisplayName("요구사항 2 - test로 시작하는 메소드 실행")
    @Test
    void testMethodRun() {
        Method[] methods = car.getClass().getDeclaredMethods();
        List<String> filteredMethods = Arrays.stream(methods)
            .filter(method -> method.getName().startsWith("test"))
            .map(method -> method.getName())
            .collect(Collectors.toList());

        assertThat(filteredMethods).contains("testGetName", "testGetPrice");
    }

    @DisplayName("요구사항 3 - @PrintView 애노테이션 메소드 실행")
    @Test
    public void testAnnotationMethodRun() {
        Method[] methods = car.getClass().getDeclaredMethods();
        List<String> filteredMethods = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(PrintView.class))
            .map(method -> method.getName())
            .collect(Collectors.toList());

        assertThat(filteredMethods).contains("printView");
    }

    @DisplayName("요구사항 4 - private field에 값 할당")
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

    @DisplayName("요구사항 5 - 인자를 가진 생성자의 인스턴스 생성")
    @Test
    void constructorWithArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<Car> constructor = Car.class.getConstructor(String.class, int.class);
        Car car = constructor.newInstance("test2", 1000022);

        assertThat(car.getName()).isEqualTo("test2");
        assertThat(car.getPrice()).isEqualTo(1000022);
    }

    @DisplayName("요구사항 6 - 클래스의 Entity 애노에티션을 확인 후 Column 애노테이션이 있는 필드 이름만 가져온다.")
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

    @DisplayName("요구사항 7 - 클래스의 Entity 애노에티션을 확인 후 Column 애노테이션에 설정된 name 값으로 필드정보를 가져온다.")
    @Test
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

    @DisplayName("요구사항 8 - Transient 애노테이션이 있는 경우 필드 값에서 제외를 한다")
    @Test
    void getFieldByTransient() {
        Class<Wheel> wheelClass = Wheel.class;

        Field[] fields = wheelClass.getDeclaredFields();
        List<String> nonTransientFieldNames = Arrays.stream(fields)
            .filter(field -> !field.isAnnotationPresent(Transient.class))
            .map(Field::getName)
            .collect(Collectors.toList());

        assertThat(nonTransientFieldNames).contains("name", "size");
        assertThat(nonTransientFieldNames).doesNotContain("transientValue");
    }
}
