from flask import Flask, render_template, request, redirect, url_for, jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from forum_DBSetup import Base, Users, Clubs, Subscription, Post, Comment, ClubPost, ClubComment
import datetime

app = Flask(__name__)

engine = create_engine('sqlite:///forum.db')
Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
session = DBSession()


@app.route('/users/JSON')
def ksers():
    users = session.query(Users).all()
    return jsonify(members=[i.serialize for i in users])


@app.route('/subscription/<int:ids>/JSON')
def display(ids):
    clubs = session.query(Subscription.club_id).filter_by(user_id=ids, value=1).all()
    post = []
    for i in clubs:
        post.append(session.query(ClubPost).filter_by(club_id=i[0]).all())

    return jsonify(post=[k.serialize for p in post for k in p])


@app.route('/create/<int:ids>', methods=['GET', 'POST'])
def newPost(ids):
    if request.method == 'POST':
        data = request.json
        newp = Post(content=data['content'], user_id=ids, likes=0,
                    created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
        session.add(newp)
        session.commit()
    else:
        pos = session.query(Post).filter_by(user_id=ids).all()
        return jsonify(posts=[i.serialize for i in pos])


@app.route('/<int:u_id>/<int:p_id>', methods=['GET', 'POST'])
def like(u_id, p_id):
    if request.method == 'POST':
        post = session.query(Post).filter_by(user_id=u_id, id=p_id).one()
        post.likes += 1
        session.add(post)
        session.commit()
    else:
        pos = session.query(Post).filter_by(user_id=u_id).all()
        return jsonify(posts=[i.serialize for i in pos])

if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0', port=8080)
