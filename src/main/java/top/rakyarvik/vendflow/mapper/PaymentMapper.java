package top.rakyarvik.vendflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.rakyarvik.vendflow.entity.Payment;

@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {
}
