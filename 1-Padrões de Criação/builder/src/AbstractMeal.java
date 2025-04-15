public abstract class AbstractMeal implements MealCompositeProtocol {
    private double price;
    private String name;

    public AbstractMeal(String name, double price) {
        this.price = price;
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

}
