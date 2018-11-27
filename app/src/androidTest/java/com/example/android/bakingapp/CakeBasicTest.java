package com.example.android.bakingapp;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CakeBasicTest {
    public static final String CAKE_NAME = "Brownies";
    public static final String CHOOSE_CAKE = "Please choose a cake";


    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void isTextDisplayed(){
        onView(withText(CHOOSE_CAKE)).check(ViewAssertions.matches(isDisplayed()));
    }

    @Test
    public void clickGridViewItem_OpensOrderActivity() {

        // Uses {@link Espresso#onData(org.hamcrest.Matcher)} to get a reference to a specific
        // rv item and clicks it.
        onView(withId(R.id.rv_cake))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Checks that the click opens with the correct cake
        onView(withId(R.id.cake_name)).check(matches(withText(CAKE_NAME)));

    }

}
