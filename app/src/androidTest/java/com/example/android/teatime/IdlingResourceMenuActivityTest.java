/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.teatime;



import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;


import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Usually Espresso syncs all view operations with the UI thread as well as AsyncTasks, but it can't
 * do so with custom resources (e.g. activity or service). For such cases, we can register the
 * custom resource and Espresso will wait for the resource to be idle before
 * executing a view operation.
 *
 * In this example, we simulate an idling situation. This test is the same as the
 * MenuActivityScreenTest but with an Idling Resource to help with synchronization.
 *
 * We added an idling period from when the user clicks on a GridView item
 * in MenuActivity to when corresponding order activity appears. This is to simulate potential
 * delay that could happen if this data were being retrieved from the web. Without registering the
 * custom resources, this test would fail because the test would proceed without waiting
 * for the Idling Resource.
 */


@RunWith(AndroidJUnit4.class)
public class IdlingResourceMenuActivityTest<IdlingResource> {

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested, MenuActivity in this case, will be launched
     * before each test that's annotated with @Test and before methods annotated with @Before.
     *
     * The activity will be terminated after the test and methods annotated with @After are
     * complete. This rule allows you to directly access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    private androidx.test.espresso.IdlingResource mIdlingResource;


    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void idlingResourceTest() {
        onData(anything()).inAdapterView(withId(R.id.tea_grid_view)).atPosition(0).perform(click());
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}