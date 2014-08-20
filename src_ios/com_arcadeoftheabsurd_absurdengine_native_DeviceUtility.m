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
#include "com_arcadeoftheabsurd_absurdengine_DeviceUtility.h"

//XMLVM_BEGIN_NATIVE_IMPLEMENTATION
#include "org_xmlvm_iphone_NSString.h"
#include "mobfox_UIDevice+IdentifierAddition.h"
#include <UIKit/UIkit.h>
//XMLVM_END_NATIVE_IMPLEMENTATION

/**
 * Methods for retrieving the device IP and user agent on iOS 
 * @author sam
 */

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

JAVA_OBJECT com_arcadeoftheabsurd_absurdengine_DeviceUtility_getUserAgentImpl__()
{
	//XMLVM_BEGIN_NATIVE[com_arcadeoftheabsurd_absurdengine_DeviceUtility_getUserAgentImpl__]
	UIWebView* webView = [[UIWebView alloc] initWithFrame:CGRectZero];
    NSString* userAgent = [webView stringByEvaluatingJavaScriptFromString:@"navigator.userAgent"];
    
    return fromNSString(userAgent);
	//XMLVM_END_NATIVE
}
