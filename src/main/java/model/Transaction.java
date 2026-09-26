package model;

import java.math.BigDecimal;

public record Transaction(Long id, String ticker, int shares, BigDecimal price) { }
