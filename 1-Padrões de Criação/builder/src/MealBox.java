import java.util.ArrayList;
import java.util.List;

public class MealBox implements MealCompositeProtocol {
    private List<MealCompositeProtocol> children = new ArrayList<>();

    public double getPrice() {
        return children.stream()
                .mapToDouble(MealCompositeProtocol::getPrice)
                .sum();
    }

    public void add(MealCompositeProtocol... meals) {
        for (MealCompositeProtocol meal : meals) {
            children.add(meal);
        }
    }
}
