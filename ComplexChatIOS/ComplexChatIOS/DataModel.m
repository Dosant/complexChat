//
//  DataModel.m
//  ComplexChatIOS
//
//  Created by Anton Dosov on 23.05.15.
//  Copyright (c) 2015 Anton Dosov. All rights reserved.
//

#import "DataModel.h"
#import "Message.h"

@implementation DataModel


- (instancetype)init
{
    self = [super init];
    if (self) {
        
        
        self.messages = [[NSMutableArray alloc] init];
        
        
        JSQMessagesBubbleImageFactory *bubbleFactory = [[JSQMessagesBubbleImageFactory alloc] init];
        
        self.outgoingBubbleImageData = [bubbleFactory outgoingMessagesBubbleImageWithColor:[UIColor jsq_messageBubbleLightGrayColor]];
        self.incomingBubbleImageData = [bubbleFactory incomingMessagesBubbleImageWithColor:[UIColor colorWithRed:(float)255/255 green:(float)102/255 blue:(float)2/255 alpha:1.0]];
        
        
    }
    
    return self;
}


-(void)addActionToData:(NSDictionary*) post{
    
    Message* message = [[Message alloc] initWithDictionary:post];
    
    if ([message.requestType isEqualToString: @"add"]) {
        [self.messages addObject:message];
    } else if (([message.requestType isEqualToString: @"edit"])){
        for(Message* m in self.messages){
            if(m.messageID == message.messageID){
                m.text = [NSString stringWithFormat:@"%@  (edited)",message.text];
                break;
            }
        }
    } else if (([message.requestType isEqualToString: @"delete"])){
        for(Message* m in self.messages){
            if(m.messageID == message.messageID){
                [self.messages removeObject:m];
                break;
            }
        }
    }
    
    else if (([message.requestType isEqualToString: @"username"])){
        for(Message* m in self.messages){
            if([m.senderId isEqualToString:message.senderId]){
                
                m.senderDisplayName = message.text;
                
            }
        }
    }

    
}

    


@end
