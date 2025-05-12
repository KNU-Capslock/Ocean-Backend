from typing import Any, Dict, List, Optional, Union
from sqlalchemy.orm import Session

from app.crud.base import CRUDBase
from app.models.cloth import Cloth
from app.schemas.cloth import ClothCreate, ClothUpdate

class CRUDCloth(CRUDBase[Cloth, ClothCreate, ClothUpdate]):
    def get_by_user_id(
        self, db: Session, *, user_id: int, skip: int = 0, limit: int = 100
    ) -> List[Cloth]:
        return (
            db.query(Cloth)
            .filter(Cloth.user_id == user_id)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def get_by_type(
        self, db: Session, *, type: str, skip: int = 0, limit: int = 100
    ) -> List[Cloth]:
        return (
            db.query(Cloth)
            .filter(Cloth.type == type)
            .offset(skip)
            .limit(limit)
            .all()
        )

    def create(self, db: Session, *, obj_in: ClothCreate) -> Cloth:
        db_obj = Cloth(
            user_id=obj_in.user_id,
            imageSrc=obj_in.imageSrc,
            type=obj_in.type,
            detail=obj_in.detail,
            print=obj_in.print,
            texture=obj_in.texture,
            style=obj_in.style,
        )
        db.add(db_obj)
        db.commit()
        db.refresh(db_obj)
        return db_obj

    def update(
        self, db: Session, *, db_obj: Cloth, obj_in: Union[ClothUpdate, Dict[str, Any]]
    ) -> Cloth:
        if isinstance(obj_in, dict):
            update_data = obj_in
        else:
            update_data = obj_in.dict(exclude_unset=True)
        return super().update(db, db_obj=db_obj, obj_in=update_data)

cloth = CRUDCloth(Cloth) 