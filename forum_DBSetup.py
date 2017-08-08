import os
import sys
from sqlalchemy import Column, ForeignKey, Integer, String,
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base()

class Users(Base):
    __tablename__ = 'users'

    id = Column(Integer,primary_key=True)
    name = Column(String(25))
    subscription = Column(String[30])
    privilage = Column(String)
    
class Comment(Base):
    __tablename__='comment'

    id = Column(Integer,primary_key=True)
    content = Column(String[250])
    #date = Column(
    user_id = Column(Integer, ForeignKey('users.id'))
    post_id = Column(Integer, ForeignKey('post.id'))
    likes = Column(Integer)
    users = relationship(Users)
    post  = relationship(post)
class Post(Base):
    __tablename__ = 'post'

    id = Column(Integer,primary_key=True)
    content = Column(String[1000])
    user_id = Column(Integer, ForeignKey('users.id'))
    likes = Column(Integer)
class clubs(Base):
    id = Column(Integer,primary_key=True)
    name = Column(String[50])
    user_id = Column(Integer, ForeignKey('users.id'))
