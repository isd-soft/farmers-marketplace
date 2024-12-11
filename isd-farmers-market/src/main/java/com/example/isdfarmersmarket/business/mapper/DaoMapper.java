package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.dto.CategoryWithNrDTO;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DaoMapper {
    CategoryDTO map(Category category);

    List<CategoryDTO> mapCategories(List<Category> categories);

    CategoryWithNrDTO mapCategoryWithNr(Category category, Integer nrOfItems);

    default List<CategoryWithNrDTO> mapCategoriesWithNr(List<Category> categories, List<Integer> nrOfItems) {
        if (categories == null || nrOfItems == null || categories.size() != nrOfItems.size()) {
            return null;
        }

        List<CategoryWithNrDTO> result = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            Integer nrOfItemsForCategory = nrOfItems.get(i);
            result.add(new CategoryWithNrDTO(category.getId(), category.getTitle(), nrOfItemsForCategory));
        }
        return result;
    }

    DeliveryTypeDTO map (DeliveryTypeFarmer deliveryTypeFarmer);

    List<DeliveryTypeDTO> mapDeliveryTypes(List<DeliveryTypeFarmer> deliveryTypeFarmerList);
}
