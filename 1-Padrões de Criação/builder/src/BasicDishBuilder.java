public class BasicDishBuilder implements MealBoxBuilder, Builder, MealBuilderProtocol, BeverageBuilderProtocol {
    private MealBox meal = new MealBox();

    @Override
    public BasicDishBuilder reset() {
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
    public BasicDishBuilder makeMeal() {
        Rice rice = new Rice(5);
        Beans beans = new Beans(10);
        this.meal.add(rice, beans);
        return this;
    }

    @Override
    public BasicDishBuilder makeBeverage() {
        Beverage beverage = new Beverage(5);
        this.meal.add(beverage);
        return this;
    }
}
