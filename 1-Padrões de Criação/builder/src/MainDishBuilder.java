public class MainDishBuilder implements MealBoxBuilder, Builder, MealBuilderProtocol, BeverageBuilderProtocol, DessertBuilderProtocol {
    private MealBox meal = new MealBox();

    @Override
    public MainDishBuilder reset() {
        this.meal = new MealBox();
        return this;
    }

    @Override
    public MealBox getMeal() {
        return this.meal;
    }

    @Override
    public double getPrice() {
        return this.meal.getPrice();
    }

    @Override
    public MainDishBuilder makeDessert() {
        Dessert dessert = new Dessert(10);
        this.meal.add(dessert);
        return this;
    }

    @Override
    public MainDishBuilder makeBeverage() {
        Beverage beverage = new Beverage(7);
        this.meal.add(beverage);
        return this;
    }

    @Override
    public MainDishBuilder makeMeal() {
        Rice rice = new Rice(5);
        Beans beans = new Beans(10);
        Meat meat = new Meat(20);
        this.meal.add(rice, beans, meat);
        return this;
    }

}
