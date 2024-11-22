package com.example.isdfarmersmarket.web.commands;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FarmerReviewCommand {
    Long farmerId;
    float rating;
    String content;
}
