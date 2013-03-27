// eJyVkN9qwjAYxflu_0xRBvGjpiEm9GGQi6IXCnEPoA5RSQhfMkpJ8IkX27ktr_1QdDWHITDofzOye_00dRJXaKyptRCrA6mQuuA9ZfDiNIJto2cLOX70W3zNsu3r7sNM5_0bdr9eLFnBF3nB6dcomD2WKMcK8t2HEMrgNAPIQCscs7PmPDplaug1_1uDrYAxUY93V_1RwJCs41TxEJZ2Cn6Vv0E0EIq6xBadDDBdrl23_1kf4f8P2P44xgOcSxEL_1lGF1XpcXZveCG3nWQuRGeIh7oJSUn_1P91jGE9ricUFGydJ6P0L1B96B3

#include "./Numbered.h"
using namespace SPL::_Operator;

#include <SPL/Runtime/Function/SPLFunctions.h>
#include <SPL/Runtime/Operator/Port/Punctuation.h>

#include <string>

#define MY_OPERATOR_SCOPE SPL::_Operator
#define MY_BASE_OPERATOR Numbered_Base
#define MY_OPERATOR Numbered$OP




void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple const & tuple, uint32_t port) 
{
   IPort0Type const & iport$0 = static_cast<IPort0Type const&>(tuple);
   if (! (1) ) 
       return;
   { OPort0Type otuple(((::SPL::spl_cast<SPL::rstring, SPL::int32 >::cast(state$i) + lit$0) + iport$0.get_contents())); submit (otuple, 0);
 }
   
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Punctuation const & punct, uint32_t port) 
{
   forwardWindowPunctuation(punct);
}

static SPL::Operator * initer() { return new MY_OPERATOR_SCOPE::MY_OPERATOR(); }
bool MY_BASE_OPERATOR::globalInit_ = MY_BASE_OPERATOR::globalIniter();
bool MY_BASE_OPERATOR::globalIniter() {
    instantiators_.insert(std::make_pair("Numbered",&initer));
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
    state$i = lit$1;
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
    AutoPortMutex $apm($svMutex, *this);
    
    IPort0Type const & iport$0 = static_cast<IPort0Type const  &>(tuple);
    
{
    state$i++;
}

    static_cast<const  MY_OPERATOR_SCOPE::MY_OPERATOR*>(this)->MY_OPERATOR::process(iport$0, 0);
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





