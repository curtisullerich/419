// eJx9T8lOwzAQ1fxKxTmdtAik3NzSILIRyXCOTHBKmsXWeCLw32OKitQLmtPTvNXZMVLvyrKmJEn7UUuzUKsBz7eBVRSt2Vu93unsk0rpN7K8r3Ocq9wPj2KHTSxkE0cfqyuqp_0oJty_0OyhK3uX9t4udquF2KU5359quo5J3NxUF0pyHbmx8xQgxjzzcIsi6ShBxTPx8hNOgMTYrD_00GxSn8BBu6sYb84NlPRh_1JqhC60D7Qr_1cVUcMBvC_0uUtD6HIRhr6C_1w_13kwAQVNa2bWMzu42CN_0A9lKGD4



#include "./Lines.h"
using namespace SPL::_Operator;

#include <SPL/Runtime/Function/SPLFunctions.h>
#include <SPL/Runtime/Operator/Port/Punctuation.h>


#define MY_OPERATOR_SCOPE SPL::_Operator
#define MY_BASE_OPERATOR Lines_Base
#define MY_OPERATOR Lines$OP


#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fstream>
#include <signal.h>

#include <streams_boost/iostreams/stream.hpp>
#include <streams_boost/iostreams/filtering_streambuf.hpp>
#include <streams_boost/iostreams/device/file_descriptor.hpp>


#include <SPL/Runtime/Common/ApplicationRuntimeMessage.h>
#include <SPL/Toolkit/IOHelper.h>
#include <SPL/Toolkit/RuntimeException.h>

#include <SPL/Runtime/Operator/OperatorMetrics.h>

using namespace std;
using namespace streams_boost::iostreams;

#define DEV_NULL "/dev/null"

// defines for error checking conditions
#define CHECK_FAIL      \
    if (failedRead)                                   \
        _numInvalidTuples.incrementValueNoLock()

    #define DO_ERROR(msg)                             \
        { _numInvalidTuples.incrementValueNoLock(); THROW (SPLRuntimeFileSourceOperator, (msg));}
    #define DO_ERROR_FILESOURCE(msg) DO_ERROR(msg)
    #define CHECK_ERROR(msg)                          \
        if (fs.fail() && !getPE().getShutdownRequested()) \
            DO_ERROR(msg)


MY_OPERATOR_SCOPE::MY_OPERATOR::MY_OPERATOR()
    : MY_BASE_OPERATOR(), _fd(-1),
         
         
        
        
        
        _numFilesOpenedMetric(getContext().getMetrics().getCustomMetricByName("nFilesOpened")),
        _numInvalidTuples(getContext().getMetrics().getCustomMetricByName("nInvalidTuples"))
{
    _numFilesOpenedMetric.setValueNoLock(0);
    _numInvalidTuples.setValueNoLock(0);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::prepareToShutdown()
{
    if (_fd >= 0) {
        ::close(_fd);
        _fd = -1;
    }
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::initialize()
{
    
}



void MY_OPERATOR_SCOPE::MY_OPERATOR::processOneFile (const string& pathName)
{
    SPL::BeJwrMSy2SM7PK0nNKykGAB0_1AS_1 tuple$;
    
    namespace bf = streams_boost::filesystem;
    SPLLOG(L_DEBUG, "Using '" << pathName << "' as the workload file...", SPL_OPER_DBG);

    int32_t fd = ::open (pathName.c_str(), O_RDONLY | O_LARGEFILE);
    if (fd < 0) {
        
        
        THROW (SPLRuntimeFileSourceOperator, (SPL_APPLICATION_RUNTIME_FAILED_OPEN_INPUT_FILE(pathName, RuntimeUtility::getErrorNoStr())));
    }
    
        file_descriptor_source fd_src (fd, true);
    
    _numFilesOpenedMetric.incrementValueNoLock();
    filtering_streambuf<input> filt_str;
    
    
    filt_str.push (fd_src);
    
        istream fs (&filt_str);
    
    fs.imbue(locale::classic());

    _fd = fd;
    _tupleNumber = 0;
    




bool failedRead = false;
while(!getPE().getShutdownRequested() && !fs.eof()) {
    _tupleNumber++;
    if (SPL::safePeek(fs) == EOF) break;
    bool doSubmit = true;
    try {
        
            SPL::rstring& t = tuple$.get_contents();
            std::getline (fs, t, '\n');
            
            if (fs.fail() || (fs.eof() && t.empty())) break;
        

    } catch (const SPLRuntimeException& e) {
        // Add the filename & tuple number
        DO_ERROR_FILESOURCE(
            SPL_APPLICATION_RUNTIME_FILE_SOURCE_SINK_FILENAME_TUPLE(_tupleNumber, pathName, e.getExplanation()));

    } catch (const std::exception& e) {
        DO_ERROR(SPL_APPLICATION_RUNTIME_EXCEPTION(e.what()));
        _numInvalidTuples.incrementValueNoLock();
        doSubmit = false;
    } catch (...) {
        DO_ERROR(SPL_APPLICATION_RUNTIME_UNKNOWN_EXCEPTION);
        _numInvalidTuples.incrementValueNoLock();
        doSubmit = false;
    }
    if (doSubmit)
        submit (tuple$, 0);
}

    if (_fd < 0) {
        // We closed it already.  Prevent an error message
        int newFd = ::open (DEV_NULL, O_RDONLY);
        ::dup2 (newFd, _fd);
        ::close(newFd);
    }
    _fd = -1; // no longer using this
    submit (Punctuation::WindowMarker, 0);
    



}


void MY_OPERATOR_SCOPE::MY_OPERATOR::process(uint32_t) 
{
    SPLLOG(L_DEBUG, "FileSource startup...", SPL_OPER_DBG);
    initialize();
    processOneFile (lit$0);
    SPLLOG(L_DEBUG, "FileSource exiting...", SPL_OPER_DBG);
}

void MY_OPERATOR_SCOPE::MY_OPERATOR::allPortsReady()
{
    createThreads (1);
}



static SPL::Operator * initer() { return new MY_OPERATOR_SCOPE::MY_OPERATOR(); }
bool MY_BASE_OPERATOR::globalInit_ = MY_BASE_OPERATOR::globalIniter();
bool MY_BASE_OPERATOR::globalIniter() {
    instantiators_.insert(std::make_pair("Lines",&initer));
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
    param$format$0 = "line";
    addParameterValue ("format", SPL::ConstValueHandle(param$format$0));
    addParameterValue ("file", SPL::ConstValueHandle(lit$0));
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




