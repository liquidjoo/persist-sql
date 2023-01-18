package camp.nextstep.edu.reinventthewheel.study;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Wheel {

    @Id
    private Long id;

    @Column(name = "target")
    private String name;

    private Integer size;

    @Transient
    private String transientValue;

    public Wheel() {
    }

    public Wheel(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public Wheel(String name, Integer size, String transientValue) {
        this.name = name;
        this.size = size;
        this.transientValue = transientValue;
    }
}
