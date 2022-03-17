# FireLine Maven Plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.jiangxincode/fireline-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.jiangxincode/fireline-maven-plugin)

`fireline-maven-plugin` is a maven plugin for `FireLine`. You can visit <http://magic.360.cn/> for further information of `FireLine`.

## How to Use

Add the below content to your `<project><reporting><plugins>` node of pom.xml. You can find latest `${fireline-maven-plugin-version}` from <https://search.maven.org/>

```xml
    <plugin>
        <groupId>io.github.jiangxincode</groupId>
        <artifactId>fireline-maven-plugin</artifactId>
        <version>${fireline-maven-plugin-version}</version>
    </plugin>
```

Run `mvn clean site`

You can see the example from my another project:
<https://jiangxincode.github.io/ApkToolBoxGUI/testReport.html>

## License

* Apache License V2.0 http://www.apache.org/licenses/LICENSE-2.0