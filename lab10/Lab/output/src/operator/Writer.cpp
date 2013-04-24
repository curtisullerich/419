// eJx1j8EOwiAQRLO_10nimUD1xo4cejBqT_0gMVqSUSSmCx0a_0XYGPCwexpJ29mZ4MzZLgNDpXnvNNG9do_0gOZhUBFS48upulX7xR_1lQi9y93z3oRWtkOKEZKoS2IDRuKHQnw_0c_04Be2ztkjX21qC1uG0jgmE4AK0m22gWm_1RpRdV4pGE0ME5QBbA0tSZYqaDf7X4V_1bUGDz59B8sg52qTkXPoB_0iFMBC



#include "./Writer.h"
using namespace SPL::_Operator;

#include <SPL/Runtime/Function/SPLFunctions.h>
#include <SPL/Runtime/Operator/Port/Punctuation.h>


#define MY_OPERATOR_SCOPE SPL::_Operator
#define MY_BASE_OPERATOR Writer_Base
#define MY_OPERATOR Writer$OP


#include <SPL/Runtime/Common/ApplicationRuntimeMessage.h>
#include <SPL/Runtime/Operator/OperatorMetrics.h>
#include <SPL/Toolkit/RuntimeException.h>
#include <streams_boost/filesystem/path.hpp>
#include <signal.h>


using namespace std;
using namespace streams_boost::iostreams;


void MY_OPERATOR_SCOPE::MY_OPERATOR::closeFile()
{
    if (!_f.get()) 
        return; // we don't want to do this twice

    delete _f.release();
    
    
    // close this file, and start a new one (if needed)
    
    
}

MY_OPERATOR_SCOPE::MY_OPERATOR::Helper::Helper(const string& fName
                            )
: _fs(&_filt_str)


{
    SPLLOG(L_DEBUG, "Using '" << fName << "' as the output file...", SPL_OPER_DBG);

    
        _fd = ::open (fName.c_str(), O_WRONLY|O_CREAT|O_LARGEFILE|O_TRUNC, 0666);
    
    if (_fd < 0)
        THROW (SPLRuntimeFileSinkOperator, 
            SPL_APPLICATION_RUNTIME_FAILED_OPEN_OUTPUT_FILE(fName,
                                                            RuntimeUtility::getErrorNoStr()));

    _fd_sink.reset (new file_descriptor_sink (_fd, true));
    
    _filt_str.push (*_fd_sink);
    _fs.imbue(locale::classic());
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::openFile()
{
    if (!_shutdown) {
        _f.reset (new Helper(_pathName
                             ));
        _numFilesOpenedMetric.incrementValueNoLock();
        
    }
}

MY_OPERATOR_SCOPE::MY_OPERATOR::MY_OPERATOR()
: MY_BASE_OPERATOR(),
  _shutdown(false), _pathName (lit$0),
  _numFilesOpenedMetric(getContext().getMetrics().getCustomMetricByName("nFilesOpened"))

  
  
  
  
  
{
    SPLLOG(L_DEBUG, "FileSink startup...", SPL_OPER_DBG);
    _numFilesOpenedMetric.setValueNoLock(0);

    // Figure out output file and open it
    namespace bf = streams_boost::filesystem;
    
    openFile();
    
    _tuplesUntilFlush = _flushCount = lit$1;
    
    
    
    signal(SIGPIPE,SIG_IGN);
}


void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Punctuation const & punct, uint32_t port) {
    AutoMutex am(_mutex);
    if (_shutdown)
        return;
    try {
        
        
            _f->flush();
        
    } catch (const std::exception& e) {
        SPLLOG(L_ERROR, SPL_APPLICATION_RUNTIME_EXCEPTION(e.what()), SPL_OPER_DBG);
    } catch (...) {
        SPLLOG(L_ERROR, SPL_APPLICATION_RUNTIME_UNKNOWN_EXCEPTION, SPL_OPER_DBG);
    }
    
    _tuplesUntilFlush = _flushCount;
}


void MY_OPERATOR_SCOPE::MY_OPERATOR::prepareToShutdown()
{
    AutoMutex am(_mutex);
    _shutdown = true;
    
        delete _f.release();
    
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::process(Tuple const & tuple$, uint32_t port)
{
    AutoMutex am(_mutex);
    if (_shutdown)
        return;
    // Write to output
    const SPL::BeJwrMcw0Tc4vzSsBABAcANt& tuple =
        static_cast<const SPL::BeJwrMcw0Tc4vzSsBABAcANt&>(tuple$);
    try {
        
        	    SPL::serializeWithPrecision(_f->writeTo(), tuple.get_count()) << '\n';

        
        if (--_tuplesUntilFlush == 0) {
            _tuplesUntilFlush = _flushCount;
            _f->fs().flush();
        }
        
    } catch (const std::exception& e) {
        SPLLOG(L_ERROR, SPL_APPLICATION_RUNTIME_EXCEPTION(e.what()), SPL_OPER_DBG);
    } catch (...) {
        SPLLOG(L_ERROR, SPL_APPLICATION_RUNTIME_UNKNOWN_EXCEPTION, SPL_OPER_DBG);
    }
    
}

static SPL::Operator * initer() { return new MY_OPERATOR_SCOPE::MY_OPERATOR(); }
bool MY_BASE_OPERATOR::globalInit_ = MY_BASE_OPERATOR::globalIniter();
bool MY_BASE_OPERATOR::globalIniter() {
    instantiators_.insert(std::make_pair("Writer",&initer));
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
    addParameterValue ("file", SPL::ConstValueHandle(lit$0));
    addParameterValue ("flush", SPL::ConstValueHandle(lit$1));
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

void MY_BASE_OPERATOR::processRaw(Tuple const & tuple, uint32_t port) {
    
    static_cast<const  MY_OPERATOR_SCOPE::MY_OPERATOR*>(this)->MY_OPERATOR::process(tuple, port);
}


void MY_BASE_OPERATOR::processRaw(Punctuation const & punct, uint32_t port) {
    
    process(punct, port);
}




