//
//  Message.h
//  ComplexChatIOS
//
//  Created by Anton Dosov on 25.05.15.
//  Copyright (c) 2015 Anton Dosov. All rights reserved.
//

#import "JSQMessage.h"

@interface Message : JSQMessage

@property (nonatomic,strong) NSString* requestType;
@property (nonatomic,assign) NSInteger messageID;
@property (nonatomic,assign) NSInteger actionID;

-(id)initWithDictionary:(NSDictionary*)dict;



@end
