package com.clonecoding.rabbitmqtest.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clonecoding.rabbitmqtest.R;
import com.clonecoding.rabbitmqtest.databinding.FragmentSimpleBinding;
import com.clonecoding.rabbitmqtest.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SimpleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SimpleFragment extends Fragment {

	/**
	 * 바인딩
	 */
	private FragmentSimpleBinding binding;

	/**
	 * 뷰 모델
	 */
	private MainViewModel viewModel;

	/**
	 * 생성자
	 */
	public SimpleFragment() { super(); }

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment SimpleFragment.
	 */
	public static SimpleFragment newInstance() { return new SimpleFragment(); }

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.viewModel = new ViewModelProvider(this).get(MainViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_simple, container, false);

		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.binding.startConsumeButton.setOnClickListener(v -> {
			updateViewModel();
			this.viewModel.startSimpleConsumeRBMQ();
		});
		this.binding.disconnectConsumeButton.setOnClickListener(v -> {
			updateViewModel();
			this.viewModel.close();
		});
		this.binding.setViewmodel(viewModel);

		this.viewModel.text.observe(requireActivity(),
				this.binding.contentText::setText);
	}

	private void updateViewModel() {

		this.viewModel.ip.setValue(this.binding.ipTextview.getText().toString());
		this.viewModel.port.setValue(Integer.parseInt(this.binding.portTextview.getText().toString()));
		this.viewModel.user.setValue(this.binding.userTextview.getText().toString());
		this.viewModel.password.setValue(this.binding.passTextview.getText().toString());
		this.viewModel.queue.setValue(this.binding.queueTextview.getText().toString());
		this.viewModel.exchange.setValue(this.binding.exchangeTextview.getText().toString());
		this.binding.contentText.setMovementMethod(new ScrollingMovementMethod());
	}
}