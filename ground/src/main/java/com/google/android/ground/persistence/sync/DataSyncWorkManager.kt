/*
 * Copyright 2019 Google LLC
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
package com.google.android.ground.persistence.sync

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.google.android.ground.persistence.sync.LocalMutationSyncWorker.Companion.createInputData
import io.reactivex.Completable
import javax.inject.Inject

/** Enqueues data sync work to be done in the background. */
class DataSyncWorkManager @Inject constructor(private val workManager: WorkManager) :
  BaseWorkManager() {
  override val workerClass: Class<LocalMutationSyncWorker>
    get() = LocalMutationSyncWorker::class.java

  /**
   * Enqueues a worker that sends changes made locally to the remote data store once a network
   * connection is available. The returned `Completable` completes immediately as soon as the worker
   * is added to the work queue (not once the sync job completes).
   */
  fun enqueueSyncWorker(locationOfInterestId: String): Completable =
    Completable.fromRunnable { enqueueSyncWorkerInternal(locationOfInterestId) }

  private fun enqueueSyncWorkerInternal(locationOfInterestId: String) {
    // Rather than having running workers monitor the queue for new mutations for their respective
    // locationOfInterestId, we instead queue a new worker on each new mutation. This simplifies the
    // worker
    // implementation and avoids race conditions in the rare event the worker finishes just when new
    // mutations are added to the db.
    val inputData = createInputData(locationOfInterestId)
    workManager.enqueueUniqueWork(
      LocalMutationSyncWorker::class.java.name,
      ExistingWorkPolicy.APPEND,
      buildWorkerRequest(inputData)
    )
  }
}
