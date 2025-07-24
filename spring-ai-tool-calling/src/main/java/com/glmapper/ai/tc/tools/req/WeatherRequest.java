package com.glmapper.ai.tc.tools.req;

import com.glmapper.ai.tc.tools.common.Unit;

/**
 * @ClassName WeatherRequest
 * @Description 请求记录类
 * @Author masen.27
 * @Date 2025/7/23 15:48
 * @Version 1.0
 */
public record WeatherRequest(String location, Unit unit) {
}
