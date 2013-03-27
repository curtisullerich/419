// eJx9T8lOwzAQ1fxKxTmdtAik3NzSILIRyXCOTHBKmsXWeCLw32OKitQLmtPTvNXZMVLvyrKmJEn7UUuzUKsBz7eBVRSt2Vu93unsk0rpN7K8r3Ocq9wPj2KHTSxkE0cfqyuqp_0oJty_0OyhK3uX9t4udquF2KU5359quo5J3NxUF0pyHbmx8xQgxjzzcIsi6ShBxTPx8hNOgMTYrD_00GxSn8BBu6sYb84NlPRh_1JqhC60D7Qr_1cVUcMBvC_0uUtD6HIRhr6C_1w_13kwAQVNa2bWMzu42CN_0A9lKGD4


#ifndef SPL_OPER_INSTANCE_LINES_H_
#define SPL_OPER_INSTANCE_LINES_H_

namespace streams_boost { namespace iostreams { class file_descriptor_source; } }
#include <SPL/Runtime/Common/Metric.h>
#include <streams_boost/filesystem/path.hpp>
#include <SPL/Runtime/Operator/Operator.h>
#include <SPL/Runtime/Operator/ParameterValue.h>
#include <SPL/Runtime/Operator/OperatorContext.h>
#include <SPL/Runtime/Operator/Port/AutoPortMutex.h>
#include <SPL/Runtime/ProcessingElement/PE.h>
#include <SPL/Runtime/Type/SPLType.h>
#include <SPL/Runtime/Utility/CV.h>
using namespace UTILS_NAMESPACE;

#include "../type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h"
#include "../type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h"


#define MY_OPERATOR Lines$OP
#define MY_BASE_OPERATOR Lines_Base
#define MY_OPERATOR_SCOPE SPL::_Operator

namespace SPL {
namespace _Operator {

class MY_BASE_OPERATOR : public Operator
{
public:
    
    typedef SPL::BeJwrMSy2SM7PK0nNKykGAB0_1AS_1 OPort0Type;
    
    MY_BASE_OPERATOR();
    
    ~MY_BASE_OPERATOR();
    
    
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
    SPL::rstring param$format$0;
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
    MY_OPERATOR();
    
    
        virtual void process(uint32_t index);
        virtual void allPortsReady();
    

    virtual void prepareToShutdown();

private:
    void initialize();
    void processOneFile (const std::string& pathName);
    

    int32_t _fd;
    uint64_t _tupleNumber;
    
    
    
    
    
    Metric& _numFilesOpenedMetric;
    Metric& _numInvalidTuples;
}; 

} // namespace _Operator
} // namespace SPL

#undef MY_OPERATOR_SCOPE
#undef MY_BASE_OPERATOR
#undef MY_OPERATOR
#endif // SPL_OPER_INSTANCE_LINES_H_


