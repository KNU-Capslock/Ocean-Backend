import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from fastapi import APIRouter, Depends, HTTPException, status
from fastapi.security import OAuth2PasswordBearer, OAuth2PasswordRequestForm
from sqlalchemy.orm import Session
from typing import List
import logging

from core.database import get_db
from schemas.user import User, UserCreate
from crud import user as user_crud
from core.security import verify_password_hash, create_access_token, verify_token

logger = logging.getLogger(__name__)
router = APIRouter()

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

@router.post("/token")
async def login_for_access_token(
    form_data: OAuth2PasswordRequestForm = Depends(),
    db: Session = Depends(get_db)
):
    user = user_crud.get_user_by_name(db, username=form_data.username)
    if not user or not verify_password_hash(form_data.password, user.hashed_password):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    
    access_token = create_access_token(data={"sub": user.name})
    return {"access_token": access_token, "token_type": "bearer"}

@router.post("/user", response_model=User)
def create_user(user: UserCreate, db: Session = Depends(get_db)):
    return user_crud.create_user(db=db, user=user)

@router.get("/user", response_model=User)
def get_current_user(
    token: str = Depends(oauth2_scheme),
    db: Session = Depends(get_db)
):
    logger.debug(f"Token: {token}")
    
    try:
        # Verify token and get payload
        payload = verify_token(token)
        logger.debug(f"Token payload: {payload}")
        
        name = payload.get("sub")
        logger.debug(f"Extracted name: {name}")
        
        if name is None:
            raise HTTPException(
                status_code=status.HTTP_401_UNAUTHORIZED,
                detail="Token payload is missing 'sub' claim",
                headers={"WWW-Authenticate": "Bearer"},
            )
        
        # Get user from database using name
        user = user_crud.get_user_by_name(db, username=name)
        logger.debug(f"Found user: {user}")
        
        if user is None:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"User with name '{name}' not found"
            )
        return user
    except Exception as e:
        logger.error(f"Error in get_current_user: {str(e)}")
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail=f"Invalid authentication credentials: {str(e)}",
            headers={"WWW-Authenticate": "Bearer"},
        ) 