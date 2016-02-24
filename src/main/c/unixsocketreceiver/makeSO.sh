#!/usr/bin/env bash

cd ../../java
javac -cp /home/mark/Programming/PSE/jdom/*:/home/mark/Programming/PSE/log4j/*:/home/mark/Programming/PSE/Jung/*:../../../../target*:.:* edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/UnixSocketReceiver.java
javah -d ../c/unixsocketreceiver edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver
cd ../c/unixsocketreceiver
gcc -I"$JAVA_HOME/include" -I "$JAVA_HOME/include/linux/" -shared -o libtruffleReceiver.so edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.c -fPIC -lrt
rm ../../../../libtruffleReceiver.so
mv libtruffleReceiver.so ../../../../libtruffleReceiver.so