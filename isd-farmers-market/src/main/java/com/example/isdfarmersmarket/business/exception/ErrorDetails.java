package com.example.isdfarmersmarket.business.exception;

import java.time.LocalDate;

public record ErrorDetails(LocalDate timeStamp, String message) {
}
