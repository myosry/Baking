package com.yosri.mustafa.eng.bakingapp;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yosri.mustafa.eng.baking.R;
import com.yosri.mustafa.eng.bakingapp.Ui.MainActivity;
import com.yosri.mustafa.eng.bakingapp.Ui.RecipeDetails;
import com.yosri.mustafa.eng.bakingapp.Ui.RecipeDetailsFragment;

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
public class ReipeDetailsTest {
    public static final String Ingredient_NAME = "Graham Cracker crumbs";
    public static final String STEP_NAME_SHORT_DESCRIPTION = "Recipe Introduction";
    @Rule
    public ActivityTestRule<RecipeDetails> mainActivityActivityTestRule =
            new ActivityTestRule<>(RecipeDetails.class);

    @Test
    public void clickRecycleViewIngredient() {
        onData(anything()).inAdapterView(withId(R.id.rv_ingredient)).atPosition(0).perform(click());

        onView(withId(R.id.ingredient)).check(matches(withText(Ingredient_NAME)));

    }

    @Test
    public void clickRecycleViewStep_toOpenStepDetails() {
        onData(anything()).inAdapterView(withId(R.id.rv_steps)).atPosition(0).perform(click());

        onView(withId(R.id.short_description)).check(matches(withText(STEP_NAME_SHORT_DESCRIPTION)));

    }
}