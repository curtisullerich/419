// eJyVkN9qwjAYxflu_0xRBvGjpiEm9GGQi6IXCnEPoA5RSQhfMkpJ8IkX27ktr_1QdDWHITDofzOye_00dRJXaKyptRCrA6mQuuA9ZfDiNIJto2cLOX70W3zNsu3r7sNM5_0bdr9eLFnBF3nB6dcomD2WKMcK8t2HEMrgNAPIQCscs7PmPDplaug1_1uDrYAxUY93V_1RwJCs41TxEJZ2Cn6Vv0E0EIq6xBadDDBdrl23_1kf4f8P2P44xgOcSxEL_1lGF1XpcXZveCG3nWQuRGeIh7oJSUn_1P91jGE9ricUFGydJ6P0L1B96B3

#ifndef SPL_OPER_INSTANCE_NUMBERED_H_
#define SPL_OPER_INSTANCE_NUMBERED_H_

#include <SPL/Runtime/Operator/Operator.h>
#include <SPL/Runtime/Operator/ParameterValue.h>
#include <SPL/Runtime/Operator/OperatorContext.h>
#include <SPL/Runtime/Operator/Port/AutoPortMutex.h>
#include <SPL/Runtime/ProcessingElement/PE.h>
#include <SPL/Runtime/Type/SPLType.h>
#include <SPL/Runtime/Utility/CV.h>
using namespace UTILS_NAMESPACE;

#include "../type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h"

#include <bitset>

#define MY_OPERATOR Numbered$OP
#define MY_BASE_OPERATOR Numbered_Base
#define MY_OPERATOR_SCOPE SPL::_Operator

namespace SPL {
namespace _Operator {

class MY_BASE_OPERATOR : public Operator
{
public:
    
    typedef SPL::BeJwrMSy2SM7PK0nNKykGAB0_1AS_1 IPort0Type;
    typedef SPL::BeJwrMSy2SM7PK0nNKykGAB0_1AS_1 OPort0Type;
    
    MY_BASE_OPERATOR();
    
    ~MY_BASE_OPERATOR();
    
    void processRaw(Tuple const & tuple, uint32_t port);
    
    void processRaw(Punctuation const & punct, uint32_t port);
    
    inline void submit(Tuple & tuple, uint32_t port)
    {
        Operator::submit(tuple, port);
    }
    inline void submit(Punctuation const & punct, uint32_t port)
    {
        Operator::submit(punct, port);
    }
    
    
    
protected:
    Mutex $svMutex;
    SPL::rstring lit$0;
    SPL::int32 lit$1;
    typedef std::bitset<2> OPortBitsetType;
    OPortBitsetType $oportBitset;
    Mutex $fpMutex;
    SPL::int32 state$i;
private:
    static bool globalInit_;
    static bool globalIniter();
    ParameterMapType paramValues_;
    ParameterMapType& getParameters() { return paramValues_;}
    void addParameterValue(std::string const & param, ConstValueHandle const& value)
    {
        ParameterMapType::iterator it = paramValues_.find(param);
        if (it == paramValues_.end())
            it = paramValues_.insert (std::make_pair (param, ParameterValueListType())).first;
        it->second.push_back(&ParameterValue::create(value));
    }
    void addParameterValue(std::string const & param)
    {
        ParameterMapType::iterator it = paramValues_.find(param);
        if (it == paramValues_.end())
            it = paramValues_.insert (std::make_pair (param, ParameterValueListType())).first;
        it->second.push_back(&ParameterValue::create());
    }
};


class MY_OPERATOR : public MY_BASE_OPERATOR 
{
public:
   MY_OPERATOR()
      : MY_BASE_OPERATOR() {}
  
   void process(Tuple const & tuple, uint32_t port);
   void process(Punctuation const & punct, uint32_t port);
}; 

} // namespace _Operator
} // namespace SPL

#undef MY_OPERATOR_SCOPE
#undef MY_BASE_OPERATOR
#undef MY_OPERATOR
#endif // SPL_OPER_INSTANCE_NUMBERED_H_

