package com.joymusic.desktop.deviceconnect

import com.joymusic.shared.deviceconnect.DeviceConnectDeviceType
import com.joymusic.shared.deviceconnect.DeviceConnectManager

object DesktopDeviceConnect {
    val manager = DeviceConnectManager(localDeviceType = DeviceConnectDeviceType.DESKTOP)
}
