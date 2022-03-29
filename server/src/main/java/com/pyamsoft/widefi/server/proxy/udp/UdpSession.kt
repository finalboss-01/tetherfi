package com.pyamsoft.widefi.server.proxy.udp

import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.core.Enforcer
import com.pyamsoft.widefi.server.ErrorEvent
import com.pyamsoft.widefi.server.proxy.SharedProxy
import com.pyamsoft.widefi.server.proxy.session.BaseSession
import io.ktor.network.sockets.Datagram
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber

internal class UdpSession(
    private val dispatcher: CoroutineDispatcher,
    private val errorBus: EventBus<ErrorEvent>,
    proxyDebug: Boolean,
) : BaseSession<Datagram>(SharedProxy.Type.UDP, proxyDebug) {

  override suspend fun exchange(proxy: Datagram) {
    Enforcer.assertOffMainThread()

    Timber.d("Attempt data exchange of UDP proxy: ${proxy.address}")
  }
}
