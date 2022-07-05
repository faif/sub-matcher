# sub-matcher

A simple program that renames a subtitle to match the video name, and moves it to the same dir. I got tired
of doing this manually so I decided to make it a hobby project. Plus, it helps me to learn Clojure :)

## Installation

Download the standalone Java Archive (JAR) file from this repository. A Java Runtime Environment (JRE) is
required.

## Usage

The only acceptable input argument at the moment is a directory path to search for videos and subtitles.

    $ java -jar sub-matcher-0.1.0-standalone.jar [DIR]

The supported hierarchy is /dir/[VIDEO_FILE] /dir/[VIDEO_FILE]/[SUB_DIR]/[SUB_FILE], where:
* VIDEO_FILE ends with .avi, .mkv, mp4, .mpeg, or .mpg
* SUB_DIR can have any name
* SUB_FILE ends with .sub, .srt, or .txt

## Options

No options supported

## Examples

java -jar sub-matcher-0.1.0-standalone.jar /tmp

### Bugs

When there are > 1 subtitle files in SUB_DIR, the program fails. Need to decide what to do, maybe consider only
the first subtitle file.

### TODO

Add tests, preferably using spec
Add logging and a verbosity flag
Maybe add a minimal GUI

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
