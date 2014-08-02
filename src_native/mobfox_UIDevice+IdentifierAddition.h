/*
 * From the MobFox iOS SDK (https://github.com/mobfox/MobFox-iOS-SDK)
 * Used in AbsurdEngine under the MoPub Client License (see /MoPub_Client_License.txt)
 */
 
//
//  UIDevice(Identifier).h
//  UIDeviceAddition
//
//  Created by Georg Kitz on 20.08.11.
//  Copyright 2011 Aurora Apps. All rights reserved.
//
//  With additional IP Address Lookup Code

#import <UIKit/UIKit.h>

@interface UIDevice (IdentifierAddition)

// IPAddress Lookup

+ (NSString *) localWiFiIPAddress;
+ (NSString *) localCellularIPAddress;
+ (NSString *) localSimulatorIPAddress;

@end
