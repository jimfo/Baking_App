package com.jimfo.baking_app;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.jimfo.baking_app.ui.RecipeDetail;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailTest {

    @Rule
    public ActivityTestRule<RecipeDetail> mActivityTestRule = new ActivityTestRule<>(RecipeDetail.class);

    private static final String STEP_DETAIL = "Recipe Introduction";

    @Test
    public void clickOnDetailActivityCardViewOpensStepActivity() {
        //        onData(anything()).inAdapterView(withId(R.id.step_rv)).atPosition(0).perform(click());
        onView(withId(R.id.step_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.step_description_tv)).check(matches(withText(STEP_DETAIL)));
    }
}
