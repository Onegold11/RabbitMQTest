package com.clonecoding.rabbitmqtest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clonecoding.rabbitmqtest.constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.repository.MultiCosumer;
import com.clonecoding.rabbitmqtest.repository.MultiProducer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 메인 액티비티 뷰 모델
 */
public class SubViewModel extends ViewModel {

	// Queue 1
	public MutableLiveData<String> text1 = new MutableLiveData<>("Init 1");

	// Queue 2
	public MutableLiveData<String> text2 = new MutableLiveData<>("Init 2");

	// Queue 3
	public MutableLiveData<String> text3 = new MutableLiveData<>("Init 3");

	// Recv
	private final MultiCosumer consumer = MultiCosumer.getInstance();

	// Send
	private final MultiProducer producer = MultiProducer.getInstance();

	/**
	 * 생성자
	 */
	public SubViewModel() {

		consumer.text1 = this.text1;
		consumer.text2 = this.text2;
		consumer.text3 = this.text3;
	}

	public void startRBMQ() {

		Log.e(RBMQConstant.MULTI_LOG, "Start Multi Queue RBMQ");

		// Producer 시작
		Executor executor1 = Executors.newSingleThreadExecutor();
		executor1.execute(this.producer::connection);

		// Consumer 시작
		Executor executor2 = Executors.newSingleThreadExecutor();
		executor2.execute(this.consumer::connection);

	}

	public void close() {

		this.producer.close();
		this.consumer.close();
	}
}
