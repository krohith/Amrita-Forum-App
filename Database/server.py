from flask import Flask, render_template, request, redirect, url_for, jsonify
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from forum_DBSetup import Base, Users, Clubs, Subscription, Post, Comment, ClubPost, ClubComment
from datetime import datetime

app = Flask(__name__)

engine = create_engine('sqlite:///forum.db')
Base.metadata.bind = engine

DBSession = sessionmaker(bind=engine)
session = DBSession()


@app.route('/users/JSON')
def ksers():
    users = session.query(Users).all()
    return jsonify(members=[i.serialize for i in users])


@app.route('/subscription/<ids>/JSON')
def display(ids):
    user = session.query(Users).filter_by(roll=ids).one()
    clubs = session.query(Subscription.club_id).filter_by(user_id=user.id, value=1).all()
    post = []
    club = []
    username = []
    for i in clubs:
        post.append(session.query(ClubPost).filter_by(club_id=i[0]).order_by(ClubPost.id.desc()).all())
        c_id = session.query(ClubPost.club_id).filter_by(club_id=i[0]).order_by(ClubPost.id.desc()).all()
        useid = session.query(ClubPost.user_id).filter_by(club_id=i[0]).order_by(ClubPost.id.desc()).all()
        for u in useid:
            username.append(session.query(Users.name).filter_by(id=u[0]).one())
        for c in c_id:
            club.append(session.query(Clubs.name).filter_by(id=c[0]).one())
    return jsonify(post=[k.serialize for p in post for k in p], clubnames=[c for p in club for c in p],
                   usernames=[u for p in username for u in p])


@app.route('/create/<ids>/<cds>', methods=['GET', 'POST'])
def newPost(ids, cds):
    if request.method == 'POST':
        data = request.json
        print data
        u_id = session.query(Users.id).filter_by(roll=ids).one()
        c_id = session.query(Clubs.id).filter_by(name=cds).one()
        newp = ClubPost(content=data['content'], user_id=u_id[0], club_id=c_id[0], likes=0,
                        created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'))
        session.add(newp)
        session.commit()
        return display(ids)
        # else:
        #     pos = session.query(Post).filter_by(user_id=ids).all()
        #     return jsonify(posts=[i.serialize for i in pos])


@app.route('/userpost/<int:p_id>', methods=['POST'])
def like(p_id):
    data = request.json
    post = session.query(Post).filter_by(id=p_id).one()
    if data['value'] == 0:
        post.likes += 1
    else:
        post.likes -= 1
    session.commit()


@app.route('/clubpost/<int:p_id>', methods=['POST'])
def clublike(p_id):
    data = request.json
    post = session.query(ClubPost).filter_by(id=p_id).one()
    post.likes += 1
    session.commit()
    return ksers()

@app.route('/comment/<int:p_id>/<roll>', methods=['POST'])
def do_comment(p_id, roll):
    data = request.json
    u_id = session.query(Users.id).filter_by(roll=roll).one()
    newcom = Comment(content=data['content'], user_id=u_id, post_id=p_id,
                     created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'), likes=0)
    session.add(newcom)
    session.commit()
    return ksers()

@app.route('/clubcom/<int:p_id>/<roll>', methods=['POST'])
def do_com(p_id, roll):
    data = request.json
    u_id = session.query(Users.id).filter_by(roll=roll).one()
    newcom = ClubComment(content=data['content'], user_id=u_id, clubpost_id=p_id,
                         created_date=datetime.now().strftime('%Y-%m-%d %H:%M:%S'), likes=0)
    session.add(newcom)
    session.commit()
    return ksers()

@app.route('/login', methods=['POST'])
def login():
    data = request.json
    isthere = session.query(Users).filter_by(roll=data['roll']).first()

    if isthere is None:
        return jsonify(auth={"value": 0})
    elif isthere.password == data['password']:
        clubssubs = session.query(Subscription).filter_by(user_id=isthere.id, value=1).all()
        clubs = []
        for i in clubssubs:
            clubs.append(session.query(Clubs).filter_by(id=i.club_id).one())
        s = {"value": 1}
        return jsonify(clubs=[i.serialize for i in clubs], auth=s, user=isthere.name)
    else:
        return jsonify(auth={"value": 0})


@app.route('/fetch/<int:u_id>/<int:c_id>/')
def fet(u_id, c_id):
    user = session.query(Users.name).filter_by(id=u_id).one()
    club = session.query(Clubs.name).filter_by(id=c_id).one()
    return jsonify(names=[user, club])


@app.route('/subscribe/<rollno>')
def subscribe(rollno):
    clubs = session.query(Clubs).all()
    user = session.query(Users).filter_by(roll=rollno).one()
    value = []
    for i in clubs:
        k = session.query(Subscription.value).filter_by(user_id=user.id, club_id=i.id).first()
        if k is None:
            subs = Subscription(club_id=i.id, user_id=user.id, value=0)
            session.add(subs)
            session.commit()
        k = session.query(Subscription.value).filter_by(user_id=user.id, club_id=i.id).one()
        value.append(k)
    return jsonify(clubs=[i.serialize for i in clubs], values=[j for j in value])


@app.route('/change/<rollno>/<cname>')
def change(rollno, cname):
    user = session.query(Users).filter_by(roll=rollno).one()
    club = session.query(Clubs).filter_by(name=cname).one()
    subs = session.query(Subscription).filter_by(user_id=user.id, club_id=club.id).one()
    if subs.value:
        subs.value = 0
        session.commit()
    else:
        subs.value = 1
        session.commit()
    return subscribe(rollno)


@app.route('/forum')
def post():
    posts = session.query(Post).order_by(Post.id.desc()).all()
    users = []
    for i in posts:
        users.append(session.query(Users.name).filter_by(id=i.user_id).one())
    return jsonify(pos=[i.serialize for i in posts], user=[j for j in users])


if __name__ == '__main__':
    app.debug = True
    app.run(host='0.0.0.0', port=8080)
