from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from forum_DBSetup import Users, Base, Post , Comment , ClubPost , Clubs, Subscription, ClubComment

from datetime import datetime

engine = create_engine('sqlite:///forum.db')


Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
# A DBSession() instance establishes all conversations with the database
# and represents a "staging zone" for all the objects loaded into the
# database session object. Any change made against the objects in the
# session won't be persisted into the database until you call
# session.commit(). If you're not happy about the changes, you can
# revert all of them back to the last commit by calling
# session.rollback()
session = DBSession()

#addUsers

User1 = Users(name = "Rohith Kasthuri",roll="CB.EN.U4CSE16046", password="AskAmrita" , privilege=1)

session.add(User1)

User2 = Users(name = "Siva Subramani",roll="CB.EN.U4CSE16053", password="AskAmrita",privilege=1)
session.add(User2)

User3 = Users(name = "Sanath Kumar",roll="CB.EN.U4CSE16048", password="AskAmrita",privilege=1)

session.add(User3)

User4 = Users(name = "Guest",roll="CB.EN.U4CSEXXXXX", password="AskAmrita",privilege=0)

session.add(User4)

session.commit()
# addClubs
Club1 = Clubs(name = "t{know}")
session.add(Club1)
Club2 = Clubs(name = "ASCII")
session.add(Club2)
Club3 = Clubs(name = "Srishti")
session.add(Club3)
# Subscriptions
subscription1 = Subscription(value = 1,user_id =1,club_id =1)
session.add(subscription1)
subs2 = Subscription(value = 1,user_id = 1,club_id =2)
session.add(subs2)
subs3 = Subscription(value = 1,user_id = 2,club_id = 3)
session.add(subs3)
session.commit()
# UserPost
post1 = Post(content = "Welcome to Amrita Forum",likes=1,user_id=1, created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
session.add(post1)
session.commit()
# UserComment
comment1 = Comment(content = "Hello!",user_id=2,post_id=1,likes=1,created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
session.add(comment1)
session.commit()
# ClubPost
Cpost1 = ClubPost(content="First Post of t{know}", user_id=1, club_id=1, likes=2,created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
session.add(Cpost1)
session.commit()
#ClubComment
c = ClubComment(content = "This is the first Club Comment" , user_id =3, clubpost_id = 1,likes =2,created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
session.add(c)
session.commit()
