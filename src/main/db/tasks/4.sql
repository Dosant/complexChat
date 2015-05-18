Select * From Messages 
Where messageID in (SELECT messageID from Actions WHERE userID=1)
AND messageText like "%hello%"