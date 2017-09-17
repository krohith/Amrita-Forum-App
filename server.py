from flask import Flask, render_template, request, redirect, url_for, jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from forum_DBSetup import Base, Users, Clubs, Subscription, Post, Comment, ClubPost, ClubComment

app = Flask(__name__)

engine = create_engine('sqlite:///forum.db')
Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
session = DBSession()


@app.route('/users/JSON')
def ksers():
    users = session.query(Users).all()
    return jsonify(members=[i.serialize for i in users])


if __name__ == '__main__':
    app.debug = True
app.run(host='0.0.0.0', port=5000)
