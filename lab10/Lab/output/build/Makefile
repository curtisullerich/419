# NOTE: This makefile is designed to be run from one level up in the directory
# hierarchy compared to where it resides. For instance, if this makefile is
# located at 'output/build/Makefile', then it can be launched as:
# 'make -f build/Makefile' from the 'output' directory.

ifeq ($(CXX),)
  CXX=g++
endif
PKGCONFIG := /opt/ibm/InfoSphereStreams/bin/dst-pe-pkg-config.sh
PE_INCLUDE := $(shell $(PKGCONFIG) --cflags dst-spl-pe-install)
PE_LIB := $(shell $(PKGCONFIG) --libs dst-spl-pe-install)
PE_STAND_LIB := $(shell $(PKGCONFIG) --libs dst-spl-pe-install-standalone)
SPL_CXXFLAGS = -Wall -Wno-unused -Wno-sign-compare
SPL_CXXFLAGS += -D_REENTRANT -fPIC -finline-functions -fmessage-length=0
SPL_CXXFLAGS += $(PE_INCLUDE) 
SPL_LDFLAGS = $(PE_LIB)

SPL_TYPES = build/type/BeJwrMcw0Tc4vzSsBABAcANt.o 
SPL_FUNCTIONS = 
SPL_OPERATORS = build/operator/Input.o \
                build/operator/Output.o \
                build/operator/Writer.o 
SPL_PE_OBJS = build/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.o 
SPL_PE_BINS = bin/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.dpe 

.PHONY: distclean clean all types functions operators pes svar

all: bin types functions operators pes svar standalone

distclean clean:
	@rm -fr bin/* build/type build/function build/operator build/pe build/svar build/standalone

bin:
	@mkdir -m 775 bin

types: $(SPL_TYPES)

build/type/BeJwrMcw0Tc4vzSsBABAcANt.o: \
	    src/type/BeJwrMcw0Tc4vzSsBABAcANt.h \
	    src/type/BeJwrMcw0Tc4vzSsBABAcANt.cpp \
	    build/cppOptions \
	    | build/type
	@echo " [CXX-type] tuple<int32 count>"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/type/BeJwrMcw0Tc4vzSsBABAcANt.cpp

build/type:
	-@mkdir -m 775 build/type

operators: $(SPL_OPERATORS)

-include build/operator/Input.d
build/operator/Input.o: \
	    src/operator/Input.h \
	    src/operator/Input.cpp\
	    build/dep/operator/Input.dep  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Input"
	@$(CXX) -o $@ -MD -c $(SPL_CXXFLAGS) src/operator/Input.cpp

-include build/operator/Output.d
build/operator/Output.o: \
	    src/operator/Output.h \
	    src/operator/Output.cpp\
	    build/dep/operator/Output.dep  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Output"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/spl/utility/JavaOp/../../../impl/include -I /opt/ibm/java-x86_64-60/include -c $(SPL_CXXFLAGS) src/operator/Output.cpp

-include build/operator/Writer.d
build/operator/Writer.o: \
	    src/operator/Writer.h \
	    src/operator/Writer.cpp\
	    build/dep/operator/Writer.dep  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Writer"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/impl/include -c $(SPL_CXXFLAGS) src/operator/Writer.cpp

build/operator:
	-@mkdir -m 775 -p build/operator

functions: $(SPL_FUNCTIONS)

pes: $(SPL_PE_BINS)

build/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.o: \
	    src/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.h \
	    src/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.cpp  \
	    build/cppOptions \
	    | build/pe
	@echo " [CXX-pe] pe0"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.cpp

PEBeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd = \
	    build/pe/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.o \
	    build/operator/Input.o \
	    build/type/BeJwrMcw0Tc4vzSsBABAcANt.o \
	    build/operator/Output.o \
	    build/operator/Writer.o 

bin/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.dpe: \
	$(PEBeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd) \
	build/dep/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.dep \
	build/ldOptions
	@echo " [LD-pe] pe0"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/BeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd.dpe -fPIC -shared $(PEBeJwz9cwrKC2JN_1MvLQHT4UWZJalF8QBniQjd) -L /opt/ibm/InfoSphereStreams/toolkits/spl/spl/utility/JavaOp/../../../impl/lib -Wl,-rpath -Wl,/opt/ibm/InfoSphereStreams/toolkits/spl/spl/utility/JavaOp/../../../impl/lib -lstreams-stdtk-javaop -L /opt/ibm/java-x86_64-60/jre/lib/amd64/j9vm -Wl,-rpath -Wl,/opt/ibm/java-x86_64-60/jre/lib/amd64/j9vm -ljvm -lpthread -ldl -lstreams-stdtk-runtime -L /opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -Wl,-rpath -Wl,/opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -lstreams-stdtk-runtime $(SPL_LDFLAGS)

build/pe:
	-@mkdir -m 775 build/pe

standalone: bin/standalone 

build/standalone/standalone.o: \
	    src/standalone/standalone.h \
	    src/standalone/standalone.cpp  \
	    build/cppOptions \
	    | build/standalone
	@echo " [CXX-standalone] standalone"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/standalone/standalone.cpp

bin/standalone: build/standalone/standalone.o build/ldOptions
	@echo " [LD-standalone] standalone"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/standalone build/standalone/standalone.o $(PE_STAND_LIB)
	@echo " [LN-standalone] standalone "
	@ln -sf standalone bin/Main
build/standalone:
	-@mkdir -m 775 build/standalone

