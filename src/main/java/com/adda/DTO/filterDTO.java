package com.adda.DTO;

import com.adda.domain.CategoriesEntity;
import lombok.Data;

@Data
public class filterDTO {
    Integer startPrice;
    Integer endPrice;
    String categoryName;
}
