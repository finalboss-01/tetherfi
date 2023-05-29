/*
 * Copyright 2023 pyamsoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.tetherfi.server.widi

import android.content.Context
import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.core.ThreadEnforcer
import com.pyamsoft.tetherfi.core.AppDevEnvironment
import com.pyamsoft.tetherfi.core.InAppRatingPreferences
import com.pyamsoft.tetherfi.server.ServerInternalApi
import com.pyamsoft.tetherfi.server.event.ServerShutdownEvent
import com.pyamsoft.tetherfi.server.permission.PermissionGuard
import com.pyamsoft.tetherfi.server.proxy.SharedProxy
import com.pyamsoft.tetherfi.server.status.RunningStatus
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

@Singleton
internal class WiDiNetworkImpl
@Inject
internal constructor(
    private val inAppRatingPreferences: InAppRatingPreferences,
    @ServerInternalApi private val dispatcher: CoroutineDispatcher,
    @ServerInternalApi private val proxy: SharedProxy,
    @ServerInternalApi config: WiDiConfig,
    enforcer: ThreadEnforcer,
    shutdownBus: EventBus<ServerShutdownEvent>,
    context: Context,
    permissionGuard: PermissionGuard,
    status: WiDiStatus,
    appEnvironment: AppDevEnvironment,
) :
    WifiDirectNetwork(
        dispatcher,
        shutdownBus,
        context,
        permissionGuard,
        config,
        appEnvironment,
        enforcer,
        status,
    ) {

  override fun onNetworkStarted() {
    proxy.start()

    inAppRatingPreferences.markHotspotUsed()
  }

  override fun onNetworkStopped(clearErrorStatus: Boolean) {
    proxy.stop(clearErrorStatus)
  }

  override fun onProxyStatusChanged(): Flow<RunningStatus> {
    return proxy.onStatusChanged()
  }

  override fun getCurrentStatus(): RunningStatus {
    val ws = super.getCurrentStatus()
    val ps = proxy.getCurrentStatus()

    // The network is an error if either are errors
    return if (ws is RunningStatus.Error) ws else if (ps is RunningStatus.Error) ps else ws
  }
}
