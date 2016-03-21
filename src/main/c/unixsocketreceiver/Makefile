# A generic makefile for a Java project.

VERSION_NUMBER := 1.0

# Location of trees.
SOURCE_DIR  := /home/jan/Projects/PSE/TruffleHog/src/main/java
SOURCE_JAVA := /home/jan/Projects/PSE/TruffleHog/src/main/java
SOURCE_NATIVE := /home/jan/Projects/PSE/TruffleHog/src/main/c
OUTPUT_DIR  := classes

# Unix tools
AWK         := awk
FIND        := /usr/bin/find
MKDIR       := mkdir -p
RM          := rm -rf
SHELL       := /bin/bash

# Path to support tools
JAVA_HOME           := /usr/lib/jvm/java-8-oracle
JUNG_HOME           := /home/jan/.m2/repository/net/sf/jung
JUNG_VERSION        := 2.0.2-SNAPSHOT
PSE_HOME            := /home/jan/Projects/PSE
ROOT_TRUFFLEHOG     := $(PSE_HOME)/TruffleHog


CLASS_SOURCE        := $(SOURCE_JAVA)/edu/kit/trufflehog/service/packetdataprocessor/profinetdataprocessor/UnixSocketReceiver.java
CLASS_NAME          := edu.kit.trufflehog.service.packetdataprocessor.profinetdataprocessor.UnixSocketReceiver


# Java tools
JAVA        := $(JAVA_HOME)/bin/java
JAVAC       := $(JAVA_HOME)/bin/javac
JAVAH       := $(JAVA_HOME)/bin/javah

JFLAGS      := -sourcepath $(SOURCE_DIR)        \
               -d $(OUTPUT_DIR)                 \
               -source 1.8

JVMFLAGS    := -ea                              \
               -esa                             \
               -Xfuture

JVM         := $(JAVA) $(JVMFLAGS)

JAR         := $(JAVA_HOME)/bin/jar
JARFLAGS    := cf
CPFLAGS     := cp

JAVADOC     := $(JAVA_HOME)/bin/javadoc
JDFLAGS     := -sourcepath $(SOURCE_DIR)        \
               -d $(OUTPUT_DIR)                 \
               -link http://java.sun.com/products/jdk/1.4/docs/api


