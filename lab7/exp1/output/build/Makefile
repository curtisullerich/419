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

SPL_TYPES = build/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.o \
            build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o \
            build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o 
SPL_FUNCTIONS = 
SPL_OPERATORS = build/operator/LineStream.o \
                build/operator/Counter.o \
                build/operator/CountSink.o \
                build/operator/Numbered.o 
SPL_PE_OBJS = build/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.o 
SPL_PE_BINS = bin/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.dpe 

.PHONY: distclean clean all types functions operators pes svar

all: bin types functions operators pes svar standalone

distclean clean:
	@rm -fr bin/* build/type build/function build/operator build/pe build/svar build/standalone

bin:
	@mkdir -m 775 bin

types: $(SPL_TYPES)

build/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.o: \
	    src/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.h \
	    src/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.cpp \
	    build/cppOptions \
	    | build/type
	@echo " [CXX-type] enum{WindowMarker,FinalMarker}"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.cpp

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

-include build/operator/LineStream.d
build/operator/LineStream.o: \
	    src/operator/LineStream.h \
	    src/operator/LineStream.cpp\
	    build/dep/operator/LineStream.dep \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] LineStream"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/impl/include -c $(SPL_CXXFLAGS) src/operator/LineStream.cpp

-include build/operator/Counter.d
build/operator/Counter.o: \
	    src/operator/Counter.h \
	    src/operator/Counter.cpp\
	    build/dep/operator/Counter.dep \
	    src/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.h  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Counter"
	@$(CXX) -o $@ -MD -c $(SPL_CXXFLAGS) src/operator/Counter.cpp

-include build/operator/CountSink.d
build/operator/CountSink.o: \
	    src/operator/CountSink.h \
	    src/operator/CountSink.cpp\
	    build/dep/operator/CountSink.dep \
	    src/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] CountSink"
	@$(CXX) -o $@ -MD -I /opt/ibm/InfoSphereStreams/toolkits/spl/impl/include -c $(SPL_CXXFLAGS) src/operator/CountSink.cpp

-include build/operator/Numbered.d
build/operator/Numbered.o: \
	    src/operator/Numbered.h \
	    src/operator/Numbered.cpp\
	    build/dep/operator/Numbered.dep  \
	    build/cppOptions \
	    | build/operator
	@echo " [CXX-operator] Numbered"
	@$(CXX) -o $@ -MD -c $(SPL_CXXFLAGS) src/operator/Numbered.cpp

build/operator:
	-@mkdir -m 775 -p build/operator

functions: $(SPL_FUNCTIONS)

pes: $(SPL_PE_BINS)

build/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.o: \
	    src/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.h \
	    src/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.cpp  \
	    build/cppOptions \
	    | build/pe
	@echo " [CXX-pe] pe0"
	@$(CXX) -o $@ -c $(SPL_CXXFLAGS) src/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.cpp

PEBeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq = \
	    build/pe/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.o \
	    build/operator/LineStream.o \
	    build/type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.o \
	    build/type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.o \
	    build/operator/Counter.o \
	    build/type/BeJyrNIo3NArPzEvJL_1dNLMpOLTI0dMvMS8yBcACkegr2.o \
	    build/operator/CountSink.o \
	    build/operator/Numbered.o 

bin/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.dpe: \
	$(PEBeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq) \
	build/dep/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.dep \
	build/ldOptions
	@echo " [LD-pe] pe0"
	@$(CXX) $(SPL_CXXFLAGS) -o bin/BeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq.dpe -fPIC -shared $(PEBeJyzdM4vzSsJzszLjjcHM1OL4g0NfDLzUoNLilITc_0Mt_1Epzk1KLUlPiAWAqBAq) -L /opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -Wl,-rpath -Wl,/opt/ibm/InfoSphereStreams/toolkits/spl/impl/lib -lstreams-stdtk-runtime -lstreams-stdtk-runtime $(SPL_LDFLAGS)

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
	@ln -sf standalone bin/exp1
build/standalone:
	-@mkdir -m 775 build/standalone

