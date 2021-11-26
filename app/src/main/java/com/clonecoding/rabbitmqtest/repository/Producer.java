package com.clonecoding.rabbitmqtest.repository;

import android.util.Log;

import com.clonecoding.rabbitmqtest.Constant.RBMQConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * RabbitMQ repository
 */
public class Producer {

	// 서버 연결
	private Connection connection;

	// 채널
	private Channel channel;

	private Producer() {
	}

	private static class LazyHolder {

		private static final Producer INSTANCE = new Producer();
	}

	/**
	 * 싱글턴 인스턴스
	 *
	 * @return 싱글턴 인스턴스
	 */
	public static Producer getInstance() {

		return Producer.LazyHolder.INSTANCE;
	}

	/**
	 * RabbitMQ 연결
	 */
	public void ExchangeConnection() {

		this.close();

		// 서버 설정
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try (Connection connection = factory.newConnection();
		     Channel channel = connection.createChannel()) {

			channel.queueDeclare(RBMQConstant.QUEUE_NAME, false, false, false, null);

			// Exchange 선언
			channel.exchangeDeclare(RBMQConstant.EXCHANGE_NAME, RBMQConstant.EXCHANGE_TYPE);

			String message = "Exchange Connection";
			channel.basicPublish("", RBMQConstant.QUEUE_NAME, null, message.getBytes());
			Log.d("RBMQ [x] Sent :", message);
		} catch (Exception e) {

			close();
		}
	}

	public void connection() {

		this.close();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try {
			// 서버 연결
			this.connection = factory.newConnection();

			// 채널 생성
			this.channel = this.connection.createChannel();

			// 큐 bind
			this.channel.queueDeclare(
					RBMQConstant.QUEUE_NAME, false, false, false, null
			);

			String message = "Simple Connection";
			//this.channel.basicPublish("", RBMQConstant.QUEUE_NAME, null, message.getBytes());
			Log.d("RBMQ [x] Sent :", message);
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

		Log.e(Producer.class.getSimpleName(), log);
	}
}
