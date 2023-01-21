package camp.nextstep.reinventtherwheel.study;

public class Car {
    private final String name;
    private final int price;

    public Car(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String testGetName() {
        return "test : " + name;
    }

    public String testGetPrice() {
        return "test : " + price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
