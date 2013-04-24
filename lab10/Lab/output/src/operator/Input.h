// eJx1jkEKwjAQRZmrlO4KaVJdZZcsXBQVoV6ghgEDJSnJxFJPb6xV6EJm8_1k83vw4DiyRHSzNUmrsjXfAlxNQMFbTPGKtsZ3CyUz8avaPZxe10sqoM7F7kbFIPWHpoLscpbSOdg1AA9lY8k_0X1vJdiQ0mwBKGnqx3EcSGFqtBEQV7S4SHgLjM4uBHH372f_0PAQsi08cnlvDo37_1l3elXxF6VJEt1

#ifndef SPL_OPER_INSTANCE_INPUT_H_
#define SPL_OPER_INSTANCE_INPUT_H_

#include <SPL/Runtime/Operator/Operator.h>
#include <SPL/Runtime/Operator/ParameterValue.h>
#include <SPL/Runtime/Operator/OperatorContext.h>
#include <SPL/Runtime/Operator/Port/AutoPortMutex.h>
#include <SPL/Runtime/ProcessingElement/PE.h>
#include <SPL/Runtime/Type/SPLType.h>
#include <SPL/Runtime/Utility/CV.h>
using namespace UTILS_NAMESPACE;

#include "../type/BeJwrMcw0Tc4vzSsBABAcANt.h"


#define MY_OPERATOR Input$OP
#define MY_BASE_OPERATOR Input_Base
#define MY_OPERATOR_SCOPE SPL::_Operator

namespace SPL {
namespace _Operator {

class MY_BASE_OPERATOR : public Operator
{
public:
    
    typedef SPL::BeJwrMcw0Tc4vzSsBABAcANt OPort0Type;
    
    MY_BASE_OPERATOR();
    
    ~MY_BASE_OPERATOR();
    
    
    
    
protected:
    Mutex $svMutex;
    SPL::uint32 lit$0;
    SPL::int32 lit$1;
    SPL::int32 state$n;
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
  
   void allPortsReady();
 
   void process(uint32_t index);

   void prepareToShutdown() {}
}; 

} // namespace _Operator
} // namespace SPL

#undef MY_OPERATOR_SCOPE
#undef MY_BASE_OPERATOR
#undef MY_OPERATOR
#endif // SPL_OPER_INSTANCE_INPUT_H_


