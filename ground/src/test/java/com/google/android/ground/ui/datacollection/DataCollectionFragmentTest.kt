/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.ground.ui.datacollection

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.ground.BaseHiltTest
import com.google.android.ground.R
import com.google.android.ground.launchFragmentInHiltContainer
import com.google.android.ground.repository.SubmissionRepository
import com.google.android.ground.ui.common.Navigator
import com.google.common.truth.Truth.assertThat
import com.sharedtest.FakeData.JOB
import com.sharedtest.FakeData.LOCATION_OF_INTEREST
import com.sharedtest.FakeData.SUBMISSION
import com.sharedtest.FakeData.SURVEY
import com.sharedtest.FakeData.TASK_1_NAME
import com.sharedtest.FakeData.TASK_2_NAME
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import io.reactivex.Single
import javax.inject.Inject
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowToast

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
class DataCollectionFragmentTest : BaseHiltTest() {

  @Inject lateinit var navigator: Navigator
  @BindValue @Mock lateinit var surveyRepository: SubmissionRepository
  lateinit var fragment: DataCollectionFragment

  @Before
  override fun setUp() {
    super.setUp()

    whenever(surveyRepository.createSubmission(SURVEY.id, LOCATION_OF_INTEREST.id, SUBMISSION.id))
      .thenReturn(Single.just(SUBMISSION))
  }

  @Test
  fun created_submissionIsLoaded_loiNameIsShown() {
    setupFragment()

    onView(withText(LOCATION_OF_INTEREST.caption)).check(matches(isDisplayed()))
  }

  @Test
  fun created_submissionIsLoaded_jobNameIsShown() {
    setupFragment()

    onView(withText(JOB.name)).check(matches(isDisplayed()))
  }

  @Test
  fun created_submissionIsLoaded_viewPagerAdapterIsSet() {
    setupFragment()

    onView(withId(R.id.pager)).check(matches(isDisplayed()))
  }

  @Test
  fun created_submissionIsLoaded_firstTaskIsShown() {
    setupFragment()

    onView(withText(TASK_1_NAME)).check(matches(isDisplayed()))
  }

  @Test
  fun onContinueClicked_noUserInput_toastIsShown() {
    setupFragment()

    onView(withId(R.id.data_collection_continue_button)).perform(click())

    assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("This field is required")
    onView(withText(TASK_1_NAME)).check(matches(isDisplayed()))
    onView(withText(TASK_2_NAME)).check(matches(not(isDisplayed())))
  }

  @Test
  fun onContinueClicked_newTaskIsShown() {
    setupFragment()
    onView(allOf(withId(R.id.user_response_text), isDisplayed())).perform(typeText("user input"))

    onView(withId(R.id.data_collection_continue_button)).perform(click())

    assertThat(ShadowToast.shownToastCount()).isEqualTo(0)
    onView(withText(TASK_1_NAME)).check(matches(not(isDisplayed())))
    onView(withText(TASK_2_NAME)).check(matches(isDisplayed()))
  }

  @Test
  fun onContinueClicked_thenOnBack_initialTaskIsShown() {
    setupFragment()
    onView(allOf(withId(R.id.user_response_text), isDisplayed())).perform(typeText("user input"))
    onView(withId(R.id.data_collection_continue_button)).perform(click())
    onView(withText(TASK_1_NAME)).check(matches(not(isDisplayed())))
    onView(withText(TASK_2_NAME)).check(matches(isDisplayed()))

    assertThat(fragment.onBack()).isTrue()

    assertThat(ShadowToast.shownToastCount()).isEqualTo(0)
    onView(withText(TASK_1_NAME)).check(matches(isDisplayed()))
    onView(withText(TASK_2_NAME)).check(matches(not(isDisplayed())))
  }

  @Test
  fun onBack_firstViewPagerItem_returnsFalse() {
    setupFragment()

    assertThat(fragment.onBack()).isFalse()
  }

  private fun setupFragment() {
    val argsBundle =
      DataCollectionFragmentArgs.Builder(SURVEY.id, LOCATION_OF_INTEREST.id, SUBMISSION.id)
        .build()
        .toBundle()

    launchFragmentInHiltContainer<DataCollectionFragment>(argsBundle) {
      fragment = this as DataCollectionFragment
    }
  }
}
