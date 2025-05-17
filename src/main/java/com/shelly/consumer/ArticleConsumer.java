package com.shelly.consumer;

import cn.hutool.json.JSONUtil;
import com.shelly.entity.dto.CanalDTO;
import com.shelly.entity.vo.Response.ArticleSearchResp;
import com.shelly.service.impl.ElasticsearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

import static com.shelly.constants.ElasticConstant.*;
import static com.shelly.constants.MqConstant.*;

@Component
public class ArticleConsumer {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = ARTICLE_QUEUE, durable = "true", autoDelete = "false"),
                    exchange = @Exchange(value = ARTICLE_EXCHANGE, type = ExchangeTypes.TOPIC),
                    key = ARTICLE_KEY
            )})
    public void listenSaveArticle(Message message) {
        String data = new String(message.getBody(), StandardCharsets.UTF_8);
        CanalDTO canalDTO = JSONUtil.toBean(JSONUtil.toJsonStr(data), CanalDTO.class);
        if (canalDTO.getIsDdl()) {
            return;
        }
        ArticleSearchResp article = JSONUtil.toBean(JSONUtil.toJsonStr(canalDTO.getData().get(0)), ArticleSearchResp.class);
        switch (canalDTO.getType()) {
            case INSERT:
                elasticsearchService.addArticle(article);
                break;
            case UPDATE:
                elasticsearchService.updateArticle(article);
                break;
            case DELETE:
                elasticsearchService.deleteArticle(article.getId());
                break;
            default:
                break;
        }
    }
}