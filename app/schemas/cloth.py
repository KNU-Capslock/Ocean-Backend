from pydantic import BaseModel
from datetime import datetime
from typing import Optional

class ClothBase(BaseModel):
    type: Optional[str] = None
    detail: Optional[str] = None
    print: Optional[str] = None
    texture: Optional[str] = None
    style: Optional[str] = None

class ClothCreate(ClothBase):
    user_id: int

class ClothUpdate(ClothBase):
    pass

class Cloth(ClothBase):
    id: int
    image_src: str
    user_id: int
    created_at: datetime
    updated_at: datetime

    class Config:
        from_attributes = True 