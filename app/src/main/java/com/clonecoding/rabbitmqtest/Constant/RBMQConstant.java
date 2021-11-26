package com.clonecoding.rabbitmqtest.Constant;

/**
 * RabbitMQ 관련 상수 함수
 */
public final class RBMQConstant {

	// Multi log tag
	public static final String MULTI_LOG = "Multi RBMQ";

	// RabbitMQ 에 연결할 exchange 이름
	public static final String EXCHANGE_NAME = "exchange_example";

	// RabbitMQ 에 연결할 multi exchange 이름
	public static final String MULTI_EXCHANGE_NAME = "Multi exchange";

	// RabbitMQ 에 연결할 exchange 타입
	public static final String EXCHANGE_TYPE = "fanout";

	// RabbitMQ 에 연결할 multi exchange 타입
	public static final String MULTI_EXCHANGE_TYPE = "direct";

	// RabbitMQ 에 연결할 queue 이름
	public static final String QUEUE_NAME = "queue_example";

	// RabbitMQ 에 연결할 multi queue 이름 1
	public static final String MULTI_QUEUE_NAME_1 = "Multi_queue_1";

	// RabbitMQ 에 연결할 multi queue 이름 2
	public static final String MULTI_QUEUE_NAME_2 = "Multi_queue_2";

	// RabbitMQ 에 연결할 multi queue 이름 3
	public static final String MULTI_QUEUE_NAME_3 = "Multi_queue_3";

	// RabbitMQ 의 서버 URL
	public static final String SERVER_HOST = "10.0.2.2";

	// 라우팅 키 1
	public static final String ROUTING_KEY_1 = "Multi Queue 1 key";

	// 라우팅 키 2
	public static final String ROUTING_KEY_2 = "Multi Queue 2 key";

	// 라우팅 키 3
	public static final String ROUTING_KEY_3 = "Multi Queue 3 key";

	// 컨텐츠 1
	public static final String CONTENT_1 = "Message 1";

	// 컨텐츠 1
	public static final String CONTENT_2 = "Message 2";

	// 컨텐츠 1
	public static final String CONTENT_3 = "Message 3";
}
