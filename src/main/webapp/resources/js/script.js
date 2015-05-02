//ALL UI LOGIC
//ALL DAY


// ----------- VARS

var chatView;  
var messageList = [];
var globalmessageID = 0;

var editMode = false;
var settingsMode = false;

var actionID = 0;

var currentUserName;



// Struct for adding message to UI
var newMessage = function (time,username,messageID,messageText){
    return {
        time: time,
        username: username,
        messageID: messageID,
        messageText: messageText,
        
    };
};

// Struct for sending message to server
/*
var newMessageSendRequest = function (session_id, username,room_id, messageText){
    return {
        session_id: session_id,
        message: JSON.stringify( {
            username : username,
            room_id: String(room_id),
            text: messageText,
            status: "new",
            
        })
        
    };
};
// Struct for deleting message from server
var deleteMessageRequest = function (session_id, message_id){
    return {
        session_id: session_id,
        message: JSON.stringify( {
            message_id: String(message_id),
            status: "delete",
        })
        
    };
};

var editMessageRequest = function (session_id, message_id, newMessage){
    return {
        session_id: session_id,
        message: JSON.stringify( {
            text: newMessage,
            message_id: String(message_id),
            status: "edit",
        })
        
    };
};
*/
var postRequest = function(username, requestType, messageID, messageText){
    return {
        username: username,
        requestType:requestType,
        messageID: messageID,
        messageText: messageText
    };  
};


/* TODO */
// Struct for geting message from server
var newGetRequest = function(actionID, username){
    return{
        actionID: actionID,
        username: username,    
    };
    
}

// ----------- Main Function

$( document ).ready(function(){

    handleChatSize();
    $( window ).resize(handleChatSize);
    run();

    chatView = $('.panel-body');
    scrollToBottom(chatView,false);
    chatView.css("opacity","100");
    
    
    
    $("#MessageForm").focus();
    
    
    //NewMessageAdd
    $("#SendBtn").click(userMessageSended);
    registerKeyPress(editMode);

    //Additional setup
    $("#EditBtn").click(editMessageEditMode);
    $("#CancelEditBtn").click(cancelEditMode);
    
    
    //SettingsListeners    
    $('.SettingsButton').on('click', toggleSettings);
    $("#NameSettingsApply").on('click', applySettings);
    $("#CancelSettingsBtn").on('click', toggleSettings);
    
    
    //alert(currentUserName);
	   


});

// ----------- LOGIC. Chat UI updates. Server Requests

function run() { // start of app. data restore.
    
    //var data = restore() ;
    //createAllMessages(data);
    
    //return;
    currentUserName = "GuestUser";
    
    setInterval(function(){ // Get Requset
		//alert(actionID);    
       var getRequest = newGetRequest(actionID,currentUserName)
       getRequestToServer(getRequest); // getNewActions 
       
    }, 5000)
}


function createAllMessages(data) {
    if (data != null) {
	for(var i = 0; i < data.length; i++)
		addMessageFromData(data[i]);
    };    
}

function restore() {
	if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}
    
	var item = localStorage.getItem("data");
    var messageIDopt = localStorage.getItem("messageID");
    if (messageIDopt != null){
        globalmessageID = messageIDopt;
    }
    var actionIDopt = localStorage.getItem("actionID");
    if (actionIDopt != null){
        actionID = actionIDopt;
    }
    
    var userNameopt = localStorage.getItem("userName");
    if (userNameopt != null){
        currentUserName = userNameopt;
        $("#NameForm").val(currentUserName);
    } else {
        
        currentUserName = "GuestUser";
        
    }
    
    
    
    
    

	return item && JSON.parse(item);
}

function store(){
    if(typeof(Storage) == "undefined") {
		alert('localStorage is not accessible');
		return;
	}

	localStorage.setItem("data", JSON.stringify(messageList));
    localStorage.setItem("actionID", actionID);
    localStorage.setItem("userName", currentUserName);
    
}

function addMessageFromData(message){
    addNewMessage(message);
    
}

function deleteFromList(messageID){
    for (var i = 0 ; i < messageList.length ; i++){
        if (messageList[i].messageID == messageID){
            messageList.splice(i,1);
            store();
            continue;
        }
    }

}

function editInList(messageID,newText){
    for (var i = 0 ; i < messageList.length ; i++){
        if (messageList[i].messageID == messageID){
            messageList[i].messageText = newText;
            store();
            continue;
        }
    }

}


function postRequestToServer(data){
    $.ajax({
        method: "POST",
        url: "ServletPostRequests",
        data: data,
        success: function(response){
            
            alert("Successfull post response");
            
        },
        error: function(response){
            
            alert("Error in post response");
            
        }
    });
};





