package com.clonecoding.rabbitmqtest.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.clonecoding.rabbitmqtest.constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.data.RBMQConnection;
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

	// 텍스트
	public MutableLiveData<String> text;

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

		// 서버 설정
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try (
				Connection connection = factory.newConnection();
				Channel channel = connection.createChannel()
		) {

			channel.queueDeclare(RBMQConstant.QUEUE_NAME, false, false, false, null);

			// Exchange 선언
			channel.exchangeDeclare(RBMQConstant.EXCHANGE_NAME, RBMQConstant.EXCHANGE_TYPE);

			String message = "Exchange Connection";
			channel.basicPublish("", RBMQConstant.QUEUE_NAME, null, message.getBytes());
			Log.d("RBMQ [x] Sent :", message);
		} catch (Exception e) {

		}
	}

	public void connection(RBMQConnection conn) {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setUsername(conn.user);
		factory.setPassword(conn.password);
		factory.setVirtualHost(RBMQConstant.VIRTUAL_HOST);
		factory.setHost(conn.ip);
		factory.setPort(conn.port);

		try {
			// 서버 연결
			this.connection = factory.newConnection();

			// 채널 생성
			this.channel = this.connection.createChannel();

			// 큐 bind
			this.channel.queueDeclare(
					conn.queue, false, false, false, null
			);

			if (text != null) {
				text.postValue(text.getValue() + "\nProducer Connect");
			}
		} catch (Exception e) {

			e.printStackTrace();
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

				this.showErrorLog("연결 해제");if (text != null) {
					text.postValue(text.getValue() + "\nProducer connection close");
				}
			} catch (Exception e) {

				this.showErrorLog("연결 해제 실패" + e.toString());
				if (text != null) {
					text.postValue(text.getValue() + "\n" + e.toString());
				}
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
