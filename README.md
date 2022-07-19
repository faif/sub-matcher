# sub-matcher

A simple program that renames a subtitle to match the video name, and moves it to the same dir. I got tired
of doing this manually so I decided to make it a hobby project. Plus, it helps me to practice Clojure :)

## Installation

Download the standalone Java Archive (JAR) file from this repository. A Java Runtime Environment (JRE) is
required.

## Usage

The only acceptable input argument at the moment is a directory path to search for videos and subtitles.

    $ java -jar sub-matcher-0.1.0-standalone.jar [DIR]

The supported hierarchy is /dir/[VIDEO_DIR]/[VIDEO_FILE] /dir/[VIDEO_DIR]/[SUB_DIR]/[SUB_FILE], where:
* VIDEO_FILE ends with .avi, .mkv, mp4, .mpeg, or .mpg
* VIDEO_DIR and SUB_DIR must have the same name
* SUB_FILE ends with .sub, .srt, or .txt

## Options

No options supported

## Examples

java -jar sub-matcher-0.1.0-standalone.jar /tmp

### Bugs

The program works only with a specific directory structure. Here's an example:
`/tmp/s01/s01e01/s01e01.mpg, /tmp/s01/s01e01/s1e01/en.srt, /tmp/s01/s01e02/s01e02.avi, /tmp/s01/s01e02/s01e02/en.txt`. Executing the example command line produces `/tmp/s01/s01e01/s01e01.srt, /tmp/s01/s01e02/s01e02.txt`.

### TODO

* Add tests, preferably using spec
* Add logging and a verbosity flag
* Maybe add a minimal GUI

## License

Copyright © 2022 Sakis Kasampalis

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
