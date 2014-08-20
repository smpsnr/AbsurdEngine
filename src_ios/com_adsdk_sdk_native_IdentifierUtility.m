/*
 * AbsurdEngine (https://bitbucket.org/smpsnr/absurdengine/) 
 * (c) by Sam Posner (http://www.arcadeoftheabsurd.com/)
 *
 * AbsurdEngine is licensed under a
 * Creative Commons Attribution 4.0 International License
 *
 * You should have received a copy of the license along with this
 * work. If not, see http://creativecommons.org/licenses/by/4.0/ 
 */
 
#include "xmlvm.h"
#include "com_adsdk_sdk_IdentifierUtility.h"

//XMLVM_BEGIN_NATIVE_IMPLEMENTATION
#include "org_xmlvm_iphone_NSString.h"
#include "com_arcadeoftheabsurd_OpenUDID.h"
#include <AdSupport/AdSupport.h>
//XMLVM_END_NATIVE_IMPLEMENTATION

/**
 * Retrieves the ASIdentifier or OpenUDID on iOS
 * @author sam
 */

JAVA_OBJECT com_adsdk_sdk_IdentifierUtility_getAdIdImpl__()
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
    return com_adsdk_sdk_IdentifierUtility_GET_DEFAULT_AD_ID();
	//XMLVM_END_NATIVE
}
