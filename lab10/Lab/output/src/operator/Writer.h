// eJx1j8EOwiAQRLO_10nimUD1xo4cejBqT_0gMVqSUSSmCx0a_0XYGPCwexpJ29mZ4MzZLgNDpXnvNNG9do_0gOZhUBFS48upulX7xR_1lQi9y93z3oRWtkOKEZKoS2IDRuKHQnw_0c_04Be2ztkjX21qC1uG0jgmE4AK0m22gWm_1RpRdV4pGE0ME5QBbA0tSZYqaDf7X4V_1bUGDz59B8sg52qTkXPoB_0iFMBC




#ifndef SPL_OPER_INSTANCE_WRITER_H_
#define SPL_OPER_INSTANCE_WRITER_H_

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <fstream>
#include <streams_boost/iostreams/stream.hpp>
#include <streams_boost/iostreams/filtering_streambuf.hpp>
#include <SPL/Runtime/Common/Metric.h>
#include <streams_boost/iostreams/device/file_descriptor.hpp>
#include <SPL/Runtime/Operator/Operator.h>
#include <SPL/Runtime/Operator/ParameterValue.h>
#include <SPL/Runtime/Operator/OperatorContext.h>
#include <SPL/Runtime/Operator/Port/AutoPortMutex.h>
#include <SPL/Runtime/ProcessingElement/PE.h>
#include <SPL/Runtime/Type/SPLType.h>
#include <SPL/Runtime/Utility/CV.h>
using namespace UTILS_NAMESPACE;

#include "../type/BeJwrMcw0Tc4vzSsBABAcANt.h"


#define MY_OPERATOR Writer$OP
#define MY_BASE_OPERATOR Writer_Base
#define MY_OPERATOR_SCOPE SPL::_Operator

namespace SPL {
namespace _Operator {

class MY_BASE_OPERATOR : public Operator
{
public:
    
    typedef SPL::BeJwrMcw0Tc4vzSsBABAcANt IPort0Type;
    
    MY_BASE_OPERATOR();
    
    ~MY_BASE_OPERATOR();
    
    void processRaw(Tuple const & tuple, uint32_t port);
    
    void processRaw(Punctuation const & punct, uint32_t port);
    
    
    
protected:
    Mutex $svMutex;
    SPL::rstring lit$0;
    SPL::uint32 lit$1;
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

    virtual void prepareToShutdown();

    void process(Tuple const & tuple, uint32_t port);
    
    
        void process(Punctuation const & punct, uint32_t port);
    
  private:
    class Helper {
        public:
            Helper (const std::string& fName
                    );
            
                std::ostream& fs() { return _fs; }
                std::ostream& writeTo() { return _fs; }
            
            void flush() { _fs.flush(); }
            int fd() { return _fd; }
            streams_boost::iostreams::filtering_streambuf<streams_boost::iostreams::output>& filt_str()
                { return _filt_str; }
        private:
            std::auto_ptr<streams_boost::iostreams::file_descriptor_sink> _fd_sink;
            std::ostream _fs;
            streams_boost::iostreams::filtering_streambuf<streams_boost::iostreams::output> _filt_str;
            
            
            int _fd;
    };

    

    

    void openFile();
    void closeFile();
    Mutex _mutex;
    volatile bool _shutdown;
    std::string _pathName;
    Metric& _numFilesOpenedMetric;
    std::auto_ptr<Helper> _f;
    
    
    
    
    uint64_t _tuplesUntilFlush, _flushCount;
    
    
    
    
    
    
}; 

} // namespace _Operator
} // namespace SPL

#undef MY_OPERATOR_SCOPE
#undef MY_BASE_OPERATOR
#undef MY_OPERATOR
#endif // SPL_OPER_INSTANCE_WRITER_H_