function getRequestToServer(data){ 
    
    //alert(JSON.stringify(message1));
    
    $.ajax({
    method: "GET",
    url: "ServletPostRequests",
    data: data,
    success: function(data){
        
        //alert(data);
        getActionFromServerWithJSON(data); 
    },
    error: function(data){
        alert("getAction: getError");
    }    
    });
}



function getActionFromServerWithJSON(jsonData){
    if (jsonData.length == 0)
        return;



    var dataFromServer = $.parseJSON(jsonData);
    var newMessages = dataFromServer["posts"];



    for( i = 0; i < newMessages.length; i++){

        var messageFromServer = newMessages[i];

        newActionID = parseInt(messageFromServer.actionID);
        actionID = Math.max(actionID, newActionID);
        
        var requestType = messageFromServer.requestType;
        
        if(requestType == "add"){ // Add


        var message = newMessage(messageFromServer.time,
                                messageFromServer.username,
                                messageFromServer.messageID,
                                messageFromServer.messageText);




        addNewMessage(message);




        } else if (requestType == "delete") { // DELETE
            var messageIDToDelete = messageFromServer.messageID;
            deleteMessageByID(messageIDToDelete);

            
        } else if(requestType == "edit"){// EDIT 
            var messageIDToEdit = messageFromServer.messageID;
            var editMessageText = messageFromServer.messageText;

            editMessageByIDandNewText(messageIDToEdit,editMessageText);

        } else if (requestType == "username"){ // change username
            
            var usernameOld = messageFromServer.username;
            var usernameNew = messageFromServer.messageText;
            
            console.log(usernameNew + ", old: " + usernameOld);
            
            
            
            var messageBlocks = $("li[data-username|='"+usernameOld+"']");
            messageBlocks.attr("data-username",[usernameNew]);
            messageBlocks.find(".NickName").text(usernameNew);
            
            
            if(currentUserName == usernameOld){
                currentUserName = usernameNew;
            }
            
        }
    }      
}
    
    

// UI
function addNewMessage(message){

    var messBlock = $(".media-list > .media:first").clone();
    messBlock.find(".message").text(message.messageText);
    messBlock.find(".NickName").text(message.username);
    messBlock.find(".Time").text(message.time);
    messBlock.attr("data-messageID",[message.messageID]);
    messBlock.attr("data-username",[message.username]);
    
    messBlock.removeClass("hidden"); 
    
    if(currentUserName == message.username){ // SameUser 
    messBlock.find(".deleteMessage").click(userMessageDelete); 
    messBlock.find(".editMessage").click(userMessageEdit);
    } else { // Can't delete and edit this message
        
    messBlock.find(".deleteMessage").remove(); 
    messBlock.find(".editMessage").remove();
        
    }
    
    
    messBlock.appendTo(".media-list");

    messageList.push(message);
    store();
    
	scrollToBottom(chatView,false);
}; 




var userMessageSended = function(){ // new message sended by user
    
    var messageForm = $("#MessageForm");
    var messageText = messageForm.val();
    
    var prepareNewMessageForSend = postRequest(currentUserName,"add","-1",messageText);
                                            
    
    if(!isMessageValid(prepareNewMessageForSend.messageText)){
        
        return;
    }
    
    messageForm.val("");
    

    //checkServerStatus();
    
   // postAction(action);
   //addNewMessage(message);
    
    postRequestToServer(prepareNewMessageForSend);
    
    //store();
    
    
    scrollToBottom(chatView,true);
    
    messageFormRefresh();
    
    
    
    
   
    

};

function deleteMessageByID(messageID){

    var messBlock = $(".media-list > .media[data-messageID="+messageID+"]");
    //alert(messBlock.text());
    messBlock.remove();
    
    deleteFromList(messageID);
    store();
    
}

function editMessageByIDandNewText(messageID, messageText){
    //alert("new text = " + messageText);
    var messBlock = $(".media-list > .media[data-messageID="+messageID+"]");
    var textUI = messBlock.find(".message");
    var editedSpan = messBlock.find(".edited");
    editedSpan.removeClass("hidden");
    textUI.text(messageText);
    
    
    scrollToBottom();
    
    editInList(messageIDtoEdit,messageToEditGlobal.text());
    store();
}

var userMessageDelete = function(evt){
    
    if (editMode)
        return;
    
    var sure = confirm("Delete Message?"); // TODO: - Nice alert view. And Animation
    if (!sure) {
        return false;
    }
    
    var messageToDelete = $(this).parents("li.media");
    var messageIDtoDelete = parseInt(messageToDelete.attr("data-messageID"));
    //deleteFromList(messageIDtoDelete);
    //messageToDelete.remove(); //TODO : - Awesome animation ASAP
    alert('delete ' + messageIDtoDelete);
    
    var postDeleteRequest = postRequest(currentUserName,
                                                "delete",                                                    messageIDtoDelete.toString(),
                                                "");
         
    
    postRequestToServer(postDeleteRequest);
    
};


