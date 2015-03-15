#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonDigest.h>
// #import <CommonCrypto/CommonHMAC.h>
// #import <CommonCrypto/CommonCryptor.h>

// Define the object here:
@interface HexTest : NSObject
-(NSString*) NSDataToHex:(NSData*)data;
@end

// Implement it here:
@implementation HexTest

-(NSString*) NSDataToHex:(NSData*)data
{
    const unsigned char *dbytes = [data bytes];
    NSMutableString *hexStr = [NSMutableString stringWithCapacity:[data length]*2];
    int i;
    for (i = 0; i < [data length]; i++) {
        [hexStr appendFormat:@"%02x ", dbytes[i]];
    }
    return [NSString stringWithString: hexStr];
}
@end

// Execute it here:
int main (int argc, const char * argv[])
{
    NSAutoreleasePool * pool = [[NSAutoreleasePool alloc] init];

    // (1) We must create our HexTest object first
    HexTest *hexer = [[HexTest alloc] init];
	
    // (2) Now, create a string
    NSString *str = @"This is some String";

    // (3) Make the string into NSData object
    NSData *strAsData = [str dataUsingEncoding:NSUTF8StringEncoding];
    
    unsigned char hashedChars[32];
    CC_SHA256(strAsData.bytes, strAsData.length, hashedChars);
    // (4) Execute our converter function
    NSLog([hexer NSDataToHex: strAsData]);


    [pool drain];
    return 0;
}