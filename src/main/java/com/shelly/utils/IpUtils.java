package com.shelly.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shelly.entity.dto.BiliIpInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class IpUtils {

    /**
     * 根据ip调用B站接口获取地理位置
     *
     * @param ip ip
     * @return 地理位置
     */
    public static String getIpSource(String ip) {
        log.info("IP: {}", ip);
        try {
            String uri = "https://api.live.bilibili.com/client/v1/Ip/getInfoNew?ip=" + ip;
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            log.info("IP接口返回内容: {}", body);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(body);

            int code = root.path("code").asInt(-1);
            if (code == 0) {
                JsonNode data = root.path("data");
                String country = data.path("country").asText("");
                String province = data.path("province").asText("");
                String city = data.path("city").asText("");
                String isp = data.path("isp").asText("");
                return String.join("|", country, province, city, isp);
            } else {
                log.warn("IP解析失败，code={}", code);
            }
        } catch (Exception e) {
            log.error("getIpSource error: ", e);
        }
        return null;
    }

}
