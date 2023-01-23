package camp.nextstep.edu.persistsql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateQueryFactoryTest {
    @Test
    @DisplayName("쿼리를 생성하는 기능")
    void testCreateQuery() {
        // given
        UpdateQueryFactory updateQueryFactory = new UpdateQueryFactory();

        // when
        String result = updateQueryFactory.createQuery(UpdateCar.class);

        // then
        assertThat(result).isEqualTo("update UpdateCar set id = ?, name = ? where id = ?");
    }

    @Entity
    static class UpdateCar {
        @Id
        private Long id;

        @Column
        private String name;
    }
}
