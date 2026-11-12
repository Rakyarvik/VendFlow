package top.rakyarvik.vendflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.rakyarvik.vendflow.common.Result;
import top.rakyarvik.vendflow.entity.Device;
import top.rakyarvik.vendflow.mapper.DeviceMapper;

import jakarta.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/device")
public class DeviceController {
    @Resource
    private DeviceMapper deviceMapper;

    @GetMapping("/list")
    public Result<List<Device>> list() {
        return Result.success(deviceMapper.selectList(null));
    }
}
