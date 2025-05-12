import sys, os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "..")))

from sqlalchemy.orm import Session
from models.cloth import Cloth
from schemas.cloth import ClothCreate, ClothUpdate
from typing import List, Optional
from datetime import datetime

def get_cloth(db: Session, cloth_id: int) -> Optional[Cloth]:
    return db.query(Cloth).filter(Cloth.id == cloth_id).first()

def get_clothes(db: Session, skip: int = 0, limit: int = 100) -> List[Cloth]:
    return db.query(Cloth).offset(skip).limit(limit).all()

def get_clothes_by_user(db: Session, user_id: int, skip: int = 0, limit: int = 100) -> List[Cloth]:
    return db.query(Cloth).filter(Cloth.user_id == user_id).offset(skip).limit(limit).all()

def create_cloth(db: Session, cloth: ClothCreate, image_src: str) -> Cloth:
    current_time = datetime.utcnow()
    db_cloth = Cloth(
        type=cloth.type,
        detail=cloth.detail,
        print=cloth.print,
        texture=cloth.texture,
        style=cloth.style,
        image_src=image_src,
        user_id=cloth.user_id
    )
    db.add(db_cloth)
    db.commit()
    db.refresh(db_cloth)
    return db_cloth

def update_cloth(db: Session, cloth_id: int, cloth: ClothUpdate) -> Optional[Cloth]:
    db_cloth = get_cloth(db, cloth_id)
    if db_cloth:
        update_data = cloth.dict(exclude_unset=True)
        for key, value in update_data.items():
            setattr(db_cloth, key, value)
        db.commit()
        db.refresh(db_cloth)
    return db_cloth

def delete_cloth(db: Session, cloth_id: int) -> bool:
    db_cloth = get_cloth(db, cloth_id)
    if db_cloth:
        db.delete(db_cloth)
        db.commit()
        return True
    return False 