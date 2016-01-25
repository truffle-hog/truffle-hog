#!/usr/bin/env bash

# Run this from the doc directory

sudo javadoc -docletpath TeXDoclet.jar -doclet org.stfm.texdoclet.TeXDoclet -sourcepath ./src/main/java -subpackages .
