package camp.nextstep.edu.persistsql;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeleteQueryFactoryTest {

    @Test
    @DisplayName("쿼리를 생성하는 기능")
    void testCreateQuery() {
        // given
        DeleteQueryFactory deleteQueryFactory = new DeleteQueryFactory();

        // when
        String result = deleteQueryFactory.createQuery(EntityCar.class);

        // then
        assertThat(result).isEqualTo("delete from EntityCar where id = ?");
    }
}
