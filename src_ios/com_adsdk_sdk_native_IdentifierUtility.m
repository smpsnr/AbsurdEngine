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
#include "java_lang_Boolean.h"
#include "com_arcadeoftheabsurd_OpenUDID.h"
#include <AdSupport/AdSupport.h>
//XMLVM_END_NATIVE_IMPLEMENTATION

/**
 * Retrieves the iOS ASIdentifier and tracking setting or OpenUDID
 * @author sam
 */

void com_adsdk_sdk_IdentifierUtility_prepareAdIdImpl__()
{
	//XMLVM_BEGIN_NATIVE[com_arcadeoftheabsurd_absurdengine_DeviceUtility_getAdIdImpl__]
	NSString* adId;
    JAVA_OBJECT adDoNotTrack = JAVA_NULL;
	XMLVMElem conversion;
	if (!NSClassFromString(@"ASIdentifierManager")) {
        adId = [OpenUDID value];
    } else {
    	adId = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    	conversion.i = (JAVA_BOOLEAN) ![[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled];
    	
    	adDoNotTrack = __NEW_java_lang_Boolean();
    	java_lang_Boolean___INIT____boolean(adDoNotTrack, conversion.i);
    	com_adsdk_sdk_IdentifierUtility_PUT_adDoNotTrack(adDoNotTrack);
    }
    if (adId != NULL) {
    	com_adsdk_sdk_IdentifierUtility_PUT_adId(fromNSString(adId));
    } else {
    	com_adsdk_sdk_IdentifierUtility_PUT_adId(com_adsdk_sdk_IdentifierUtility_GET_DEFAULT_AD_ID());
    }
	//XMLVM_END_NATIVE
}
