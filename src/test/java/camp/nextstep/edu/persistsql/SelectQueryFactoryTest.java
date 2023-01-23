package camp.nextstep.edu.persistsql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SelectQueryFactoryTest {
    @Test
    @DisplayName("쿼리를 생성하는 기능")
    void testCreateQuery() {
        // given
        SelectQueryFactory selectQueryFactory = new SelectQueryFactory();

        // when
        String result = selectQueryFactory.createQuery(EntityCar.class);

        // then
        assertThat(result).isEqualTo("select id, name, price from EntityCar where id = ?");
    }

    @Test
    @DisplayName("Entity 애너테이션이 없는 클래스의 select 쿼리를 생성하면 예외를 발생시킨다.")
    void testCreateQueryIfNotExistEntityAnnotation() {
        // given
        SelectQueryFactory selectQueryFactory = new SelectQueryFactory();

        // when // then
        assertThatThrownBy(() -> selectQueryFactory.createQuery(NoEntityCar.class))
                .isExactlyInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("@Table이 있고 name이 빈 값이 아니라면 테이블 이름은 @Table의 name으로 지정된다.")
    void testCreateQueryIfExistTableEntityWithNameAnnotation() {
        // given
        SelectQueryFactory selectQueryFactory = new SelectQueryFactory();

        // when
        String result = selectQueryFactory.createQuery(TableEntityCar.class);

        // then
        assertThat(result).isEqualTo("select id from table_entity_car where id = ?");
    }

    @Test
    @DisplayName("@Table이 있고 name이 빈 값이 아니라면 테이블 이름은 Class 이름으로 지정된다.")
    void testCreateQueryIfExistTableEntityNoWithNameAnnotation() {
        // given
        SelectQueryFactory selectQueryFactory = new SelectQueryFactory();

        // when
        String result = selectQueryFactory.createQuery(TableNoWithNameEntityCar.class);

        // then
        assertThat(result).isEqualTo("select id from TableNoWithNameEntityCar where id = ?");
    }

    @Entity
    static class EntityCar {
        @Id
        private Long id;

        @Column
        private String name;

        @Column
        private int price;

        @Transient
        private boolean hidden;
    }

    static class NoEntityCar {
        @Column
        private String name;

        @Column
        private int price;

        @Transient
        private boolean hidden;
    }

    @Table(name = "table_entity_car")
    @Entity
    static class TableEntityCar {
        @Id
        private Long id;
    }

    @Table
    @Entity
    static class TableNoWithNameEntityCar {
        @Id
        private Long id;
    }
}
