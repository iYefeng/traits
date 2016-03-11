SHELL=/bin/bash

TARGET1=bin/server.exe
TARGET2=bin/client.exe
TARGET1_SRC=src/DsServer_server.cpp 
TARGET2_SRC=src/DsServer_client.cpp

OTHER_SRC = gen-cpp/DsServer.cpp \
						gen-cpp/dss_constants.cpp \
			 			gen-cpp/dss_types.cpp

TARGET1_OBJ=$(patsubst %.cpp, %.o, ${TARGET1_SRC})
TARGET2_OBJ=$(patsubst %.cpp, %.o, ${TARGET2_SRC})
OTHER_OBJ=$(patsubst %.cpp, %.o, ${OTHER_SRC})

CC=gcc
CXX=g++

INC=-I/home/yefeng/include -I/home/yefeng/include/thrift
LIBS=-lssl \
		 -lcrypto \
		 -pthread \
		 -lrt \
		 -lthrift \
		 -lthriftnb \
		 -lthriftz \
		 -levent \
		 -lkyotocabinet \
		 -lz \
		 -lm \
		 -lc
LDFLAGS=-L./
ARFLAGS=
CFLAGS=-Wall $(INC)
CXXFLAGS=

BLACK 	= "\e[30;1m"
RED  		= "\e[31;1m"
GREEN 	= "\e[32;1m"
YELLOW 	= "\e[33;1m"
BLUE  	= "\e[34;1m"
PURPLE 	= "\e[35;1m"
CYAN  	= "\e[36;1m"
WHITE 	= "\e[37;1m"
CLEAR		= "\e[0m"

define print_status
	@echo -e $(BLUE)  $(1) $(YELLOW)"["$(2)"]"$(CLEAR)
endef


.PHONY: all clean thrift_compile

all: ${TARGET1} ${TARGET2}

${TARGET1}: ${TARGET1_OBJ} ${OTHER_OBJ}
	$(call print_status, "build",$@)
	$(CXX) $^ ${LIBS} -o $@

${TARGET2}: ${TARGET2_OBJ} ${OTHER_OBJ}
	$(call print_status, "build",$@)
	$(CXX) $^ ${LIBS} -o $@

thrift_compile: src/dss.thrift
	$(call print_status, "thrift",$@)
	thrift --gen cpp:cob_style -r $<

%.o:%.c
	$(call print_status, "compile",$@)
	$(CC) -c $(CFLAGS) $< -o $@

%.o:%.cc
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS) $< -o $@

%.o:%.cpp
	$(call print_status, "compile",$@)
	$(CXX) -c $(CFLAGS) $(CXXFLAGS)  $< -o $@

clean:
	rm ${TARGET1} ${TARGET1_OBJ} ${TARGET2} ${TARGET2_OBJ} ${OTHER_OBJ}




