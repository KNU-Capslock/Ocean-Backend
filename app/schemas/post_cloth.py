from pydantic import BaseModel
from typing import Optional

class PostClothBase(BaseModel):
    cloth_id: int
    post_id: int
    user_id: int

class PostClothCreate(PostClothBase):
    pass

class PostClothUpdate(PostClothBase):
    pass

class PostClothInDBBase(PostClothBase):
    id: int

    class Config:
        from_attributes = True

class PostCloth(PostClothInDBBase):
    pass

class PostClothInDB(PostClothInDBBase):
    pass 