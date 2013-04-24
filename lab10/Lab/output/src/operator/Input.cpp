// eJx1jkEKwjAQRZmrlO4KaVJdZZcsXBQVoV6ghgEDJSnJxFJPb6xV6EJm8_1k83vw4DiyRHSzNUmrsjXfAlxNQMFbTPGKtsZ3CyUz8avaPZxe10sqoM7F7kbFIPWHpoLscpbSOdg1AA9lY8k_0X1vJdiQ0mwBKGnqx3EcSGFqtBEQV7S4SHgLjM4uBHH372f_0PAQsi08cnlvDo37_1l3elXxF6VJEt1

#include "./Input.h"
using namespace SPL::_Operator;

#include <SPL/Runtime/Function/SPLFunctions.h>
#include <SPL/Runtime/Operator/Port/Punctuation.h>


#define MY_OPERATOR_SCOPE SPL::_Operator
#define MY_BASE_OPERATOR Input_Base
#define MY_OPERATOR Input$OP


void MY_OPERATOR_SCOPE::MY_OPERATOR::allPortsReady()
{
    createThreads (1);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(uint32_t) 
{
    SPLLOG(L_DEBUG, "Beacon startup...", SPL_OPER_DBG);
    SPL::BeJwrMcw0Tc4vzSsBABAcANt tuple;
    ProcessingElement& pe = getPE();
    tuple.clear();
    
        SPL::uint32 iters = lit$0;
    
    
    
    
    while(!pe.getShutdownRequested()) {
        
            if (iters == 0)
                break;
            iters--;
        
        tuple.set_count(state$n++); 
        submit (tuple, 0);
        
    }
    submit(Punctuation::WindowMarker, 0);
    submit(Punctuation::FinalMarker, 0);
    SPLLOG(L_DEBUG, "Beacon exiting...", SPL_OPER_DBG);
}

static SPL::Operator * initer() { return new MY_OPERATOR_SCOPE::MY_OPERATOR(); }
bool MY_BASE_OPERATOR::globalInit_ = MY_BASE_OPERATOR::globalIniter();
bool MY_BASE_OPERATOR::globalIniter() {
    instantiators_.insert(std::make_pair("Input",&initer));
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
    initRTC(*this, lit$0, "lit$0");
    initRTC(*this, lit$1, "lit$1");
    state$n = lit$1;
    addParameterValue ("iterations", SPL::ConstValueHandle(lit$0));
    (void) getParameters(); // ensure thread safety by initializing here
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




