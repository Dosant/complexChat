//
//  NetworkContoller.m
//  ComplexChatIOS
//
//  Created by Anton Dosov on 23.05.15.
//  Copyright (c) 2015 Anton Dosov. All rights reserved.
//

#import "NetworkContoller.h"

@implementation NetworkContoller


+ (NetworkContoller *)sharedClient {
    static NetworkContoller *_sharedClient = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        _sharedClient = [[self alloc] initWithBaseURL:[NSURL URLWithString:nil]];
        _sharedClient.securityPolicy = [AFSecurityPolicy policyWithPinningMode:AFSSLPinningModeNone];
        
        
        
        
    });
    return _sharedClient;
}

-(id)initWithBaseURL:(NSURL *)url{
    
    self = [super initWithBaseURL:[NSURL URLWithString:nil]];
    if (!self) {
        return nil;
    }
    
    self.responseSerializer = self.responseSerializer = [AFCompoundResponseSerializer compoundSerializerWithResponseSerializers:@[[AFJSONResponseSerializer serializer],[AFHTTPResponseSerializer serializer]]];
    self.requestSerializer = [AFHTTPRequestSerializer serializer];
    [self.requestSerializer setTimeoutInterval:40.0];
    
    return self;
    
}

-(void)getNewMessages:(NSUInteger)actionID
               userID:(NSString*)userID
             username:(NSString*)username
              success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
              failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    
    NSString* actionIDstring = [NSString stringWithFormat:@"%ld",(long)actionID];
    
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            actionIDstring, @"actionID",
                            userID, @"userID",
                            username, @"username",
                            nil];
    
    [self GET:@"http://localhost:8080/ServletPostRequests" parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
        
       NSDictionary* dict =  [NSJSONSerialization JSONObjectWithData:responseObject options:kNilOptions error:nil];
        
        NSArray* posts = dict[@"posts"];
        success(task,posts);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        
        failure(task,error);
        
    }];
}

-(void)postNewMessageWithUserID:(NSString*)userID
                       username:(NSString*)username
                    messageText:(NSString*)text
                        success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
                        failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            @"add", @"requestType",
                            userID, @"userID",
                            username, @"username",
                            @"-1", @"messageID",
                            text, @"messageText",
                            nil];
    
    [self POST:@"http://localhost:8080/ServletPostRequests" parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
        success(task,responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        failure(task,error);
    }];

}

-(void)postNewUsernameWithUserID:(NSString*)userID
                     oldUsername:(NSString*)oldUsername
                     newUsername:(NSString*)newUsername
                         success:(void (^)(NSURLSessionDataTask *task, NSArray* responseArray))success
                         failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    
    
    NSDictionary *params = [NSDictionary dictionaryWithObjectsAndKeys:
                            @"username", @"requestType",
                            userID, @"userID",
                            oldUsername, @"username",
                            @"-1", @"messageID",
                            newUsername, @"messageText",
                            nil];
    
    [self POST:@"http://localhost:8080/ServletPostRequests" parameters:params success:^(NSURLSessionDataTask *task, id responseObject) {
        success(task,responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        failure(task,error);
    }];
    
    
}

-(void)getUserIDsuccess:(void (^)(NSURLSessionDataTask *task, NSString* userID))success
failure:(void (^)(NSURLSessionDataTask *task, NSError *error))failure{
    
    [self GET:@"http://localhost:8080/ServletGetUserID" parameters:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        NSString *userIDString = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];

        success(task,userIDString);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        failure(task,error);
    }];
}






@end
