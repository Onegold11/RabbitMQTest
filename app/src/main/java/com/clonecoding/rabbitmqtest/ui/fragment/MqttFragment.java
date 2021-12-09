package com.clonecoding.rabbitmqtest.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clonecoding.rabbitmqtest.R;
import com.clonecoding.rabbitmqtest.databinding.FragmentMqttBinding;
import com.clonecoding.rabbitmqtest.viewmodel.MainViewModel;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.Objects;

public class MqttFragment extends Fragment {

	/**
	 * binding
	 */
	private FragmentMqttBinding binding;

	/**
	 * 뷰모델
	 */
	private MainViewModel viewModel;
	
	/**
	 * MQTT android client
	 */
	private MqttAndroidClient mqttAndroidClient;

	/**
	 * Action callback
	 */
	private final IMqttActionListener mqttActionListener = new IMqttActionListener() {
		@Override
		public void onSuccess(IMqttToken asyncActionToken) {

			appendLog("MQTT Connection success");
		}

		@Override
		public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

			appendLog("MQTT Connection fail");
		}
	};

	/**
	 * Mqtt callback
	 */
	private final MqttCallback mqttCallback = new MqttCallback() {
		@Override
		public void connectionLost(Throwable cause) {

			appendLog("MQTT Connection lost ");
		}

		@Override
		public void messageArrived(String topic, MqttMessage message) throws Exception {

			appendLog("MQTT Receive message : " + topic + "-" + message.toString());
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {

			appendLog("MQTT Delivery complete");
		}
	};

	/**
	 * 생성자
	 */
	public MqttFragment() { super(); }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		 this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mqtt, container, false);

		 return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.binding.mqttConnectButton.setOnClickListener(v -> connect());
		this.binding.mqttDisconnectButton.setOnClickListener(v -> disconnect());
		this.binding.mqttSubscribeButton.setOnClickListener(v -> subscribe());
		this.binding.mqttUnsubscribeButton.setOnClickListener(v -> unsubscribe());
		this.binding.mqttPublishButton.setOnClickListener(v -> publish());
		this.binding.contentText.setMovementMethod(new ScrollingMovementMethod());
		this.binding.setViewmodel(viewModel);

		this.viewModel.text.observe(requireActivity(),
				this.binding.contentText::setText);
	}

	/**
	 * MQTT 연결
	 */
	public void connect() {
		updateViewModel();

		mqttAndroidClient = new MqttAndroidClient(
				getContext(),
				"tcp://" + viewModel.ip.getValue() + ":" + viewModel.port.getValue(),
				viewModel.user.getValue()
		);
		mqttAndroidClient.setCallback(this.mqttCallback);

		MqttConnectOptions options = new MqttConnectOptions();
		options.setUserName(viewModel.user.getValue());
		options.setPassword(Objects.requireNonNull(viewModel.password.getValue()).toCharArray());

		this.appendLog(
				"URI : " + mqttAndroidClient.getServerURI() + "\n" +
				"Client ID : " + mqttAndroidClient.getClientId() + "\n" +
				"User name : " + options.getUserName() + "\n" +
				"Password : " + Arrays.toString(options.getPassword()) + "\n"
		);

		// connect
		try {
			mqttAndroidClient.connect(options, null, this.mqttActionListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * MQTT 구독
	 */
	public void subscribe() {
		updateViewModel();

		this.appendLog("Subscribe topic : " + viewModel.topic.getValue());

		try {
			mqttAndroidClient.subscribe(viewModel.topic.getValue(), 1, null, this.mqttActionListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * MQTT 구독 해제
	 */
	public void unsubscribe() {
		updateViewModel();

		this.appendLog("Unsubscribe topic");
		try {
			mqttAndroidClient.unsubscribe(viewModel.topic.getValue(), null, this.mqttActionListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * MQTT 메시지 발행
	 */
	public void publish() {
		updateViewModel();

		try {

			MqttMessage message = new MqttMessage();
			message.setPayload(Objects.requireNonNull(viewModel.msg.getValue()).getBytes());
			message.setQos(1);
			message.setRetained(false);

			this.appendLog("Publish : " + Arrays.toString(message.getPayload()));
			mqttAndroidClient.publish(viewModel.topic.getValue(), message, null, this.mqttActionListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * MQTT 연결 해제
	 */
	public void disconnect() {
		updateViewModel();
		this.appendLog("Disconnect");
		try {
			mqttAndroidClient.disconnect(null, this.mqttActionListener);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 텍스트 뷰에 표시할 로그 내용을 추가
	 *
	 * @param log
	 *      log
	 */
	public void appendLog(String log) {

		if (viewModel == null || viewModel.text == null) { return; }

		viewModel.text.setValue(viewModel.text.getValue() + "\n" + log);
	}

	private void updateViewModel() {

		this.viewModel.ip.setValue(this.binding.ipTextview.getText().toString());
		this.viewModel.port.setValue(Integer.parseInt(this.binding.portTextview.getText().toString()));
		this.viewModel.user.setValue(this.binding.userTextview.getText().toString());
		this.viewModel.password.setValue(this.binding.passTextview.getText().toString());
		this.viewModel.topic.setValue(this.binding.topicTextview.getText().toString());
		this.viewModel.msg.setValue(this.binding.msgTextview.getText().toString());
	}
}