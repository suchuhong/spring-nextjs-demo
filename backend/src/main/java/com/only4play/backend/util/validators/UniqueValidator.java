package com.only4play.backend.util.validators;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class UniqueValidator implements ConstraintValidator<Unique, String> {
//  spring 6
//  private final JdbcClient jdbcClient;
  private final JdbcTemplate jdbcTemplate;
  private String tableName;
  private String columnName;

  public UniqueValidator(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public void initialize(Unique constraintAnnotation) {
    tableName = constraintAnnotation.tableName();
    columnName = constraintAnnotation.columnName();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
    int count = jdbcTemplate.queryForObject(sql, new Object[]{value}, Integer.class);
    return count == 0;
  }
}