package de.fantjastisch.cards_backend.category.repository;

import de.fantjastisch.cards_backend.category.Category;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryQueryRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public CategoryQueryRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  public List<Category> getList() {
    String query = "select id, label from public.categories;";
    return namedParameterJdbcTemplate.query(query, new RowMapper<Category>() {
      public Category mapRow(ResultSet rs,
          int rowNum) throws SQLException {
        return Category.builder().id(rs.getString("id")).label(rs.getString("label")).build();
      }
    });
  }

}
