package com.zlr.seat.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.config
 * @Description
 * @create 2022-11-04-上午12:58
 */
@Configuration
public class MQConfig {
    public static final String SECKILL_QUEUE = "seckill.queue";
    public static final String SECKILL_EXCHANGE = "seckill.exchange";
    public static final String SECKILL_ROUTING_KEY = "seckill.key";
    public static final String QUEUE = "queue";
    public static final String CLIENT_QUEUE = "client.queue";
    public static final String CLIENT_EXCHANGE = "client.exchange";

    @Bean
    public MessageConverter getMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }
    /**
     * Direct模式 交换机Exchange
     * */
	@Bean
	public Queue queue() {
		return new Queue(SECKILL_QUEUE, true);
	}
	@Bean
	public DirectExchange topicDirect(){
		return new DirectExchange(SECKILL_EXCHANGE);
	}

    /**
     * Direct模式 交换机Exchange
     * */
    @Bean
    public Queue clientQueue() {
        return new Queue(CLIENT_QUEUE, true);
    }
    @Bean
    public DirectExchange clientTopicDirect(){
        return new DirectExchange(CLIENT_EXCHANGE);
    }

	/**
     * Topic模式 交换机Exchange
     * *//*
	@Bean
	public Queue topicQueue1() {
		return new Queue(TOPIC_QUEUE1, true);
	}
	@Bean
	public Queue topicQueue2() {
		return new Queue(TOPIC_QUEUE2, true);
	}
	@Bean
	public TopicExchange topicExchage(){
		return new TopicExchange(TOPIC_EXCHANGE);
	}
	@Bean
	public Binding topicBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(topicExchage()).with("topic.key1");
	}
	@Bean
	public Binding topicBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(topicExchage()).with("topic.#");
	}
	*//**
     * Fanout模式 交换机Exchange
     * *//*
	@Bean
	public FanoutExchange fanoutExchage(){
		return new FanoutExchange(FANOUT_EXCHANGE);
	}
	@Bean
	public Binding FanoutBinding1() {
		return BindingBuilder.bind(topicQueue1()).to(fanoutExchage());
	}
	@Bean
	public Binding FanoutBinding2() {
		return BindingBuilder.bind(topicQueue2()).to(fanoutExchage());
	}
	*//**
     * Header模式 交换机Exchange
     * *//*
	@Bean
	public HeadersExchange headersExchage(){
		return new HeadersExchange(HEADERS_EXCHANGE);
	}
	@Bean
	public Queue headerQueue1() {
		return new Queue(HEADER_QUEUE, true);
	}
	@Bean
	public Binding headerBinding() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("header1", "value1");
		map.put("header2", "value2");
		return BindingBuilder.bind(headerQueue1()).to(headersExchage()).whereAll(map).match();
	}
	*/
}
