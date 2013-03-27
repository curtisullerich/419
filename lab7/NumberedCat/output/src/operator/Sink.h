// eJx9UMtOwzAQ1P5KxTndtAik3NzSIPIikuEcmeIUN6ljrTcC_1z2mqEi9oD3Nzox2Zr0bE_1WuHGvKstyMWho7AJ5nBYskWXJwernRxSfVMqxkfd_0WaJsyDI9ig10qZJcmH4sraaDmCdcvnuoa12V47dLnZridq2NbhP1X1cg7V4qd6I9DsZ1_0zAgpjIZvEGRbZRl5JmMPEBP0MVIkr9YXreCI32bWOWkN_1UQnxZF8UKzyX4BRaTVsZ8_1TqTKxpBqjAsG4if7O_1V8ODND5HRCd_08mytuzhkgW_1AcEJGAV




#ifndef SPL_OPER_INSTANCE_SINK_H_
#define SPL_OPER_INSTANCE_SINK_H_

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

#include "../type/BeJwrMSy2SM7PK0nNKykGAB0_1AS_1.h"
#include "../type/BeJyrNI03TsrMM03KyU_1ONk4uLjPJycxLNS6pKAEAfjkJCo.h"


#define MY_OPERATOR Sink$OP
#define MY_BASE_OPERATOR Sink_Base
#define MY_OPERATOR_SCOPE SPL::_Operator

namespace SPL {
namespace _Operator {

class MY_BASE_OPERATOR : public Operator
{
public:
    
    typedef SPL::BeJwrMSy2SM7PK0nNKykGAB0_1AS_1 IPort0Type;
    
    MY_BASE_OPERATOR();
    
    ~MY_BASE_OPERATOR();
    
    void processRaw(Tuple const & tuple, uint32_t port);
    
    void processRaw(Punctuation const & punct, uint32_t port);
    
    
    
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
    
    
    
    
    
    
    
    
    
    
    
}; 

} // namespace _Operator
} // namespace SPL

#undef MY_OPERATOR_SCOPE
#undef MY_BASE_OPERATOR
#undef MY_OPERATOR
#endif // SPL_OPER_INSTANCE_SINK_H_

