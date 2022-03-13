# Fireline Maven Plugin

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.jiangxincode/fireline-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.jiangxincode/fireline-maven-plugin)

`fireline-maven-plugin` is a maven plugin for `FireLine`. You can visit <http://magic.360.cn/> for further information of `FireLine`.

## How to Use


```shell
mvn install:install-file -DgroupId="io.github.jiangxincode" -DartifactId=”fireline” -Dversion="1.7.3" -Dpackaging=”jar” -Dfile="D:\Code\Maven\fireline-maven-plugin\lib\fireline_1.7.3.jar"
```

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

## TODO

* Download FireLine automatically
* Make clean build warnings
* Upload to maven center repository
* Integrate into ApkToolBoxGUI