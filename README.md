# 小黑车

## 小黑车 LeanCloud 数据库结构

用户表（也可称为 ThumbUp 表）：
```
// 用户根据设备号
{
    "deviceId": "hahaha",
    "thumbUp": [
        {
            "bikeId": "123456",
            "upDeviceId": "hahaha"
        },
        {
            "bikeId": "123457",
            "upDeviceId": "hahaha"
        }
    ]
}
```

自行车主表(Bike)：
```
// 自行车 Id 和 密码（主要显示）
{
    "bikeId": "123456",
    "password": "123456",
    "upDeviceId": "hahaha",
    "vote": 2
}
```
