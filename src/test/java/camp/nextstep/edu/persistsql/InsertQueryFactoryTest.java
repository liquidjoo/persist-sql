package camp.nextstep.edu.persistsql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.assertj.core.api.Assertions.assertThat;

class InsertQueryFactoryTest {
    @Test
    @DisplayName("쿼리를 생성하는 기능")
    void testCreateQuery() {
        // given
        InsertQueryFactory insertQueryFactory = new InsertQueryFactory();

        // when
        String result = insertQueryFactory.createQuery(InsertCar.class);

        // then
        assertThat(result).isEqualTo("insert into InsertCar(id, name) values (?, ?)");
    }

    @Entity
    static class InsertCar {
        @Id
        private Long id;

        @Column
        private String name;

        public InsertCar(String name) {
            this.name = name;
        }
    }
}
