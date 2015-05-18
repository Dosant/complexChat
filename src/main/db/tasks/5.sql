Select * from users 
Where userID in (Select userID from Actions group by userID having count(userID)>=3)