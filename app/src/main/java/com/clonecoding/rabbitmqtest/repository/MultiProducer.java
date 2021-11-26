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
public class MultiProducer {

	// 서버 연결
	private Connection connection;

	// 채널
	private Channel channel;

	private MultiProducer() {
	}

	private static class LazyHolder {

		private static final MultiProducer INSTANCE = new MultiProducer();
	}

	/**
	 * 싱글턴 인스턴스
	 *
	 * @return 싱글턴 인스턴스
	 */
	public static MultiProducer getInstance() {

		return MultiProducer.LazyHolder.INSTANCE;
	}

	public void connection() {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try {
			// 서버 연결
			this.connection = factory.newConnection();

			// 채널 생성
			this.channel = this.connection.createChannel();

			channel.exchangeDeclare(RBMQConstant.MULTI_EXCHANGE_NAME, RBMQConstant.MULTI_EXCHANGE_TYPE);

			this.channel.basicPublish(
					RBMQConstant.MULTI_EXCHANGE_NAME,
					RBMQConstant.ROUTING_KEY_1, null,
					RBMQConstant.CONTENT_1.getBytes());
			this.channel.basicPublish(
					RBMQConstant.MULTI_EXCHANGE_NAME,
					RBMQConstant.ROUTING_KEY_2, null,
					RBMQConstant.CONTENT_2.getBytes());
			this.channel.basicPublish(
					RBMQConstant.MULTI_EXCHANGE_NAME,
					RBMQConstant.ROUTING_KEY_3, null,
					RBMQConstant.CONTENT_3.getBytes());

			Log.d(RBMQConstant.MULTI_LOG, "RBMQ Sent Queue 1:" + RBMQConstant.CONTENT_1);
			Log.d(RBMQConstant.MULTI_LOG, "RBMQ Sent Queue 2:" + RBMQConstant.CONTENT_2);
			Log.d(RBMQConstant.MULTI_LOG, "RBMQ Sent Queue 3:" + RBMQConstant.CONTENT_3);
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
