package com.mamboflix.model.managedevice

import com.mamboflix.model.CreateWatchUserModel

class ManageDeviceModel(
    var devices: ArrayList<DeviceModel>,
    var sub_users: ArrayList<CreateWatchUserModel>
)