var userMessageEdit = function(evt){ // ENTER EDIT MODE
    
    if (editMode)
        return;
    
    var messageToEdit = $(this).parents("li.media");
    messageToEditGlobal = messageToEdit.find(".message");
    
    UIToggleEditMode();
    

}; 

var messageToEditGlobal;

var editMessageEditMode = function(){ // EDIT MESSAGE 
    if (!editMode)
        return;
    
    var text = $("#MessageForm").val();
    if (!isMessageValid(text))
        return;
    
   
    
    
    messageToEditGlobal.text("Editing...");
    var messageIDtoEdit = parseInt(messageToEditGlobal.parents("li.media").attr("data-messageID"));
    
    var postEditRequest = postRequest(currentUserName,
                                                "edit",
                                                messageIDtoEdit.toString(),
                                                text);
         
    postRequestToServer(postEditRequest);
    
    
    
    cancelEditMode();
}

var cancelEditMode = function(){
    if (!editMode)
        return;
    UIToggleEditMode();
}


function UIToggleEditMode(){
    if (editMode){
        editMode = false;
        messageFormRefresh();
        messageToEditGlobal.removeClass("editModeEnabled");
        $("#SendBtn").show();
        $("#EditBtn").addClass("hidden");
        $("#CancelEditBtn").addClass("hidden");
        registerKeyPress();
        
    } else {
        editMode = true;
        registerKeyPress(editMode);
        messageToEditGlobal.addClass("editModeEnabled");
        var textToEdit = messageToEditGlobal.text();
        $("#MessageForm").val(textToEdit);
        $("#MessageForm").focus();
        $("#SendBtn").hide();
        $("#EditBtn").removeClass("hidden");
        $("#CancelEditBtn").removeClass("hidden");
        
    }
    
}


// ----------- UI details you do not need to carry about. 
// Really. Don't

 var handleChatSize = function handleChatSize(){
    $('.panel-body').height($('body').height() - $('.panel-footer').height() - 40);
 };


function scrollToBottom(view,animate){
    if(animate){
        view.animate({"scrollTop": $('.panel-body')[0].scrollHeight}, "slow");
    }
    else {
        view.animate({"scrollTop": $('.panel-body')[0].scrollHeight}, 0);
  
    }     
}  




var registerKeyPress = function(editMode){
    $(document).off("keypress");
    $(document).keypress(function(e) {
    if(e.which == 13 && !e.shiftKey) {
        if (editMode){
           editMessageEditMode();    
        } else {
           userMessageSended(); 
        }
        
        }   
    });
}

var registerForNameCheck = function(){
    if(settingsMode){
    $(document).keyup(function(e){
            
    var Newname = $("#NameForm").val();
    if(isMessageValid(Newname)){
        
        $("#NameSettingsApply").removeClass("disabled");
        
    } else {
        
        $("#NameSettingsApply").addClass("disabled");
        
    }  
    });
    } else {
        
       $(document).off("keyup"); 
        
    }
}


function messageFormRefresh(){
    $("#MessageForm").val("");
    $("#MessageForm").blur();
    setTimeout(function(){
        $("#MessageForm").focus();
    }, 50);
};
  
var settingsHeight = "-120px"

var toggleSettings = function(){
    if (settingsMode){
        settingsMode = false;
         $(".settings").css("z-index", "-1");   $("#SettingsButtonGlyph").removeClass("coloredGlyph");
        $( "#mainRow" ).transition({ y: '0px' });
        $('.settings').transition({ opacity: 0});
        $(".settings").css("box-shadow", "none");
        $(".settings").css("z-index", "-1");

    } else {
        settingsMode = true;
        $(".settings").css("bottom","120px");
        $(".settings").css("box-shadow", "0px 2px 3px #888888"); $("#SettingsButtonGlyph").addClass("coloredGlyph");
        $( "#mainRow" ).transition({ y: settingsHeight,
                                   });
        
        $('.settings').transition({ opacity: 100 }, function(){
            $(".settings").css("z-index", "0");
            
        });


    }
    
    registerForNameCheck();

}; 


var applySettings = function(){
    var newName = $("#NameForm").val();
    
    
    usernameChanged(currentUserName, newName);
    
    store();
    
    toggleSettings();
}

function usernameChanged(usernameOld, usernameNew){
    
    var usernameChangedRequest = postRequest(usernameOld,"username",
                                             "-1",usernameNew);
    
    postRequestToServer(usernameChangedRequest);
    
}

var isMessageValid = function(messageText){


    if (messageText.length == 0 )
        return false;

    for (var i = 0, ch; i < messageText.length; i++ ){
        ch = messageText.charCodeAt(i);
        if(ch != 32 && ch != 10){
        return true;
        }
    }
    return false;
}








