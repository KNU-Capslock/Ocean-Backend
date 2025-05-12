from pydantic import BaseModel
from typing import Optional, List
from datetime import datetime
from .cloth import Cloth

class PostBase(BaseModel):
    content: str

class PostCreate(PostBase):
    pass

class PostUpdate(PostBase):
    pass

class PostFilter(BaseModel):
    user_id: Optional[int] = None

class Post(PostBase):
    id: int
    user_id: int
    image_src: str
    created_at: datetime
    updated_at: datetime
    clothes: List[Cloth] = []

    class Config:
        from_attributes = True 