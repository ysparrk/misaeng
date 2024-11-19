package dgp.misaeng.domain.food.repository;

import dgp.misaeng.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
