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
    roll = Column(String(17), nullable="false")
    password = Column(String(16), nullable="false")
    privilege = Column(Integer)

    @property
    def serialize(self):
        return {
            'name': self.name,
            'roll': self.roll,
            'id': self.id,
            'password': self.password,
            'privilege': self.privilege,
        }


class Clubs(Base):
    __tablename__ = 'clubs'
    id = Column(Integer, primary_key=True)
    name = Column(String(50))

    @property
    def serialize(self):
        return {
            'id': self.id,
            'name': self.name,
        }


class Subscription(Base):
    __tablename__ = 'subscription'
    id = Column(Integer, primary_key=True)
    club_id = Column(Integer, ForeignKey('clubs.id'))
    user_id = Column(Integer, ForeignKey('users.id'))
    value = Column(Integer)
    users = relationship(Users)
    clubs = relationship(Clubs)

    @property
    def serialize(self):
        return {
            'id': self.id,
            'club_id': self.club_id,
            'user_id': self.user_id

        }


class Post(Base):
    __tablename__ = 'post'

    id = Column(Integer, primary_key=True)
    content = Column(String(1000), nullable="false")
    user_id = Column(Integer, ForeignKey('users.id'))
    likes = Column(Integer)
    users = relationship(Users)
    created_date = Column(String(30))

    @property
    def serialize(self):
        return {
            'id': self.id,
            'content': self.content,
            'user_id': self.user_id,
            'likes': self.likes,
        }


class Comment(Base):
    __tablename__ = 'comment'

    id = Column(Integer, primary_key=True)
    content = Column(String(250), nullable="false")
    created_date = Column(String(30))
    user_id = Column(Integer, ForeignKey('users.id'))
    post_id = Column(Integer, ForeignKey('post.id'))
    likes = Column(Integer)
    users = relationship(Users)
    post = relationship(Post)

    @property
    def serialize(self):
        return {
            'id': self.id,
            'content': self.content,
            'created_date': self.created_date,
            'user_id': self.user_id,
            'post_id': self.post_id,
            'likes': self.likes,
        }


class ClubPost(Base):
    __tablename__ = 'clubpost'

    id = Column(Integer, primary_key=True)
    content = Column(String(1000), nullable="false")
    created_date = Column(String(30))
    user_id = Column(Integer, ForeignKey('users.id'))
    likes = Column(Integer)
    users = relationship(Users)
    club_id = Column(Integer, ForeignKey('clubs.id'))
    clubs = relationship(Clubs)

    @property
    def serialize(self):
        return {
            'id': self.id,
            'content': self.content,
            'created_date': self.created_date,
            'user_id': self.user_id,
            'likes': self.likes,
            'club_id': self.club_id,
        }


class ClubComment(Base):
    __tablename__ = 'clubcomment'
    id = Column(Integer, primary_key=True)
    content = Column(String(250), nullable="false")
    created_date = Column(String(30))
    user_id = Column(Integer, ForeignKey('users.id'))
    clubpost_id = Column(Integer, ForeignKey('clubpost.id'))
    likes = Column(Integer)
    users = relationship(Users)
    clubpost = relationship(ClubPost)

    @property
    def serialize(self):
        return{
            'id': self.id,
            'content': self.content,
            'created_date': self.created_date,
            'user_id': self.user_id,
            'likes': self.likes,
            'clubpost_id': self.club_id,
        }

engine = create_engine('sqlite:///forum.db')
Base.metadata.create_all(engine)
