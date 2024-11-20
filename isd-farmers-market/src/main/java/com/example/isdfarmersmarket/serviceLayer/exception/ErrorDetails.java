package com.example.isdfarmersmarket.serviceLayer.exception;

import java.time.LocalDate;

public record ErrorDetails(LocalDate timeStamp, String message) {
}
