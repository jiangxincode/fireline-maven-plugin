# Simian Maven Plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.jiangxincode/simian-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.jiangxincode/simian-maven-plugin)

`simian-maven-plugin` is a maven plugin for `simian`. You can visit <http://www.harukizaemon.com/simian> for further information of `simian`.

There is a `maven-simian-plugin`(http://maven.apache.org/archives/maven-1.x/plugins/simian), however it can be used in maven 1.x only, so I write the `simian-maven-plugin` for maven 2+.

## How to Use

Add the below content to your `<project><reporting><plugins>` node of pom.xml. You can find latest `${simian-maven-plugin-version}` from <https://search.maven.org/>

```xml
    <plugin>
        <groupId>com.github.jiangxincode</groupId>
        <artifactId>simian-maven-plugin</artifactId>
        <version>${simian-maven-plugin-version}</version>
    </plugin>
```

Run `mvn clean site`

You can see the example from my another project:
<https://jiangxincode.github.io/ApkToolBoxGUI/simian-report.html>

## License

* Apache License V2.0 http://www.apache.org/licenses/LICENSE-2.0

*WARN*

`simian-maven-plugin` is a completely free and open-source project but `simian` is not. So you must read the license of it: <http://www.harukizaemon.com/simian/license.pdf>