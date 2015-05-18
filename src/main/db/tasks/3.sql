Select * From Messages 
Where messageID in (SELECT messageID from Actions WHERE userID=1)
AND messageTime like "2015-05-15%"