//
//  Message.m
//  ComplexChatIOS
//
//  Created by Anton Dosov on 25.05.15.
//  Copyright (c) 2015 Anton Dosov. All rights reserved.
//

#import "Message.h"

@implementation Message


-(id)initWithDictionary:(NSDictionary*)dict{
    
    
    
    self = [super initWithSenderId:dict[@"userID"] senderDisplayName:dict[@"username"] date:[NSDate distantPast] text:dict[@"messageText"]];
    
    self.messageID = [dict[@"messageID"] intValue];
    self.actionID = [dict[@"actionID"] intValue];
    self.requestType = dict[@"requestType"];
    
    return self;
    
}


@end
