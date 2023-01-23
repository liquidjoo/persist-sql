package camp.nextstep.edu.persistsql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
class EntityCar {
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Transient
    private boolean hidden;
}
