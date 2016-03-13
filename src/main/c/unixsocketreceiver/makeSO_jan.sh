#!/usr/bin/env bash

cd ../../java
javac -cp /home/jan/Projects/PSE/apache-log4j-2.5-bin/*:/home/jan/Projects/PSE/jung2-2_0_1/*:/home/jan/Projects/PSE/jdom2-2.0.6/*:../../../../target*:.:*:/home/jan/Projects/PSE/commons-lang3-3.4/*:/home/jan/Projects/PSE/commons-collections4-4.1/* edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/UnixSocketReceiver.java
javah -d ../c/unixsocketreceiver edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver
cd ../c/unixsocketreceiver
gcc -I"$JAVA_HOME/include" -I "$JAVA_HOME/include/linux/" -shared -o libtruffleReceiver.so edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.c -fPIC -lrt
rm ../../../../libtruffleReceiver.so
mv libtruffleReceiver.so ../../../../libtruffleReceiver.so
