
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AppTest {

    @Test
    public void testMainDishBuilder_fullMeal() {
        MainDishBuilder builder = new MainDishBuilder();
        builder.makeMeal().makeBeverage().makeDessert();

        double expectedPrice = 5 + 10 + 20 + 7 + 10;
        assertEquals(expectedPrice, builder.getPrice(), 0.001);
    }

    @Test
    public void testMainDishBuilder_reset() {
        MainDishBuilder builder = new MainDishBuilder();
        builder.makeMeal();
        assertTrue(builder.getPrice() > 0);

        builder.reset();
        assertEquals(0, builder.getPrice(), 0.001);
    }

    @Test
    public void testBasicDishBuilder_mealAndBeverage() {
        BasicDishBuilder builder = new BasicDishBuilder();
        builder.makeMeal().makeBeverage();

        double expectedPrice = 5 + 10 + 5;
        assertEquals(expectedPrice, builder.getPrice(), 0.001);
    }

    @Test
    public void testBasicDishBuilder_reset() {
        BasicDishBuilder builder = new BasicDishBuilder();
        builder.makeMeal();
        assertTrue(builder.getPrice() > 0);

        builder.reset();
        assertEquals(0, builder.getPrice(), 0.001);
    }
}
