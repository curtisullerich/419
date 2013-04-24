// eJyNjssKwjAQRcmvBBe6SR_06yi5duJBahPYHaggaME2ZTC31651GFwqKMqt5nDPXTcL3UjYmIEtjZYwLkeDUm6QwuxH2ekwbvbne6lCoQmlVoTjzeJozfWlDqFpnCKsPpZQQEGx3ouVru_0TzA75iCqk_1Dmi2YMyDLu0RWph_0CSiTm7MmH00Z3dveAy6e3LfozDKI2Rkx2g8dTTpc59Hg_1zI4MryzKcF3lyFhBf

#include "./Output.h"
using namespace SPL::_Operator;

#include <SPL/Runtime/Function/SPLFunctions.h>
#include <SPL/Runtime/Operator/Port/Punctuation.h>

#include <string>

#define MY_OPERATOR_SCOPE SPL::_Operator
#define MY_BASE_OPERATOR Output_Base
#define MY_OPERATOR Output$OP




// JavaOp - non generated 


#include <SPL/Toolkit/JavaOp.h>

MY_OPERATOR_SCOPE::MY_OPERATOR::MY_OPERATOR()
{
  /* Get a handle to the Java virtual machine */
  size_t vmOptionsCount = 0;
SPL::rstring vmOptions[vmOptionsCount];


  jboolean startedJVM;
  char const * javaOpDir = "/opt/ibm/InfoSphereStreams/toolkits/spl/spl/utility/JavaOp";

  SPL::JNI::JVMControl *jvmControl = SPL::JNI::JVMControl::getJVM(
     javaOpDir, vmOptions, vmOptionsCount, &startedJVM);
   
  /* Attach to the JVM  for the duration of the initialization */
  JNIEnv * env = jvmControl->attach();

  /* How we invoke methods against an OperatorSetup instance */
  SPL::JNI::OpSetupInvoker* osi = jvmControl->getOpSetupInvoker();

  /* OperatorSetup instance specific to this operator */
  jobject setup = osi->newSetup(env, this); 
   
  /**
     Pass the parameters into my OperatorSetup instance.
  */
     osi->setParameter(env, setup,   "className",   SPL::rstring("Test"));
   osi->setParameter(env, setup,   "classLibrary",   SPL::rstring("../my.op/Test"));


  /**
    Pass the windowing information for each port as
    a list of values for the parameter '[window].N' where
    N is the index of the windowed input port.
  */
  

  
  /* At this point all the initialization information has been
     passed to OperatorSetup. Create a JNIBridge instance object
     we use to communicate with the user's Operator implementation
     at runtime.
  */
  
  _bi = jvmControl->getBridgeInvoker();
  _bridge = _bi->newBridge(env, this, setup, startedJVM, (jboolean) false);
        
  /* Completed Java initialization, detach from the JVM */
  jvmControl->detach();
}

MY_OPERATOR_SCOPE::MY_OPERATOR::~MY_OPERATOR() 
{
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::allPortsReady() 
{
    _bi->allPortsReady(_bridge);
    createThreads(1);
}
 
void MY_OPERATOR_SCOPE::MY_OPERATOR::prepareToShutdown() 
{
   _bi->shutdown(_bridge);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(uint32_t idx)
{
   _bi->complete(_bridge);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple & tuple, uint32_t port)
{
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple const & tuple, uint32_t port)
{
   _bi->processTuple(_bridge, tuple, port);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Punctuation const & punct, uint32_t port)
{
   _bi->processPunctuation(_bridge, punct, port);
}

static SPL::Operator * initer() { return new MY_OPERATOR_SCOPE::MY_OPERATOR(); }
bool MY_BASE_OPERATOR::globalInit_ = MY_BASE_OPERATOR::globalIniter();
bool MY_BASE_OPERATOR::globalIniter() {
    instantiators_.insert(std::make_pair("Output",&initer));
    return true;
}

template<class T> static void initRTC (SPL::Operator& o, T& v, const char * n) {
    SPL::ValueHandle vh = v;
    o.getContext().getRuntimeConstantValue(vh, n);
}

MY_BASE_OPERATOR::MY_BASE_OPERATOR()
 : Operator()  {
    PE & pe = PE::instance();
    uint32_t index = getIndex();
    param$className$0 = SPL::rstring("Test");
    addParameterValue ("className", SPL::ConstValueHandle(param$className$0));
    param$classLibrary$0 = SPL::rstring("../my.op/Test");
    addParameterValue ("classLibrary", SPL::ConstValueHandle(param$classLibrary$0));
    (void) getParameters(); // ensure thread safety by initializing here
    $oportBitset = OPortBitsetType(std::string("01"));
}
MY_BASE_OPERATOR::~MY_BASE_OPERATOR()
{
    for (ParameterMapType::const_iterator it = paramValues_.begin(); it != paramValues_.end(); it++) {
        const ParameterValueListType& pvl = it->second;
        for (ParameterValueListType::const_iterator it2 = pvl.begin(); it2 != pvl.end(); it2++) {
            delete *it2;
        }
    }
}

void MY_BASE_OPERATOR::processRaw(Tuple const & tuple, uint32_t port) {
    
    static_cast<const  MY_OPERATOR_SCOPE::MY_OPERATOR*>(this)->MY_OPERATOR::process(tuple, port);
}


void MY_BASE_OPERATOR::processRaw(Punctuation const & punct, uint32_t port) {
    
    if (punct == Punctuation::FinalMarker) {
        process(punct, port);
        bool forward = false;
        {
            AutoPortMutex $apm($fpMutex, *this);
            $oportBitset.reset(port);
            if ($oportBitset.none()) {
                $oportBitset.set(1);
                forward=true;
            }
        }
        if(forward)
            submit(punct, 0);
        return;
    }
    
    process(punct, port);
}






