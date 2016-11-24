# Source Code Crawler

Source Code Crawler is a demo application to test concurrency.
Crawler walks project directory and builds inverted index of superclasses to it's descendants.

It includes following modules:

Step0 - counts word occurrences in file

Step1 - single threaded version

Step2 - each file is indexed in separate thread, each thread prints it's local index

Step3 - each file is indexed in separate thread, shared index is guarded by lock

Step4 - ```ExecutorService``` is used

Step5 - map and reduce threads are separated by queues

Step6 - Apache Hadoop MapReduce library is used

Step7 - Akka library is used

## Build

```
./gradlew clean build
```

## Run

```
./gradlew :source-code-crawler-step1:run
```

where `source-code-crawler-step1` is step module name.

## License

Copyright (C) 2016 Pavel Prokhorov (pavelvpster@gmail.com)


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
