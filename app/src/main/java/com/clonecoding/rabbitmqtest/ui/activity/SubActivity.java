package com.clonecoding.rabbitmqtest.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.clonecoding.rabbitmqtest.R;
import com.clonecoding.rabbitmqtest.databinding.ActivityMainBinding;
import com.clonecoding.rabbitmqtest.databinding.ActivitySubBinding;
import com.clonecoding.rabbitmqtest.viewmodel.MainViewModel;
import com.clonecoding.rabbitmqtest.viewmodel.SubViewModel;

public class SubActivity extends AppCompatActivity {

	// 뷰 모델
	private SubViewModel viewModel;

	// 데이터 바인딩
	private ActivitySubBinding binding;

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

		this.viewModel = new ViewModelProvider(this).get(SubViewModel.class);
	}

	/**
	 * 데이터 바인딩 초기화
	 */
	private void initDataBinding() {

		this.binding = DataBindingUtil.setContentView(this, R.layout.activity_sub);
		this.binding.setLifecycleOwner(this);
		this.binding.setViewmodel(this.viewModel);

		this.binding.startButton.setOnClickListener(view -> {

			this.viewModel.startRBMQ();
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.d(SubActivity.class.getSimpleName(), "onDestroy");

		this.viewModel.close();
	}
}