package camp.nextstep.edu.reinventthewheel.study;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Wheel {

    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    private Integer size;

    public Wheel() {
    }

    public Wheel(String name, Integer size) {
        this.name = name;
        this.size = size;
    }
}
