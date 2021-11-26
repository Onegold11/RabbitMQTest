package com.clonecoding.rabbitmqtest.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clonecoding.rabbitmqtest.Constant.RBMQConstant;
import com.clonecoding.rabbitmqtest.R;
import com.clonecoding.rabbitmqtest.databinding.ActivityMainBinding;
import com.clonecoding.rabbitmqtest.viewmodel.MainViewModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * 메인 액티비티
 */
public class MainActivity extends AppCompatActivity {

	// 뷰 모델
	private MainViewModel viewModel;

	// 데이터 바인딩
	private ActivityMainBinding binding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initViewModel();
		initDataBinding();
	}

	/**
	 * 뷰 모델 초기화
	 */
	private void initViewModel() {

		this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);
	}

	/**
	 * 데이터 바인딩 초기화
	 */
	private void initDataBinding() {

		this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		this.binding.setLifecycleOwner(this);
		this.binding.setViewmodel(this.viewModel);

		this.binding.nextButton.setOnClickListener(view -> {

			Intent intent = new Intent(this, SubActivity.class);
			startActivity(intent);
		});

		this.binding.simpleButton.setOnClickListener(view -> {

			// 테스트용 RBMQ 메시지 받기
			this.viewModel.startSimpleRBMQ();
		});

		this.binding.exchangeButton.setOnClickListener(view -> {

			this.viewModel.startExchangeRBMQ();
		});
	}
}