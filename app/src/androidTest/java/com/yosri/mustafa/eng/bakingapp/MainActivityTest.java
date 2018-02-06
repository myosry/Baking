package com.yosri.mustafa.eng.bakingapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Ui.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    public static final String Recipe_NAME = "Nutella Pie";
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickRecycleViewItem_OpensRecipeDetailsActivity() {
        onData(anything()).inAdapterView(withId(R.id.rv)).atPosition(0).perform(click());

        onView(withId(R.id.title)).check(matches(withText(Recipe_NAME)));

    }
}