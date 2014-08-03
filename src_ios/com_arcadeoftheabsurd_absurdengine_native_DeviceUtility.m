
#include "xmlvm.h"
#include "com_arcadeoftheabsurd_absurdengine_DeviceUtility.h"

//XMLVM_BEGIN_NATIVE_IMPLEMENTATION
#include "org_xmlvm_iphone_NSString.h"
#include "mobfox_UIDevice+IdentifierAddition.h"
#include "com_arcadeoftheabsurd_OpenUDID.h"
#include <AdSupport/AdSupport.h>
//XMLVM_END_NATIVE_IMPLEMENTATION

JAVA_OBJECT com_arcadeoftheabsurd_absurdengine_DeviceUtility_getAdIdImpl__()
{
	//XMLVM_BEGIN_NATIVE[com_arcadeoftheabsurd_absurdengine_DeviceUtility_getAdIdImpl__]
	NSString* adId;
	if (!NSClassFromString(@"ASIdentifierManager")) {
        adId = [OpenUDID value];
    } else {
    	adId = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    }
    if (adId != NULL) {
    	return fromNSString(adId);
    }
    return com_arcadeoftheabsurd_absurdengine_DeviceUtility_GET_DEFAULT_AD_ID();
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
