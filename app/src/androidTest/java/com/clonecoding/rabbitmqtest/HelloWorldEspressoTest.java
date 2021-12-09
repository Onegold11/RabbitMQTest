package com.clonecoding.rabbitmqtest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.clonecoding.rabbitmqtest.ui.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HelloWorldEspressoTest {

	@Rule
	public ActivityTestRule<MainActivity> activityActivityTestRule =
			new ActivityTestRule<>(MainActivity.class);

	@Test
	public void listGoesOverTheFold() {

		onView(withText("Hello world!")).check(matches(isDisplayed()));
	}
}
