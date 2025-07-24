package com.glmapper.ai.tc.tools.service;

import com.glmapper.ai.tc.tools.common.Unit;
import com.glmapper.ai.tc.tools.req.WeatherRequest;
import com.glmapper.ai.tc.tools.resp.WeatherResponse;

import java.util.function.Function;

/**
 * @ClassName WeatherService
 * @Description 天气工具的具体实现
 * @Author masen.27
 * @Date 2025/7/23 15:51
 * @Version 1.0
 */
public class WeatherService implements Function<WeatherRequest, WeatherResponse> {

    public WeatherResponse apply(WeatherRequest weatherRequest) {
        return new WeatherResponse(30.0, Unit.C);
    }
}
