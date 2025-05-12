from typing import Any, Dict, List, Optional, Union
from sqlalchemy.orm import Session

from app.crud.base import CRUDBase
from app.models.post import Post
from app.schemas.post import PostCreate, PostUpdate

class CRUDPost(CRUDBase[Post, PostCreate, PostUpdate]):
    def get_by_user_id(
        self, db: Session, *, user_id: int, skip: int = 0, limit: int = 100
    ) -> List[Post]:
        return (
            db.query(Post)
            .filter(Post.user_id == user_id)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def create(self, db: Session, *, obj_in: PostCreate) -> Post:
        db_obj = Post(
            user_id=obj_in.user_id,
            imageSrc=obj_in.imageSrc,
            content=obj_in.content,
            isPublic=obj_in.isPublic,
        )
        db.add(db_obj)
        db.commit()
        db.refresh(db_obj)
        return db_obj

    def update(
        self, db: Session, *, db_obj: Post, obj_in: Union[PostUpdate, Dict[str, Any]]
    ) -> Post:
        if isinstance(obj_in, dict):
            update_data = obj_in
        else:
            update_data = obj_in.dict(exclude_unset=True)
        return super().update(db, db_obj=db_obj, obj_in=update_data)

post = CRUDPost(Post) 