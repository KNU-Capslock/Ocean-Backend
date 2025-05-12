from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class UserBase(BaseModel):
    name: str
    gender: Optional[str] = None
    birthDate: Optional[datetime] = None

class UserCreate(UserBase):
    password: str

class UserUpdate(UserBase):
    password: Optional[str] = None

class UserInDBBase(UserBase):
    id: int
    authToken: Optional[str] = None
    hashed_password: str

    class Config:
        from_attributes = True

class User(UserInDBBase):
    pass

class UserInDB(UserInDBBase):
    pass 