package camp.nextstep.edu.reinventthewheel.study;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Wheel {

    @Id
    private Long id;

    @Column
    private String name;

    private Integer size;
}
