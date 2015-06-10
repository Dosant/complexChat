//
//  NetworkContoller.h
//  ComplexChatIOS
//
//  Created by Anton Dosov on 23.05.15.
//  Copyright (c) 2015 Anton Dosov. All rights reserved.
//

#import "AFHTTPSessionManager.h"

@interface NetworkContoller : AFHTTPSessionManager

+ (NetworkContoller *)sharedClient;

-(void)getNewMessages:(NSUInteger)actionID
               userID:(NSString*)userID
             username:(NSString*)username
              success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
              failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

-(void)postNewMessageWithUserID:(NSString*)userID
                       username:(NSString*)username
                       messageText:(NSString*)text
              success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
              failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

-(void)postNewUsernameWithUserID:(NSString*)userID
                       oldUsername:(NSString*)oldUsername
                    newUsername:(NSString*)newUsername
                        success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
                        failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;

-(void)getUserIDsuccess:(void (^)(NSURLSessionDataTask *task, NSString* userID))success
                failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure;
@end
