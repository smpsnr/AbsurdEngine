
#include "xmlvm.h"
#include "com_arcadeoftheabsurd_absurdengine_DeviceUtility.h"

#import "UIDevice+IdentifierAddition.h"

JAVA_OBJECT com_arcadeoftheabsurd_absurdengine_DeviceUtility_getAdIdImpl__()
{
	//XMLVM_BEGIN_NATIVE[com_arcadeoftheabsurd_absurdengine_DeviceUtility_getAdIdImpl__]
	NSLog(@"this will return the advertising id");
	
	return JAVA_NULL;
	//XMLVM_END_NATIVE
}

JAVA_OBJECT com_arcadeoftheabsurd_absurdengine_DeviceUtility_getLocalIpImpl__()
{
	//XMLVM_BEGIN_NATIVE[com_arcadeoftheabsurd_absurdengine_DeviceUtility_getLocalIpImpl__]
	NSString *IPAddressToReturn;

	#if TARGET_IPHONE_SIMULATOR
        IPAddressToReturn = [UIDevice localSimulatorIPAddress];
	#else

    IPAddressToReturn = [UIDevice localWiFiIPAddress];

    if(!IPAddressToReturn) {
        IPAddressToReturn = [UIDevice localCellularIPAddress];
    }
	#endif
	
	return fromNSString(IPAddressToReturn);
	//XMLVM_END_NATIVE
}
