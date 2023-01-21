package camp.nextstep.reinventtherwheel.study;

public class Car {
    private final String name;
    private final int price;

    public Car(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @PrintView
    public void printView() {
        System.out.println("자동차 정보를 출력 합니다.");
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
