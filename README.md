# HAFM
#Adulting Financial Manager: Updating an old project  
This project is based on code written shortly after I learned Java. It's not particularly great.
My main intent with this project is to update and fix the bugs I left.  
  
-xeranimus  
  
In the process of making everything much more object-oriented, including the way Transaction interacts with Category. I am in the process of moving the functionality over to "apply()" and "unapply()". For the older functionality, look at the commit before this, or look at the addCategory(category) call I commented out in the Transaction constructor.  
  
Another important point, regarding backwards compatibility: Following this commit, the Category "remaining" data field is saved, and is required when loading things in. (This is due to the "apply()" and "unapply()" methods.) Obviously, nobody but me has used this program at the time of writing, so I think the lack of backwards compatibility shouldn't be an issue.  
  
This is a fully workable version! Once I trim the fat (i.e. all the methods obviated by my current stuff) I'm going to make better documentation.  