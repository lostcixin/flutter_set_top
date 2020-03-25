#import "SettopPlugin.h"
#if __has_include(<settop/settop-Swift.h>)
#import <settop/settop-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "settop-Swift.h"
#endif

@implementation SettopPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSettopPlugin registerWithRegistrar:registrar];
}
@end
