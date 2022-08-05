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

package com.google.android.ground.persistence.remote.firestore.schema

/** User details nested for nesting inside entities for audit purposes.  */
data class UserNestedObject(
    val id: String? = null,
    val email: String? = null,
    val displayName: String? = null
) {
    companion object {
        /** Fallback value when reading invalid or legacy schemas.  */
        @JvmField
        val UNKNOWN_USER = UserNestedObject("", "", "Unknown user")
    }
}
