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

SPL_TYPES = build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o \
            build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o 
SPL_FUNCTIONS = 
SPL_OPERATORS = build/operator/Lines.o \
                build/operator/Numbered.o \
                build/operator/Sink.o 
SPL_PE_OBJS = build/pe/BeJwz9cnMSy2OBwAJewKQ.o \
              build/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.o \
              build/pe/BeJwzCc7My44HAAcAAIp.o 
SPL_PE_BINS = bin/BeJwz9cnMSy2OBwAJewKQ.dpe \
              bin/BeJyz8CvNTUotSk2JBwAT_1gPK.dpe \
              bin/BeJwzCc7My44HAAcAAIp.dpe 

.PHONY: distclean clean all types functions operators pes svar

all: bin types functions operators pes svar standalone

distclean clean:
	@rm -fr bin/* build/type build/function build/operator build/pe build/svar build/standalone

bin:
	@mkdir -m 775 bin

types: $(SPL_TYPES)

build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o: \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.cpp \
	    build/cppOptions \
	    | build/type
	@echo " [CXX-type] enum{bin,block,csv,line,txt}"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.cpp

build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o: \
	    src/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h \
	    src/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.cpp \
	    build/cppOptions \
	    | build/type
	@echo " [CXX-type] tuple<rstring contents>"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.cpp

build/type:
	-@mkdir -m 775 build/type

operators: $(SPL_OPERATORS)

-include build/operator/Lines.d
build/operator/Lines.o: \
	    src/operator/Lines.h \
	    src/operator/Lines.cpp\
	    build/dep/operator/Lines.dep \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h \
	    src/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Lines"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/impl/include -c $(SPL_CXXFLAGS) src/operator/Lines.cpp

-include build/operator/Numbered.d
build/operator/Numbered.o: \
	    src/operator/Numbered.h \
	    src/operator/Numbered.cpp\
	    build/dep/operator/Numbered.dep  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Numbered"
	@$(CXX) -o $@ -MD -c $(SPL_CXXFLAGS) src/operator/Numbered.cpp

-include build/operator/Sink.d
build/operator/Sink.o: \
	    src/operator/Sink.h \
	    src/operator/Sink.cpp\
	    build/dep/operator/Sink.dep \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h \
	    src/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Sink"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/impl/include -c $(SPL_CXXFLAGS) src/operator/Sink.cpp

build/operator:
	-@mkdir -m 775 -p build/operator

functions: $(SPL_FUNCTIONS)

pes: $(SPL_PE_BINS)

build/pe/BeJwz9cnMSy2OBwAJewKQ.o: \
	    src/pe/BeJwz9cnMSy2OBwAJewKQ.h \
	    src/pe/BeJwz9cnMSy2OBwAJewKQ.cpp  \
	    build/cppOptions \
	    | build/pe
	@echo " [CXX-pe] pe0"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/pe/BeJwz9cnMSy2OBwAJewKQ.cpp

PEBeJwz9cnMSy2OBwAJewKQ = \
	    build/pe/BeJwz9cnMSy2OBwAJewKQ.o \
	    build/operator/Lines.o \
	    build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o \
	    build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o 

bin/BeJwz9cnMSy2OBwAJewKQ.dpe: \
	$(PEBeJwz9cnMSy2OBwAJewKQ) \
	build/dep/BeJwz9cnMSy2OBwAJewKQ.dep \
	build/ldOptions
	@echo " [LD-pe] pe0"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/BeJwz9cnMSy2OBwAJewKQ.dpe -fPIC -shared $(PEBeJwz9cnMSy2OBwAJewKQ) -L /opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -Wl,-rpath -Wl,/opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -lstreams-stdtk-runtime $(SPL_LDFLAGS)

build/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.o: \
	    src/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.h \
	    src/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.cpp  \
	    build/cppOptions \
	    | build/pe
	@echo " [CXX-pe] pe1"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.cpp

PEBeJyz8CvNTUotSk2JBwAT_1gPK = \
	    build/pe/BeJyz8CvNTUotSk2JBwAT_1gPK.o \
	    build/operator/Numbered.o \
	    build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o 

bin/BeJyz8CvNTUotSk2JBwAT_1gPK.dpe: \
	$(PEBeJyz8CvNTUotSk2JBwAT_1gPK) \
	build/dep/BeJyz8CvNTUotSk2JBwAT_1gPK.dep \
	build/ldOptions
	@echo " [LD-pe] pe1"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/BeJyz8CvNTUotSk2JBwAT_1gPK.dpe -fPIC -shared $(PEBeJyz8CvNTUotSk2JBwAT_1gPK) $(SPL_LDFLAGS)

build/pe/BeJwzCc7My44HAAcAAIp.o: \
	    src/pe/BeJwzCc7My44HAAcAAIp.h \
	    src/pe/BeJwzCc7My44HAAcAAIp.cpp  \
	    build/cppOptions \
	    | build/pe
	@echo " [CXX-pe] pe2"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/pe/BeJwzCc7My44HAAcAAIp.cpp

PEBeJwzCc7My44HAAcAAIp = \
	    build/pe/BeJwzCc7My44HAAcAAIp.o \
	    build/operator/Sink.o \
	    build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o \
	    build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o 

bin/BeJwzCc7My44HAAcAAIp.dpe: \
	$(PEBeJwzCc7My44HAAcAAIp) \
	build/dep/BeJwzCc7My44HAAcAAIp.dep \
	build/ldOptions
	@echo " [LD-pe] pe2"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/BeJwzCc7My44HAAcAAIp.dpe -fPIC -shared $(PEBeJwzCc7My44HAAcAAIp) -L /opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -Wl,-rpath -Wl,/opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -lstreams-stdtk-runtime $(SPL_LDFLAGS)

build/pe:
	-@mkdir -m 775 build/pe

standalone: 
	@rm -f bin/standalone

