package com.clonecoding.rabbitmqtest.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.clonecoding.rabbitmqtest.Constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.dto.RoomDto;
import com.google.gson.Gson;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * RabbitMQ repository
 */
public class MultiCosumer {

	// 서버 연결
	private Connection connection;

	// 채널
	private Channel channel;

	// 텍스트
	public MutableLiveData<String> text1 = new MutableLiveData<>();

	// 텍스트
	public MutableLiveData<String> text2 = new MutableLiveData<>();

	// 텍스트
	public MutableLiveData<String> text3 = new MutableLiveData<>();

	// 실패 콜백
	CancelCallback cancelCallback = consumerTag -> showErrorLog("Consume cancel 실패 " + consumerTag);

	// 성공 콜백
	DeliverCallback deliverCallback = (consumerTag, message) -> {

		String body = new String(message.getBody(), StandardCharsets.UTF_8);

		Log.d(RBMQConstant.MULTI_LOG, message.getEnvelope().getExchange() + " - " + message.getProperties().getClassName());
		Log.d(RBMQConstant.MULTI_LOG, message.getEnvelope().getRoutingKey() + " - " + body);

		switch (message.getEnvelope().getRoutingKey()) {
			case RBMQConstant.ROUTING_KEY_1:
				text1.postValue(body);
				break;
			case RBMQConstant.ROUTING_KEY_2:
				text2.postValue(body);
				break;
			case RBMQConstant.ROUTING_KEY_3:
				text3.postValue(body);
				break;
			default:
				break;
		}
	};

	private MultiCosumer() {
	}

	private static class LazyHolder {

		private static final MultiCosumer INSTANCE = new MultiCosumer();
	}

	/**
	 * 싱글턴 인스턴스
	 *
	 * @return 싱글턴 인스턴스
	 */
	public static MultiCosumer getInstance() {

		return LazyHolder.INSTANCE;
	}

	/**
	 * RabbitMQ 연결
	 */
	public void connection() {

		// 서버 설정
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try {
			// 서버 연결
			this.connection = factory.newConnection();

			// 채널 생성
			this.channel = this.connection.createChannel();
			
			// Exchange 생성
			channel.exchangeDeclare(RBMQConstant.MULTI_EXCHANGE_NAME, RBMQConstant.MULTI_EXCHANGE_TYPE);
			
			// 큐 생성
			channel.queueDeclare(RBMQConstant.MULTI_QUEUE_NAME_1, false, false, false, null);
			channel.queueDeclare(RBMQConstant.MULTI_QUEUE_NAME_2, false, false, false, null);
			channel.queueDeclare(RBMQConstant.MULTI_QUEUE_NAME_3, false, false, false, null);


			// 큐 바인딩
			channel.queueBind(RBMQConstant.MULTI_QUEUE_NAME_1, RBMQConstant.MULTI_EXCHANGE_NAME, RBMQConstant.ROUTING_KEY_1);
			channel.queueBind(RBMQConstant.MULTI_QUEUE_NAME_2, RBMQConstant.MULTI_EXCHANGE_NAME, RBMQConstant.ROUTING_KEY_2);
			channel.queueBind(RBMQConstant.MULTI_QUEUE_NAME_3, RBMQConstant.MULTI_EXCHANGE_NAME, RBMQConstant.ROUTING_KEY_3);

			// Recv 설정
			this.channel.basicConsume(
					RBMQConstant.MULTI_QUEUE_NAME_1, true, deliverCallback, cancelCallback);
			this.channel.basicConsume(
					RBMQConstant.MULTI_QUEUE_NAME_2, true, deliverCallback, cancelCallback);
			this.channel.basicConsume(
					RBMQConstant.MULTI_QUEUE_NAME_3, true, deliverCallback, cancelCallback);

		} catch (Exception e) {

			close();
		}
	}

	/**
	 * 서버 연결 종료
	 */
	public void close() {

		Executor executors = Executors.newSingleThreadExecutor();
		executors.execute(() -> {

			try {
				if (channel != null) {
					channel.close();
				}

				if (connection != null) {
					connection.close();
				}

				this.showErrorLog("연결 해제");
			} catch (Exception e) {

				this.showErrorLog("연결 해제 실패" + e.toString());
			}
		});
	}

	/**
	 * 에러 로그 출력
	 *
	 * @param log 로그 내용
	 */
	private void showErrorLog(String log) {

		Log.e(RBMQConstant.MULTI_LOG, log);
	}
}
