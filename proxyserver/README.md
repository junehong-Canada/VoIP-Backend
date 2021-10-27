# IntelliJ Configuration

1. Add Configuration for running
- Edit Run/Debug Configuration -> Add new... -> Gradle -> Name proxyserver, Run: bootRun

2. Add Configuration for deployment
- Go to Gradle tap -> proxyserver -> Tasks -> build -> bootJar, Run bootJar by double-clicking.
- proxyserver/build/libs/proxyserver-0.0.1-SNAPSHOT.jar
