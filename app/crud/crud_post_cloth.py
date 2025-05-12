from typing import Any, Dict, List, Optional, Union
from sqlalchemy.orm import Session

from app.crud.base import CRUDBase
from app.models.post_cloth import PostCloth
from app.schemas.post_cloth import PostClothCreate, PostClothUpdate

class CRUDPostCloth(CRUDBase[PostCloth, PostClothCreate, PostClothUpdate]):
    def get_by_post_id(
        self, db: Session, *, post_id: int, skip: int = 0, limit: int = 100
    ) -> List[PostCloth]:
        return (
            db.query(PostCloth)
            .filter(PostCloth.post_id == post_id)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def get_by_cloth_id(
        self, db: Session, *, cloth_id: int, skip: int = 0, limit: int = 100
    ) -> List[PostCloth]:
        return (
            db.query(PostCloth)
            .filter(PostCloth.cloth_id == cloth_id)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def create(self, db: Session, *, obj_in: PostClothCreate) -> PostCloth:
        db_obj = PostCloth(
            cloth_id=obj_in.cloth_id,
            post_id=obj_in.post_id,
            user_id=obj_in.user_id,
        )
        db.add(db_obj)
        db.commit()
        db.refresh(db_obj)
        return db_obj

    def update(
        self, db: Session, *, db_obj: PostCloth, obj_in: Union[PostClothUpdate, Dict[str, Any]]
    ) -> PostCloth:
        if isinstance(obj_in, dict):
            update_data = obj_in
        else:
            update_data = obj_in.dict(exclude_unset=True)
        return super().update(db, db_obj=db_obj, obj_in=update_data)

post_cloth = CRUDPostCloth(PostCloth) 