import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from fastapi import APIRouter, Depends, HTTPException, UploadFile, File, status
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.orm import Session
from typing import List, Optional
import shutil
import os
from datetime import datetime
import uuid

from core.database import get_db
from schemas.post import Post, PostCreate, PostUpdate, PostFilter
from crud import post as post_crud
from crud import user as user_crud
from core.security import verify_token

router = APIRouter()
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

# 이미지 저장 디렉토리 설정
UPLOAD_DIR = os.path.join(os.path.dirname(os.path.dirname(os.path.dirname(__file__))), "uploads", "images")
os.makedirs(UPLOAD_DIR, exist_ok=True)

async def get_current_user(token: str = Depends(oauth2_scheme), db: Session = Depends(get_db)):
    try:
        payload = verify_token(token)
        name = payload.get("sub")
        if name is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Invalid authentication credentials",
                headers={"WWW-Authenticate": "Bearer"},
            )
        
        user = user_crud.get_user_by_name(db, username=name)
        if user is None:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail="User not found"
            )
        return user
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Invalid authentication credentials",
            headers={"WWW-Authenticate": "Bearer"},
        )

@router.post("/post", response_model=Post)
async def create_post(
    content: str,
    file: UploadFile = File(...),
    token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    payload = verify_token(token)
    username = payload.get("sub")
    user = user_crud.get_user_by_name(db, username=username)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    # 이미지 파일 확장자 검증
    allowed_extensions = {".jpg", ".jpeg", ".png", ".gif"}
    file_ext = os.path.splitext(file.filename)[1].lower()
    if file_ext not in allowed_extensions:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid file type. Only jpg, jpeg, png, and gif files are allowed."
        )

    # 고유한 파일명 생성 (UUID + 원본 확장자)
    unique_filename = f"{uuid.uuid4()}{file_ext}"
    file_location = os.path.join(UPLOAD_DIR, unique_filename)
    
    # 이미지 파일 저장
    try:
        contents = await file.read()
        with open(file_location, "wb") as f:
            f.write(contents)
        await file.seek(0)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Failed to save image file: {str(e)}"
        )
    
    post_data = PostCreate(content=content)
    return post_crud.create_post(db=db, post=post_data, image_src=file_location, user_id=user.id)

@router.get("/post", response_model=List[Post])
def get_user_posts(
    token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    payload = verify_token(token)
    username = payload.get("sub")
    user = user_crud.get_user_by_name(db, username=username)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    
    return post_crud.get_posts_by_username(db=db, username=username)

@router.get("/post/{username}", response_model=List[Post])
def get_posts_by_username(
    username: str,
    db: Session = Depends(get_db)
):
    user = user_crud.get_user_by_name(db, username=username)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")
    
    return post_crud.get_posts_by_username(db=db, username=username)

@router.get("/post/{post_id}", response_model=Post)
def get_post(
    post_id: int,
    db: Session = Depends(get_db)
):
    post = post_crud.get_post(db=db, post_id=post_id)
    if not post:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
    return post

@router.patch("/post/{post_id}", response_model=Post)
def update_post(
    post_id: int,
    post_update: PostUpdate,
    token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    payload = verify_token(token)
    username = payload.get("sub")
    user = user_crud.get_user_by_name(db, username=username)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    post = post_crud.get_post(db=db, post_id=post_id)
    if not post:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
    
    if post.user_id != user.id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized to update this post")
    
    return post_crud.update_post(db=db, post_id=post_id, post=post_update)

@router.delete("/post/{post_id}")
def delete_post(
    post_id: int,
    token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    payload = verify_token(token)
    username = payload.get("sub")
    user = user_crud.get_user_by_name(db, username=username)
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="User not found")

    post = post_crud.get_post(db=db, post_id=post_id)
    if not post:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
    
    if post.user_id != user.id:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="Not authorized to delete this post")
    
    # 게시물 삭제 시 이미지 파일도 함께 삭제
    try:
        if os.path.exists(post.image_src):
            os.remove(post.image_src)
    except Exception:
        pass
    
    success = post_crud.delete_post(db=db, post_id=post_id)
    if not success:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Post not found")
    return {"message": "Post deleted successfully"} 