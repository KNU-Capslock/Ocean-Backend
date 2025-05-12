import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy.orm import Session
from models.post import Post
from models.user import User
from schemas.post import PostCreate, PostUpdate, PostFilter
from typing import List, Optional

def get_post(db: Session, post_id: int) -> Optional[Post]:
    return db.query(Post).filter(Post.id == post_id).first()

def get_posts_by_username(db: Session, username: str) -> List[Post]:
    return db.query(Post).join(Post.user).filter(User.name == username).order_by(Post.created_at.desc()).all()

def get_all_posts(db: Session) -> List[Post]:
    return db.query(Post).order_by(Post.created_at.desc()).all()

def create_post(db: Session, post: PostCreate, image_src: str, user_id: int) -> Post:
    db_post = Post(
        content=post.content,
        image_src=image_src,
        user_id=user_id
    )
    db.add(db_post)
    db.commit()
    db.refresh(db_post)
    return db_post

def update_post(db: Session, post_id: int, post: PostUpdate) -> Optional[Post]:
    db_post = get_post(db, post_id)
    if db_post:
        for key, value in post.dict(exclude_unset=True).items():
            setattr(db_post, key, value)
        db.commit()
        db.refresh(db_post)
    return db_post

def delete_post(db: Session, post_id: int) -> bool:
    db_post = get_post(db, post_id)
    if db_post:
        db.delete(db_post)
        db.commit()
        return True
    return False 