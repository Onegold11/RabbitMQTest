package com.clonecoding.rabbitmqtest.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.clonecoding.rabbitmqtest.repository.Cosumer;
import com.clonecoding.rabbitmqtest.repository.Producer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 메인 액티비티 뷰 모델
 */
public class MainViewModel extends ViewModel {

	public MutableLiveData<String> text = new MutableLiveData<>("Init");

	private final Cosumer consumer = Cosumer.getInstance();

	private final Producer producer = Producer.getInstance();


	/**
	 * 생성자
	 */
	public MainViewModel() {

		consumer.text = this.text;
	}

	public void startSimpleRBMQ() {

		Log.e("RBMQ", "Start Simple RBMQ");

		// Producer 시작
		Executor executor1 = Executors.newSingleThreadExecutor();
		executor1.execute(this.producer::connection);

		// Consumer 시작
		Executor executor2 = Executors.newSingleThreadExecutor();
		executor2.execute(this.consumer::connection);
	}

	public void startExchangeRBMQ() {

		Log.e("RBMQ", "Start Exchange RBMQ");

		// Producer 시작
		Executor executor1 = Executors.newSingleThreadExecutor();
		executor1.execute(this.producer::ExchangeConnection);

		// Consumer 시작
		Executor executor2 = Executors.newSingleThreadExecutor();
		executor2.execute(this.consumer::ExchangeConnection);
	}
}
