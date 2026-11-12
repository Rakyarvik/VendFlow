package top.rakyarvik.vendflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.rakyarvik.vendflow.entity.Order;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    @Select("SELECT * FROM orders WHERE status = 'CREATED' AND expire_at < NOW()")
    List<Order> selectExpiredCreatedOrders();
}
