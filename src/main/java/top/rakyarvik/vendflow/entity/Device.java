package top.rakyarvik.vendflow.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("device")
public class Device {
    private Long id;
    private String deviceNo;
    private String location;
    private Integer onlineStatus;
    private LocalDateTime lastHeartbeatAt;
}
