
#include "xmlvm.h"
#include "com_arcadeoftheabsurd_absurdengine_DeviceUtility.h"

//XMLVM_BEGIN_NATIVE_IMPLEMENTATION
#include "org_xmlvm_iphone_NSString.h"
#include "UIDevice+IdentifierAddition.h"
//XMLVM_END_NATIVE_IMPLEMENTATION

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

    IPAddressToReturn = [UIDevice localWiFiIPAddress];

    if(IPAddressToReturn == NULL) {
        IPAddressToReturn = [UIDevice localCellularIPAddress];
    }
	
	return fromNSString(IPAddressToReturn);
	//XMLVM_END_NATIVE
}
