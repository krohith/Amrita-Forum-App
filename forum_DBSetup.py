import datetime
import os
import sys
from sqlalchemy import Column, ForeignKey, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base()


class Users(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    name = Column(String(25))
    rollno = Column(String(16))


class Clubs(Base):
    __tablename__ = 'clubs'
    id = Column(Integer, primary_key=True)
    name = Column(String(50))


class Subscription(Base):
    __tablename__ = 'subscription'
    id = Column(Integer, primary_key=True)
    club_id = Column(Integer, ForeignKey('clubs.id'))
    user_id = Column(Integer, ForeignKey('users.id'))
    value = Column(Integer)
    users = relationship(Users)
    clubs = relationship(Clubs)


class Post(Base):
    __tablename__ = 'post'

    id = Column(Integer, primary_key=True)
    content = Column(String(1000))
    user_id = Column(Integer, ForeignKey('users.id'))
    likes = Column(Integer)
    users = relationship(Users)


class Comment(Base):
    __tablename__ = 'comment'

    id = Column(Integer, primary_key=True)
    content = Column(String(250), nullable="false")
    created_date = Column(DateTime, default=datetime.datetime.utcnow)
    user_id = Column(Integer, ForeignKey('users.id'))
    post_id = Column(Integer, ForeignKey('post.id'))
    likes = Column(Integer)
    users = relationship(Users)
    post = relationship(Post)


class ClubPost(Base):
    __tablename__ = 'clubpost'

    id = Column(Integer, primary_key=True)
    content = Column(String(1000))
    user_id = Column(Integer, ForeignKey('users.id'))
    likes = Column(Integer)
    users = relationship(Users)
    club_id = Column(Integer,ForeignKey('clubs.id'))

engine = create_engine('sqlite:///forum.db')
Base.metadata.create_all(engine)
