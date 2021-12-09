package com.clonecoding.rabbitmqtest.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.clonecoding.rabbitmqtest.constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.data.RBMQConnection;
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
public class Cosumer {

	// 서버 연결
	private Connection connection;

	// 채널
	private Channel channel;

	// 텍스트
	public MutableLiveData<String> text;

	// 실패 콜백
	CancelCallback cancelCallback = consumerTag -> showErrorLog("Consume cancel 실패 " + consumerTag);

	// 성공 콜백
	DeliverCallback deliverCallback = (consumerTag, message) -> {

		String body = new String(message.getBody(), StandardCharsets.UTF_8);

		Log.d("RBMQ", body);

		if (text != null) {
			text.postValue(text.getValue() + "\n" + body);
		}
	};

	private Cosumer() {
	}

	private static class LazyHolder {

		private static final Cosumer INSTANCE = new Cosumer();
	}

	/**
	 * 싱글턴 인스턴스
	 *
	 * @return 싱글턴 인스턴스
	 */
	public static Cosumer getInstance() {

		return LazyHolder.INSTANCE;
	}

	/**
	 * RabbitMQ 연결
	 */
	public void connection(RBMQConnection conn) {

		// 서버 설정
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
					conn.queue,
					false, false, false, null
			);

			this.channel.basicConsume(
					conn.queue,
					true,
					deliverCallback, cancelCallback
			);

			if (text != null) {
				text.postValue(text.getValue() + "\nConsumer connect");
			}
		} catch (Exception e) {

			if (text != null) {
				text.postValue(text.getValue() + "\n" + e.toString());
			}
			e.printStackTrace();
			close();
		}
	}

	public void ExchangeConnection() {

		close();

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RBMQConstant.SERVER_HOST);

		try {
			// 서버 연결
			this.connection = factory.newConnection();

			// 채널 생성
			this.channel = this.connection.createChannel();

			// Exchange 선언
			channel.exchangeDeclare(RBMQConstant.EXCHANGE_NAME, RBMQConstant.EXCHANGE_TYPE);
			String queueName = channel.queueDeclare().getQueue();

			// 큐 bind
			this.channel.queueBind(queueName, RBMQConstant.EXCHANGE_NAME, "");

			this.channel.basicConsume(
					RBMQConstant.QUEUE_NAME,
					true,
					deliverCallback, cancelCallback
			);
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
				if (text != null) {
					text.postValue(text.getValue() + "\nComsumer connection close");
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

		Log.e(Cosumer.class.getSimpleName(), log);
	}
}
