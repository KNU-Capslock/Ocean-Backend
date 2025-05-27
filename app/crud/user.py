import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy.orm import Session
from models.user import User
from schemas.user import UserCreate
# from core.security import get_password_hash, verify_password_hash
from typing import Optional

def get_user(db: Session, user_id: int) -> Optional[User]:
    return db.query(User).filter(User.id == user_id).first()

def get_user_by_name(db: Session, username: str) -> Optional[User]:
    return db.query(User).filter(User.name == username).first()

def get_users(db: Session, skip: int = 0, limit: int = 100):
    return db.query(User).offset(skip).limit(limit).all()

def create_user(db: Session, user: UserCreate) -> User:
    # hashed_password = get_password_hash(user.password)
    db_user = User(
        name=user.name,
        gender=user.gender,
        birthDate=user.birthDate,
        hashed_password=user.password  # 임시로 비밀번호를 그대로 저장
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

# def verify_password(plain_password: str, hashed_password: str) -> bool:
#     return verify_password_hash(plain_password, hashed_password) 