# Jars
JUNG_VISUAL           := $(JUNG_HOME)/jung-visualization/$(JUNG_VERSION)/*
JUNG_API              := $(JUNG_HOME)/jung-api/$(JUNG_VERSION)/*
JUNG_ALGO             := $(JUNG_HOME)/jung-algorithms/$(JUNG_VERSION)/*
JUNG_IMPL             := $(JUNG_HOME)/jung-graph-impl/$(JUNG_VERSION)/*

COMMONS_LOGGING       := $(PSE_HOME)/libs/apache-log4j-2.5-bin/*
GUAVA                 := $(PSE_HOME)/libs/guava19/*
COMMONS_COLLECTIONS   := $(PSE_HOME)/libs/commons-collections4-4.1/*
COMMONS_LANG          := $(PSE_HOME)/libs/commons-lang3-3.4/*
JDOM                  := $(PSE_HOME)/libs/jdom2-2.0.6/*

# Set the Java classpath
class_path := JUNG_VISUAL               \
              JUNG_API                	\
              JUNG_ALGO       			\
              JUNG_IMPL                 \
              COMMONS_LOGGING           \
              COMMONS_COLLECTIONS       \
              COMMONS_LANG              \
              GUAVA                     \
              JDOM                      \
			  SOURCE_JAVA

# space - A blank space
space := $(empty) $(empty)

# $(call build-classpath, variable-list)
define build-classpath
$(strip                                         \
  $(patsubst :%,%,                              \
    $(subst : ,:,                               \
      $(strip                                   \
        $(foreach j,$1,$(call get-file,$j):)))))
endef

# $(call get-file, variable-name)
define get-file
  $(strip                                       \
    $($1)                                       \
    $(if $(call file-exists-eval,$1),,          \
      $(warning The file referenced by variable \
                '$1' ($($1)) cannot be found)))
endef

# $(call file-exists-eval, variable-name)
define file-exists-eval
  $(strip                                       \
    $(if $($1),,$(warning '$1' has no value))   \
    $(wildcard $($1)))
endef

# $(call brief-help, makefile)
define brief-help
  $(AWK) '$$1 ~ /^[^.][-A-Za-z0-9]*:/                   \
         { print substr($$1, 1, length($$1)-1) }' $1 |  \
  sort |                                                \
  pr -T -w 80 -4
endef

# $(call file-exists, wildcard-pattern)
file-exists = $(wildcard $1)

# $(call check-file, file-list)
define check-file
  $(foreach f, $1,                              \
    $(if $(call file-exists, $($f)),,           \
      $(warning $f ($($f)) is missing)))
endef

# #(call make-temp-dir, root-opt)
define make-temp-dir
  mktemp -t $(if $1,$1,make).XXXXXXXXXX
endef

# MANIFEST_TEMPLATE - Manifest input to m4 macro processor
MANIFEST_TEMPLATE := src/manifest/manifest.mf
TMP_JAR_DIR       := $(call make-temp-dir)
TMP_MANIFEST      := $(TMP_JAR_DIR)/manifest.mf

# $(call add-manifest, jar, jar-name, manifest-file-opt)
define add-manifest
  $(RM) $(dir $(TMP_MANIFEST))
  $(MKDIR) $(dir $(TMP_MANIFEST))
  m4 --define=NAME="$(notdir $2)"                       \
     --define=IMPL_VERSION=$(VERSION_NUMBER)            \
     --define=SPEC_VERSION=$(VERSION_NUMBER)            \
     $(if $3,$3,$(MANIFEST_TEMPLATE))                   \
     > $(TMP_MANIFEST)
  $(JAR) -ufm $1 $(TMP_MANIFEST)
  $(RM) $(dir $(TMP_MANIFEST))
endef

# $(call make-jar,jar-variable-prefix)
define make-jar
  .PHONY: $1 $$($1_name)
  $1: $($1_name)
  $$($1_name):
        cd $(OUTPUT_DIR); \
        $(JAR) $(JARFLAGS) $$(notdir $$@) $$($1_packages)
        $$(call add-manifest, $$@, $$($1_name), $$($1_manifest))
endef

# Set the CLASSPATH
export CLASSPATH := $(call build-classpath, $(class_path))

# make-directories - Ensure output directory exists.
make-directories := $(shell $(MKDIR) $(OUTPUT_DIR))

# help - The default goal
.PHONY: help
help:
        @$(call brief-help, $(CURDIR)/Makefile)

# all - Perform all tasks for a complete build
.PHONY: all
all: compile jars javadoc

#.PHONY: javac
do-javac:
	$(JAVAC) -cp $(CLASSPATH) $(CLASS_SOURCE)
	# -d $(OUTPUT_DIR)

do-javah:
	$(JAVAH) -d $(SOURCE_NATIVE)/unixsocketreceiver $(CLASS_NAME)

do-native:
	gcc -I$(JAVA_HOME)/include -I $(JAVA_HOME)/include/linux/ -shared -o $(ROOT_TRUFFLEHOG)/libtruffleReceiver.so $(SOURCE_NATIVE)/unixsocketreceiver/edu_kit_trufflehog_service_packetdataprocessor_profinetdataprocessor_UnixSocketReceiver.c -fPIC -lrt

jni: do-javac do-javah do-native


# all_javas - Temp file for holding source file list
all_javas := $(OUTPUT_DIR)/all.javas

# compile - Compile the source
.PHONY: compile
compile: $(all_javas)
	$(JAVAC) $(JFLAGS) @$<

# all_javas - Gather source file list
.INTERMEDIATE: $(all_javas)
$(all_javas):
	$(FIND) $(SOURCE_DIR) -name '*.java' > $@

# jar_list - List of all jars to create
jar_list := server_jar ui_jar

# jars - Create all jars
.PHONY: jars
jars: $(jar_list)

# server_jar - Create the $(server_jar)
server_jar_name     := $(OUTPUT_DIR)/lib/a.jar
server_jar_manifest := src/com/company/manifest/foo.mf
server_jar_packages := com/company/m com/company/n

# ui_jar - create the $(ui_jar)
ui_jar_name     := $(OUTPUT_DIR)/lib/b.jar
ui_jar_manifest := src/com/company/manifest/bar.mf
ui_jar_packages := com/company/o com/company/p

# Create an explicit rule for each jar
# $(foreach j, $(jar_list), $(eval $(call make-jar,$j)))
#$(eval $(call make-jar,server_jar))
#$(eval $(call make-jar,ui_jar))

# javadoc - Generate the Java doc from sources
.PHONY: javadoc
javadoc: $(all_javas)
	$(JAVADOC) $(JDFLAGS) @$<

.PHONY: clean
clean:
	$(RM) $(OUTPUT_DIR)
	$(RM) $(ROOT_TRUFFLEHOG)/libtruffleReceiver.so

.PHONY: classpath
classpath:
	@echo CLASSPATH='$(CLASSPATH)'

.PHONY: check-config
check-config:
	@echo Checking configuration...
	$(call check-file, $(class_path) JAVA_HOME)

.PHONY: print
print:
	$(foreach v, $(V), \
          $(warning $v = $($v)))
