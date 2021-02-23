# MultiDeviceDemo

#### Description
**multidevice playing cropped videos**

Supporting for playing cropped videos on multiple devices and sync playing time.

#### Software Architecture
![](examples/progress.png)

#### Core principles

**Adjusting time**

Assume that time offset between two devices is T, network delay is TTL. And now, device A's time is TA1, device B's time is TB1; playing progress is P.

1. A send a commond
2. B accepted it and resend one
3. A take resended one，and now time is TA2
```
TA2-TA1 = 2TTL
TTL = (TA2-TA1)/2
T= TA2-(TB + TTL)
```
when TTL less than 50ms, we could make sure that the absolute offset within two devices is less than 50ms.

**dispatching**

1. device A send dispatching commond [P]
2. when device B take that, TTL = TB + T - TA，and the proper progress for now is P2 = P + TTL


#### Installation


````gradle
allprojects {
    repositories {
    	...
    	maven { url 'https://jitpack.io' }
    }
}
````
````gradle
dependencies {
    implementation 'com.github.nigalinson:VideoSync:1.0.0-alpha'
}
````

#### Instructions

```java
 SyncCenter.startSync(
    this,
    //LAN group name
    "9998877",
    //device name
    "dev-" + Utils.random().nextInt(127),
    //playing source
    new SyncSource(SyncSource.SourceType.Image.code, Test.getNana())
);

```

```java
SyncCenter.stopSync();
```


#### Contribution

1.  Fork the repository
2.  Create Feat_xxx branch
3.  Commit your code
4.  Create Pull Request
