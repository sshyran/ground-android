/*
 * Copyright 2020 Google LLC
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

package com.google.android.gnd.rx.annotations;

import static java.lang.annotation.ElementType.TYPE_USE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * Denotes a cold observable. Unlike {@link Hot} observables, cold observables have side effects
 * when subscribed to. Typically, this includes the creation of a new producer on each subscription.
 * This interpretation has several consequences:
 *
 * <ul>
 *   <li>Cold observables are "lazy", since no actual work is done before the producer is created on
 *       subscription.
 *   <li>Items are only pushed to the stream when observers subscribe; as such, no items can be
 *       missed as the result of subscribing "too late".
 *   <li>The results generated by any two producers may differ, so observers are not guaranteed to
 *       receive the same sequence of items. As such, cold observables are not suitable for sharing
 *       the same sequence with multiple observers (<i>multicasting</i>). To allow multicasting,
 *       RxJava2 cold observables can be turned into hot connectable observers using the respective
 *       observable class' <code>share()</code> and <code>connect()</code> methods.
 * </ul>
 *
 * <p>Cold RxJava2 observables can be created by passing a producer that generates results to the
 * observable's <code>create()</code> method.
 *
 * <p>See ReactiveX <a href="http://reactivex.io/documentation/observable.html">Observable</</a> and
 * <a href="https://github.com/ReactiveX/RxJava/blob/2.x/DESIGN.md">RxJava v2 Design</a> for
 * details.
 */
@Documented
@Target({TYPE_USE})
public @interface Cold {
  /**
   * Indicates whether or not the observable is expected to terminate (<code>finite=true</code>), or
   * whether it is expected to run indefinitely <code>finite=false</code>. Cold observables are
   * considered finite by default.
   */
  boolean finite() default true;

  /**
   * Indicates whether the whole sequence must be received for its output to be useful (stateful),
   * or whether values are independent (stateless). Stateful observables require the producer and
   * observer to maintain state, while stateless observables allow observers to use idempotent
   * <code>onNext()</code> handlers.
   */
  boolean stateful() default false;
}
