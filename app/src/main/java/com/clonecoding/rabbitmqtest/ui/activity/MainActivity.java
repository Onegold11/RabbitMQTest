package com.clonecoding.rabbitmqtest.ui.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.clonecoding.rabbitmqtest.R;
import com.clonecoding.rabbitmqtest.databinding.ActivityMainBinding;
import com.clonecoding.rabbitmqtest.viewmodel.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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

		initView();
	}

	private void initView() {

		initViewModel();

		initDataBinding();

		initTab();
	}

	private void initTab() {

		NavHostFragment navHostFragment =
				(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
		NavController navController = navHostFragment.getNavController();

		this.binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {

				switch (tab.getPosition()) {

					case 0:
						navController.navigate(R.id.action_mqttFragment_to_simpleFragment);
						break;
					case 1:
						navController.navigate(R.id.action_simpleFragment_to_mqttFragment);
						break;
					default:
						break;
				}
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});
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
	}
}