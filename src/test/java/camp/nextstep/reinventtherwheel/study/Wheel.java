package camp.nextstep.reinventtherwheel.study;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Wheel {
    @Id
    private Long id;

    @Column(name = "column_name")
    private String name;

    @Column(name = "column_price")
    private int price;

    @Transient
    private boolean hidden;
}
