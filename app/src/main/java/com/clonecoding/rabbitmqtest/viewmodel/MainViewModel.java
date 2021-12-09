package com.clonecoding.rabbitmqtest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clonecoding.rabbitmqtest.constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.data.RBMQConnection;
import com.clonecoding.rabbitmqtest.repository.Cosumer;
import com.clonecoding.rabbitmqtest.repository.Producer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 메인 액티비티 뷰 모델
 */
public class MainViewModel extends ViewModel {

	// Consumer
	private final Cosumer consumer = Cosumer.getInstance();

	// Producer
	private final Producer producer = Producer.getInstance();

	// Content text
	public MutableLiveData<String> text = new MutableLiveData<>("Init");

	public final MutableLiveData<String> user = new MutableLiveData<>(RBMQConstant.ADMIN_NAME);

	public final MutableLiveData<String> password = new MutableLiveData<>(RBMQConstant.ADMIN_PASS);

	public final MutableLiveData<String> ip = new MutableLiveData<>(RBMQConstant.SERVER_HOST);

	public final MutableLiveData<Integer> port = new MutableLiveData<>(RBMQConstant.SERVER_PORT);

	public final MutableLiveData<String> queue = new MutableLiveData<>(RBMQConstant.QUEUE_NAME);

	public final MutableLiveData<String> exchange = new MutableLiveData<>(RBMQConstant.EXCHANGE_NAME);

	public final MutableLiveData<String> topic = new MutableLiveData<>(RBMQConstant.TOPIC);

	public final MutableLiveData<String> msg = new MutableLiveData<>(RBMQConstant.MQTT_MSG);

	/**
	 * 생성자
	 */
	public MainViewModel() {

		consumer.text = this.text;
		producer.text = this.text;
	}

	public void  startSimpleConsumeRBMQ() {

		Log.i("RBMQ", "Start Simple Consume RBMQ");
		this.text.setValue("Start Simple Consume RBMQ");

		RBMQConnection conn = new RBMQConnection();
		conn.setIp(this.ip.getValue());
		conn.setPort(this.port.getValue());
		conn.setUser(this.user.getValue());
		conn.setPassword(this.password.getValue());
		conn.setQueue(this.queue.getValue());
		conn.setExchange(this.exchange.getValue());

		// Server connect
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(() -> this.producer.connection(conn));

		// Consumer connect
		Executor executor2 = Executors.newSingleThreadExecutor();
		executor2.execute(() -> this.consumer.connection(conn));
	}

	public void close() {

		this.consumer.close();
		this.producer.close();
	}

	@Override
	protected void onCleared() {
		super.onCleared();

		this.close();
	}
}
