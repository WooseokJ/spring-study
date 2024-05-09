package hello.itemservice.repository.jdbctemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// jdbcTemplate
@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }


    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name,price,quantity) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder(); // id(pk)는 DB에서 생성하므로 비워두고 저장.
        // 데이터 변경은 update 사용. (반환 int형(영향받은 row 수))
        template.update(connection -> {
            // 자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;
        }, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);
        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where_id=?";
        template.update(sql, // 순서중요.
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId
                );
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";
        try {
            // queryForObject는 결과없으면 예외 터져
            Item item = template.queryForObject(sql, getItemRowMapper(), id);
            return Optional.of(item); // of는 인자로 null값 안받음. ofNullable은 null값 받음.
        }catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // 데이터에 결과없는 거니 빈 옵셔널 반환.
        }

    }


    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String sql = "select id, item_name, price, quantity from item";

        //동적 쿼리
        sql = ignoreChageQuery(itemName, maxPrice, sql);
        // query는 결과가 1개이상일떄 사용.
        return template.query(sql, getItemRowMapper());
    }
    //동적 쿼리(안외워도됨)
    private static String ignoreChageQuery(String itemName, Integer maxPrice, String sql) {
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',?,'%')";
            param.add(itemName);
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }
        log.info("sql={}", sql);
        return sql;
    }

    private RowMapper<Item> getItemRowMapper() {
        return ((resultSet, rowNum) ->{ // sql 결과값이 resultSet을 객체로 변환. (결과없으면 빈 컬렉션 반환)
            Item item = new Item();
            item.setId(resultSet.getLong("id"));
            item.setItemName(resultSet.getString("item_name"));
            item.setPrice(resultSet.getInt("price"));
            item.setQuantity(resultSet.getInt("quantity"));
            return item;
        });

    }
}
