# Contributions Analysis

## Table of Contents
1. [How To Compile](README.md#howtocompile)
2. [How to Test](README.md#howtotest)
3. [How to Run](README.md#howtorun)
4. [How to Package into a jar](README.md#howtopackage)
5. [How to run a packaged jar](README.md#howtopackagerun)
6. [Authors](README.md#authors)

Possible resolution for problem regarding the contributions for campaigns

### How To Compile
Please do configure scala version in build.sbt according to yours
```
$ cd to the project file
$ sbt compile
```

### How to Test

```
$ sbt test
```

### How to Run

```
$ sbt "run pathTocontributionsFile pathToPercetileFile pathToOutputFile"
```

Example
```
$ sbt "run data/test_1/input/itcont.txt data/test_1/input/percentile.txt out.txt"
```

### How to Package into a jar

```
$ sbt package
```

### How to run a packaged jar

```
$ scala /pathToJarFile.jar pathTocontributionsFile pathToPercetileFile pathToOutputFile
```

Example
```
$ scala target/scala-2.11/donation-analytics_2.11-0.1.jar data/test_1/input/itcont.txt data/test_1/input/percentile.txt data/test_1/input/out.txt
```

### Authors

* **Nuno Tavares** - [NunoTavares](nunoduartetavares.com)