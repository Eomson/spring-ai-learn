package com.glmapper.ai.tc.tools.resp;

import com.glmapper.ai.tc.tools.common.Unit;

/**
 * @ClassName WeatherResponse
 * @Description 天气返回响应记录类
 * @Author masen.27
 * @Date 2025/7/23 15:50
 * @Version 1.0
 */
public record WeatherResponse(double temp, Unit unit) {
}
