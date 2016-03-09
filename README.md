# TruffleHog

[![Build Status](https://travis-ci.org/TruffleHog/TruffleHog.svg?branch=master)](https://travis-ci.org/TruffleHog/TruffleHog) [![Coverage Status](https://coveralls.io/repos/github/TruffleHog/TruffleHog/badge.svg)](https://coveralls.io/github/TruffleHog/TruffleHog)

This program requires a modified version of Snort including a PROFINET-preprocessor in order to collect so called Truffles representing a semantic analysis of one PROFINET-network package. 

It keeps track of all incoming Truffles, uses the semantic information the build a network topology (or rather a network map) and displays it in a nice way to look at in quasi real time. Some more advanced feature like replaying network changes, applying filters to network participants and alerts and therefor notifications are supposed appear in some later version of the program. 

This project was started as a university project at the KIT (Karlsruhe Institute of Technology) and will actively be maintained until summer 2016. 
